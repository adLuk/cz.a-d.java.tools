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

package cz.ad.gradle.plugin.jdk

import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.DependencySet
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.jvm.internal.JvmFeatureInternal
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.component.internal.DefaultJvmSoftwareComponent
import org.gradle.jvm.toolchain.JavaCompiler
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.jvm.toolchain.JavaToolchainSpec
import org.gradle.util.internal.GUtil

class MultiJdkExtension {

    protected ProjectInternal projectInternal;

    protected SourceSetContainer sourceSets;

    protected DefaultJvmSoftwareComponent mainComponent;

    protected JavaPluginExtension javaPluginExtension;

    protected JavaToolchainService toolchainService;

    protected Set<JavaToolchainSpec> toolchains;

    protected Set<MultiJdkTargetPlatform> targetPlatforms;


    MultiJdkExtension(
            ProjectInternal projectInternal,SourceSetContainer sourceSets,DefaultJvmSoftwareComponent mainComponent,
                      JavaPluginExtension javaPluginExtension, JavaToolchainService toolchainService
    ) {
        this.projectInternal = projectInternal;
        this.sourceSets = sourceSets;
        this.mainComponent = mainComponent;
        this.javaPluginExtension = javaPluginExtension
        this.toolchainService = toolchainService
    }

    Set<JavaToolchainSpec> getToolchains() {
        return toolchains
    }

    Set<MultiJdkTargetPlatform> getTargetPlatforms() {
        return targetPlatforms
    }

    Set<MultiJdkTargetPlatform> target(Action<? super MultiJdkTargetPlatform> action) {
        if (targetPlatforms == null) {
            targetPlatforms = new HashSet<>()
        }
        if (action != null) {
            MultiJdkTargetPlatform target = new MultiJdkTargetPlatform()
            action.execute(target);
            targetPlatforms.add(target)
            configurePlatform(target)
        }
        return targetPlatforms;
    }


    protected void configurePlatform(MultiJdkTargetPlatform target){
        String name="jdk"+target.getTargetPlatform().asInt();
        def mainSourceSet = mainComponent.mainFeature.getSourceSet();
        SourceSet created = createSourceSet(name, sourceSets, mainSourceSet);

        JavaVersion javaVersion = JavaVersion.toVersion(target.targetPlatform.asInt());
        if (!javaVersion.isJava9Compatible()) {
            created.java.getFilter().exclude("**/module-info.java")
        }

        Provider<JavaCompiler> compiler = toolchainService.compilerFor {
            it.getLanguageVersion().set(target.targetPlatform)
        }

        projectInternal.tasks.named("compile" + GUtil.toCamelCase(name) + "Java", JavaCompile) {
            //def depend = projectInternal.getDependencies();
            it.getJavaCompiler().set(compiler)
            it.setTargetCompatibility(target.getTargetPlatform().toString())
        }


        javaPluginExtension.registerFeature(name, {
            it.usingSourceSet(created)
//            it.capability(projectInternal.getGroup().toString(), this.projectInternal.getName(), this.projectInternal.getVersion().toString())
        })

        projectInternal.tasks.named(name+ "Classes") {
            JvmFeatureInternal mainFeature = mainComponent.getMainFeature();
            JvmFeatureInternal newFeature = mainComponent.getFeatures().findByName(name)
            configureDependencies(newFeature, mainFeature)
        }


//        def dependen =projectInternal.getDependencies();
//        JvmFeatureInternal mainFeature = mainComponent.getMainFeature();
//        def config = mainFeature.getImplementationConfiguration();
//        def dependencies = config.dependencies;
//        if(!dependencies.isEmpty()){
//
//        }
    }


    @Override
    public String toString() {
        return "MultiJdkExtension{" +
                "toolchains=" + toolchains +
                ", targetPlatform=" + targetPlatforms +
                '}';
    }

    protected SourceSet createSourceSet(String name, SourceSetContainer sourceSets, SourceSet mainSourceSet){
        return sourceSets.create(name, (Action<SourceSet>) {
            SourceDirectorySet source = mainSourceSet.getJava()
            it.getJava().source(source)
            SourceDirectorySet resources = mainSourceSet.getResources()
            it.getResources().source(resources)
        });
    }

    protected void configureDependencies(JvmFeatureInternal newFeature, JvmFeatureInternal mainTemplate) {
        configureDependencies(newFeature.getJavadocElementsConfiguration(), mainTemplate.getJavadocElementsConfiguration())
        configureDependencies(newFeature.getSourcesElementsConfiguration(), mainTemplate.getSourcesElementsConfiguration())
        configureDependencies(newFeature.getImplementationConfiguration(), mainTemplate.getImplementationConfiguration())
        configureDependencies(newFeature.getRuntimeOnlyConfiguration(), mainTemplate.getRuntimeOnlyConfiguration())
        configureDependencies(newFeature.getCompileOnlyConfiguration(), mainTemplate.getCompileOnlyConfiguration())
        configureDependencies(newFeature.getApiConfiguration(), mainTemplate.getApiConfiguration())
        configureDependencies(newFeature.getCompileOnlyApiConfiguration(), mainTemplate.getCompileOnlyApiConfiguration())
        configureDependencies(newFeature.getRuntimeClasspathConfiguration(), mainTemplate.getRuntimeClasspathConfiguration())
        configureDependencies(newFeature.getCompileClasspathConfiguration(), mainTemplate.getCompileClasspathConfiguration())
        configureDependencies(newFeature.getApiElementsConfiguration(), mainTemplate.getRuntimeElementsConfiguration())
    }

    protected void configureDependencies(Configuration newFeature, Configuration mainTemplate) {
        if (mainTemplate != null && newFeature != null) {
            DependencySet mainFeatureDep = mainTemplate.getDependencies()
            if (mainFeatureDep != null && !mainFeatureDep.isEmpty()) {
                DependencySet newFeatureDep = newFeature.getDependencies()
                newFeatureDep.addAll(mainFeatureDep)
            }
        }
    }

    protected void configureJarTask(String featureName, SourceSet featureSourceSet) {
        TaskProvider<Jar> jarTask = projectInternal.tasks.named(featureName + "Jar")
        ; jarTask.configure {
//            it.setDescription("Assemble jar archive with classes compiled by ${featureName}.")
//            it.from(featureSourceSet.getOutput())
            it.getArchiveClassifier().set(featureName)
//            it.getArchiveExtension().set("jar")
        }
    }
}
