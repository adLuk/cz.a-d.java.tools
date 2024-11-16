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

import cz.ad.gradle.plugin.jdk.spec.JdkTargetVersionSpec
import cz.ad.gradle.plugin.jdk.spec.JdkToolChainsSpec
import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.provider.Property
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.toolchain.JavaCompiler
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.jvm.toolchain.JavaToolchainSpec
import org.gradle.jvm.toolchain.internal.DefaultToolchainSpec

/**
 * Extension of plugin used to define interface for configuration of multiple JDK version release plugin and perform all
 * configuration steps required to introduce this into build process.
 */
class MultiJdkExtension implements JdkToolChainsSpec, JdkTargetVersionSpec {
    /**
     * Filter used in case when source code is bigger than version 8 and contains module definition, to be able to
     * compile it with java 8 which has no support of this file.
     */
    protected static final String MODULE_PATTERN = "**/module-info.java"
    /**
     * Constance used as name of assembly task to ensure connection between new feature and build process.
     */
    protected static final String ASSEMBLE_TASK_NAME = "assemble"
    /**
     * Project used as source of data about tasks and configuration.
     */
    protected Project project;
    /**
     * Used to create new sourceSet.
     */
    protected SourceSetContainer sourceSets;
    /**
     * Used to register feature for JDK specific version.
     */
    protected JavaPluginExtension javaPluginExtension;
    /**
     * Used to locate / download required JDK version.
     */
    protected JavaToolchainService toolchainService;
    /**
     * Toolchain configured to main sourceSet.
     */
    protected JavaToolchainSpec mainSourceToolchain;
    /**
     * Set of toolchains available from build.gradle configuration.
     */
    protected Set<JavaToolchainSpec> toolchains;
    /**
     * Internal map of compilers lazy configured in tim eof need from default toolchain and configured toolchains.
     */
    protected Map<JavaLanguageVersion, Provider<JavaCompiler>> compilers;

    /**
     * Create new instance of extension and configure it with provided parameters.
     * @param project used to obtain data about tasks and other project information
     * @param sourceSets used to create new sourceSet for JDK version specific code.
     * @param javaPluginExtension used to register feature with JDK version specific configuration.
     * @param toolchainService used to locate compiler for JDK specific version.
     */
    MultiJdkExtension(
            Project project, SourceSetContainer sourceSets,
            JavaPluginExtension javaPluginExtension, JavaToolchainService toolchainService
    ) {
        this.project = project;
        this.sourceSets = sourceSets;
        this.javaPluginExtension = javaPluginExtension
        this.toolchainService = toolchainService
        JavaToolchainSpec toolchain = this.javaPluginExtension.getToolchain()
        addToolchain(toolchain)
        this.mainSourceToolchain = toolchain
    }

    /**
     * Allowing to define multiple toolchains to be ready and available for compile process.
     * @param action configuration allowing to call toolchain multiple times.
     */
    void toolchains(Action<? super JdkToolChainsSpec> action) {
        action.execute(this)
    }

    /**
     * Allow to configure java toolchain with specific selected version to be used as part of build.
     * @param action configuration action for java toolchain specification.
     */
    @Override
    void toolchain(Action<? super JavaToolchainSpec> action) {
        if (action != null) {
            DefaultToolchainSpec
            DefaultToolchainSpec toolchainSpec = this.project.objects.newInstance(DefaultToolchainSpec.class, new Object[0]);
            action.execute(toolchainSpec);
            addToolchain(toolchainSpec)
        }
    }

    /**
     * Allowing to define multiple targets to be defined for JDK version specific compilation.
     * @param action configure action allowing to define target multiple times.
     */
    void targets(Action<? super JdkTargetVersionSpec> action) {
        action.execute(this)
    }

    /**
     * Allow configuration of single target from build gradle file as is commonly defined for other plugins.
     * @param action configure action with data from build file.
     */
    void target(Action<? super MultiJdkTargetPlatform> action) {
        if (action != null) {
            MultiJdkTargetPlatform target = new MultiJdkTargetPlatform()
            action.execute(target);
            configurePlatform(target)
        }
    }

    /**
     * Perform all configuration steps to introduce new JDK version specific feature.
     * @param target jdk version.
     */
    protected void configurePlatform(MultiJdkTargetPlatform target) {
        JavaLanguageVersion targetPlatform = target.getTargetPlatform();
        int targetPlatformAsInt = targetPlatform.asInt();

        String name = "jdk" + targetPlatformAsInt;
        Provider<JavaCompiler> compiler = getCompiler(targetPlatform)

        SourceSet mainSourceSet = findMainSourceSet();
        SourceSet created = createSourceSet(name, sourceSets, mainSourceSet);
        configureSourceSetDependencies(mainSourceSet, created);
        configureSourceSetData(created, targetPlatformAsInt)
        configureCompileTask(created, targetPlatform, compiler)

        javaPluginExtension.registerFeature(name, {
            it.usingSourceSet(created)
            it.capability(this.project.getGroup().toString(), this.project.getName(), this.project.getVersion().toString())
        })

        // jar task is create as part of process register feature, so is available after registration
        addAssembleTaskDependencies(created)
    }

    /**
     * Locate main sourceSet to be used as source of configuration and dependencies for new registered feature.
     * @return main sourceSet if exist.
     */
    protected SourceSet findMainSourceSet() {
        return sourceSets.find {
            SourceSet.isMain(it)
        }
    }

