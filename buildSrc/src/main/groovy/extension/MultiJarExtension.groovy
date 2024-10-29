/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package extension

import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.ConfigurationPublications
import org.gradle.api.attributes.AttributeContainer
import org.gradle.api.attributes.Bundling
import org.gradle.api.attributes.Category
import org.gradle.api.attributes.Usage
import org.gradle.api.attributes.java.TargetJvmVersion
import org.gradle.api.component.AdhocComponentWithVariants
import org.gradle.api.component.SoftwareComponentContainer
import org.gradle.api.internal.artifacts.configurations.ConfigurationRolesForMigration
import org.gradle.api.internal.artifacts.configurations.RoleBasedConfigurationContainerInternal
import org.gradle.api.internal.artifacts.dsl.LazyPublishArtifact
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.tasks.TaskDependencyFactory
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.internal.JavaConfigurationVariantMapping
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.toolchain.JavaCompiler
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.util.internal.GUtil

import javax.inject.Inject
import org.gradle.jvm.toolchain.JavaToolchainService

abstract class MultiJarExtension {
    private final JavaPluginExtension javaPluginExtension
    private final TaskContainer tasks
    private final SourceSetContainer sourceSets
    private final ObjectFactory objects
    private final JavaToolchainService javaToolchains
    private final SoftwareComponentContainer softwareContainer;
    private final RoleBasedConfigurationContainerInternal configurationContainer;
    private final TaskDependencyFactory taskDependencyFactory;
    private final FileResolver fileResolver

    @Inject
    MultiJarExtension(
            JavaPluginExtension javaPluginExtension,TaskContainer tasks, SourceSetContainer sourceSets,
            ObjectFactory objects, JavaToolchainService javaToolchains,
            SoftwareComponentContainer softwareContainer, RoleBasedConfigurationContainerInternal configurationContainer,
            TaskDependencyFactory taskDependencyFactory,FileResolver fileResolver
    ) {
        this.tasks = tasks
        this.sourceSets = sourceSets
        this.objects = objects
        this.javaToolchains = javaToolchains
        this.softwareContainer =softwareContainer
        this.configurationContainer = configurationContainer
        this.taskDependencyFactory = taskDependencyFactory
        this.fileResolver = fileResolver
        this.javaPluginExtension = javaPluginExtension;
    }

    void jdkArtifacts(int version){
        JavaLanguageVersion javaLanguageVersion = JavaLanguageVersion.of(version);

        Provider<JavaCompiler> targetCompiler = javaToolchains.compilerFor {
            it.getLanguageVersion().set(javaLanguageVersion)
        }

        String variantName = "jdk${version}"
        Configuration variantConfiguration = createConfiguration(variantName, version);
        ConfigurationPublications outgoing = variantConfiguration.getOutgoing();
        Objects.requireNonNull(outgoing);

        TaskProvider jarTask = setupJarBuild(variantName, version, targetCompiler)
        outgoing.artifact(new LazyPublishArtifact(jarTask, fileResolver, taskDependencyFactory));

        AdhocComponentWithVariants java = softwareContainer.java
        java.addVariantsFromConfiguration(variantConfiguration,
                new JavaConfigurationVariantMapping("runtime", true))
    }

    protected Configuration createConfiguration(String variantName, int version){
        Configuration variantConfiguration = configurationContainer.maybeCreateMigratingUnlocked(variantName,
                ConfigurationRolesForMigration.CONSUMABLE_DEPENDENCY_SCOPE_TO_CONSUMABLE);
        variantConfiguration.setDescription(" elements for " + variantName + ".");

        AttributeContainer attributes = variantConfiguration.getAttributes();
        attributes.attribute(Category.CATEGORY_ATTRIBUTE, (Category)objects.named(Category.class, Category.LIBRARY));
        attributes.attribute(Usage.USAGE_ATTRIBUTE, (Usage)objects.named(Usage.class, Usage.JAVA_RUNTIME));
        attributes.attribute(Bundling.BUNDLING_ATTRIBUTE, (Bundling)objects.named(Bundling.class, Bundling.EXTERNAL));
        attributes.attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, version);
        return variantConfiguration;
    }

    protected TaskProvider setupJarBuild(String variantName, int version, Provider<JavaCompiler> targetCompiler){
        def sharedSourceSet = sourceSets.main.allSource

        // Define source set for selected language version to get tasks configured and created automatically.
        def langSourceSet = sourceSets.create(variantName) {
            it.java.srcDir(sharedSourceSet)
        }
        if(!JavaVersion.toVersion(version).isJava9Compatible()){
            langSourceSet.java.getFilter().exclude("**/module-info.java")
        }

        // Use target compiler obtained by toolchain to compile java sources.
        tasks.named("compile"+ GUtil.toCamelCase(variantName)+"Java", JavaCompile) {
            it.getJavaCompiler().set(targetCompiler)
        }

        TaskProvider jarTask = tasks.register("jar${version}jdk", Jar){
            it.setGroup("build")
            it.setDescription("Assemble jar archive with classes compiled by jdk-${version}.")
            it.from(langSourceSet.output)
            it.getArchiveClassifier().set("jdk${version}")
            it.getArchiveExtension().set("jar")
        }

        if (tasks.getNames().contains("assemble")) {
            tasks.named("assemble").configure((task) -> {
                task.dependsOn.add(jarTask)
            });
        }
        return jarTask;
    }
}

