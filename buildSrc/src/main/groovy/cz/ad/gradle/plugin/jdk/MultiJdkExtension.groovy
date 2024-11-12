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
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.DependencySet
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.jvm.internal.JvmFeatureInternal
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.component.internal.JvmSoftwareComponentInternal
import org.gradle.jvm.toolchain.JavaCompiler
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.jvm.toolchain.JavaToolchainSpec
import org.gradle.util.internal.GUtil

class MultiJdkExtension {

    protected Project project;

    protected SourceSetContainer sourceSets;

    protected JavaPluginExtension javaPluginExtension;

    protected JavaToolchainService toolchainService;

    protected Set<JavaToolchainSpec> toolchains;

    protected Set<MultiJdkTargetPlatform> targetPlatforms;


    MultiJdkExtension(
            Project project, SourceSetContainer sourceSets,
            JavaPluginExtension javaPluginExtension, JavaToolchainService toolchainService
    ) {
        this.project = project;
        this.sourceSets = sourceSets;
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


    protected void configurePlatform(MultiJdkTargetPlatform target) {
        String name = "jdk" + target.getTargetPlatform().asInt();
        Provider<JavaCompiler> compiler = toolchainService.compilerFor {
            it.getLanguageVersion().set(target.targetPlatform)
        }

        def mainSourceSet = sourceSets.find {
            SourceSet.isMain(it)
        }
        SourceSet created = createSourceSet(name, sourceSets, mainSourceSet);
        def dependenciesSource = project.configurations.findByName(mainSourceSet.compileClasspathConfigurationName);
        def variantDependencies = project.configurations.findByName(created.compileClasspathConfigurationName);
        variantDependencies.extendsFrom(dependenciesSource)


        JavaVersion javaVersion = JavaVersion.toVersion(target.targetPlatform.asInt());
        if (!javaVersion.isJava9Compatible()) {
            created.java.getFilter().exclude("**/module-info.java")
        }

        project.tasks.named(created.getCompileJavaTaskName(), JavaCompile) {
            it.getJavaCompiler().set(compiler)
            it.setTargetCompatibility(target.getTargetPlatform().toString())
        }

        javaPluginExtension.registerFeature(name, {
            it.usingSourceSet(created)
            it.capability(this.project.getGroup().toString(), this.project.getName(), this.project.getVersion().toString())
        })
    }

    protected SourceSet createSourceSet(String name, SourceSetContainer sourceSets, SourceSet mainSourceSet) {
        return sourceSets.create(name, (Action<SourceSet>) {
            SourceDirectorySet source = mainSourceSet.getJava()
            it.getJava().source(source)
            SourceDirectorySet resources = mainSourceSet.getResources()
            it.getResources().source(resources)
        });
    }

}