    /**
     * Create jdk specific sourceSet and extend it by sourceSet used as default project target to allow consume source
     * code provided in default project sourceSet and enhance it by platform specific code.
     * @param name define name of source set. Must not be null.
     * @param sourceSets container used to create new sourceSet. Must not be null.
     * @param mainSourceSet instance used as source for extension of new sourceSet instance by mainSourceSet
     * @return initialized instance of sourceSet.
     */
    protected SourceSet createSourceSet(String name, SourceSetContainer sourceSets, SourceSet mainSourceSet) {
        return sourceSets.create(name, (Action<SourceSet>) {
            SourceDirectorySet source = mainSourceSet.getJava()
            it.getJava().source(source)
            SourceDirectorySet resources = mainSourceSet.getResources()
            it.getResources().source(resources)
        });
    }

    /**
     * Configure new sources with compile dependencies of main source set to provide ability to compile code with
     * dependencies.
     * @param main main source set.
     * @param feature new source set created for feature.
     */
    protected void configureSourceSetDependencies(SourceSet main, SourceSet feature) {
        Configuration dependenciesSource = project.configurations.findByName(main.getCompileClasspathConfigurationName());
        project.configurations.named(feature.compileClasspathConfigurationName).configure((configuration) -> {
            configuration.extendsFrom(dependenciesSource)
        });
    }

    /**
     * Define filters for platform specific files and sourceSet contents.
     * @param sourceSet source set instance for configuration
     * @param jdkVersion numeric version of JDK used for sourceSet
     */
    protected void configureSourceSetData(SourceSet sourceSet, int jdkVersion) {
        JavaVersion javaVersion = JavaVersion.toVersion(jdkVersion);
        if (!javaVersion.isJava9Compatible()) {
            sourceSet.java.getFilter().exclude(MODULE_PATTERN)
        }
    }

    /**
     * Configure compiler for compile task with output in requested version of JDK.
     * @param sourceSet definition for new platform.
     * @param targetPlatform version of JDK which should be produced by compile task.
     * @param compiler provider of compiler capable to produce selected platform.
     */
    protected void configureCompileTask(SourceSet sourceSet, JavaLanguageVersion targetPlatform, Provider<JavaCompiler> compiler) {
        project.tasks.named(sourceSet.getCompileJavaTaskName(), JavaCompile) {
            it.getJavaCompiler().set(compiler)
            it.setTargetCompatibility(targetPlatform.toString())
            // This line is working in current project(code is in written by java 8 syntax, but for generic solution is not suitable.
            it.setSourceCompatibility(targetPlatform.toString())
        }
    }


    /**
     * adding toolchain to set of internally configured toolchains configured by plugin extension to be used.
     * @param toolchain instance of new toolchain to be added into internal set of configured toolchains. Must not
     * be null.
     */
    protected void addToolchain(JavaToolchainSpec toolchain) {
        if (toolchain != null) {
            Property<JavaLanguageVersion> version = toolchain.getLanguageVersion();
            if (version != null) {
                if (this.toolchains == null) {
                    this.toolchains = new HashSet<>();
                }
                this.toolchains.add(toolchain)
            }
        }
    }

    /**
     * Provide compiler available in toolchain configuration able to produce requested target language version.
     * @param languageVersion version of java language required to be produced by compiler.
     * @return compiler provider or null for now.
     */
    protected Provider<JavaCompiler> getCompiler(JavaLanguageVersion languageVersion) {
        Provider<JavaCompiler> retValue = null;
        if (languageVersion != null) {
            Map<JavaLanguageVersion, Provider<JavaCompiler>> compilers = initCompilersIfNeeded()
            if (compilers != null && compilers.containsKey(languageVersion)) {
                retValue = compilers.get(languageVersion)
            } else {
                retValue = compilers.get(this.mainSourceToolchain.getLanguageVersion().get())
            }
        }
        return retValue;
    }

    /**
     * Used to lazy initialize compiler for all registered platform. To obtain configuration toolchains definition is
     * used to load all JDK including default one into map with versions.
     * @return physical target jdk version connected with compiler of same jdk version
     */
    protected Map<JavaLanguageVersion, Provider<JavaCompiler>> initCompilersIfNeeded() {
        if (this.compilers == null) {
            this.compilers = new HashMap<>(this.toolchains.size());
            for (def value : this.toolchains) {
                Provider<JavaCompiler> compiler = this.toolchainService.compilerFor(value);
                def version = value.getLanguageVersion().get()
                this.compilers.put(version, compiler)
            }
        }
        return this.compilers;
    }

    /**
     * Add jar task of new feature as dependency task for assemble task if exist to ensure jar file for specific jdk is
     * created as part of build task.
     * @param sourceSet for JDK specific feature.
     */
    protected void addAssembleTaskDependencies(SourceSet sourceSet) {
        if (project.tasks.getNames().contains(ASSEMBLE_TASK_NAME)) {
            // jar task needs to be located by name otherwise configuration of assembly task fail.
            def jarTask = project.tasks.findByName(sourceSet.getJarTaskName())
            project.tasks.named(ASSEMBLE_TASK_NAME).configure((task) -> {
                task.dependsOn.add(jarTask)
            });
        }
    }
}
