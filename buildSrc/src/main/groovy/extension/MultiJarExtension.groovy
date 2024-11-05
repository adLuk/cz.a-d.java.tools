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


import org.gradle.api.Action
import org.gradle.api.JavaVersion
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.DependencySet
import org.gradle.api.artifacts.dsl.DependencyHandler
import org.gradle.api.component.SoftwareComponentContainer
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.artifacts.configurations.RoleBasedConfigurationContainerInternal
import org.gradle.api.internal.file.FileResolver
import org.gradle.api.internal.tasks.TaskDependencyFactory
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.jvm.internal.JvmFeatureInternal
import org.gradle.api.provider.Provider
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.api.tasks.TaskContainer
import org.gradle.api.tasks.TaskProvider
import org.gradle.api.tasks.bundling.Jar
import org.gradle.api.tasks.compile.JavaCompile
import org.gradle.jvm.component.internal.JvmSoftwareComponentInternal
import org.gradle.jvm.toolchain.JavaCompiler
import org.gradle.jvm.toolchain.JavaLanguageVersion
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.util.internal.GUtil

import javax.inject.Inject
import java.util.regex.Pattern

abstract class MultiJarExtension {
    private static final Pattern VALID_FEATURE_NAME = Pattern.compile("[a-zA-Z0-9]+");
    protected Project project
    private final JavaPluginExtension javaPluginExtension
    private final TaskContainer tasks
    private final SourceSetContainer sourceSets
    private final ObjectFactory objects
    private final JavaToolchainService javaToolchains
    private final SoftwareComponentContainer softwareContainer;
    private final RoleBasedConfigurationContainerInternal configurationContainer;
    private final TaskDependencyFactory taskDependencyFactory;
    private final FileResolver fileResolver
    private final DependencyHandler dependencies

    @Inject
    MultiJarExtension(
            JavaPluginExtension javaPluginExtension, TaskContainer tasks, SourceSetContainer sourceSets,
            ObjectFactory objects, JavaToolchainService javaToolchains,
            SoftwareComponentContainer softwareContainer, RoleBasedConfigurationContainerInternal configurationContainer,
            TaskDependencyFactory taskDependencyFactory, FileResolver fileResolver, DependencyHandler dependencies, Project project
    ) {
        this.tasks = tasks
        this.sourceSets = sourceSets
        this.objects = objects
        this.javaToolchains = javaToolchains
        this.softwareContainer = softwareContainer
        this.configurationContainer = configurationContainer
        this.taskDependencyFactory = taskDependencyFactory
        this.fileResolver = fileResolver
        this.javaPluginExtension = javaPluginExtension;
        this.dependencies = dependencies
        this.project = project
    }

    void features(int ... versions) {
        def compilers = prepareCompilers(versions)
        if (!compilers.isEmpty()) {
            JvmSoftwareComponentInternal mainComponentProvider = findComponent("java")

            SourceSet main = sourceSets.find {
                SourceSet.isMain(it)
            }
            for (Map.Entry<Integer, Provider<JavaCompiler>> compilerEntry : compilers.entrySet()) {
                int version = compilerEntry.getKey();
                String featureName = "jdk" + version
                Provider<JavaCompiler> targetCompiler = compilerEntry.getValue()
                SourceSet featureSourceSet = prepareSourceSet(featureName, version, main, targetCompiler);

                javaPluginExtension.registerFeature(featureName, {
                    it.usingSourceSet(featureSourceSet)
                    it.capability(this.project.getGroup().toString(), this.project.getName(), this.project.getVersion().toString())
                    //TODO javadoc hack or real copy is question
                })
                configureJarTask(featureName, featureSourceSet)
                NamedDomainObjectProvider<JvmFeatureInternal> currentFeature = findFeature(featureName, mainComponentProvider);
                JvmFeatureInternal mainFeature = mainComponentProvider.getMainFeature()
                currentFeature.configure {
                    configureDependencies(it, mainFeature)
                }
//                JvmSoftwareComponentInternal component = getSingleJavaComponent();
//                if(component!=null){
//                    def set =component.getFeatures().findAll();
//                    NamedDomainObjectProvider<JvmFeatureInternal> currentFeature = component.getFeatures().named(featureName);
//                    currentFeature.configure {
//                        configureDependencies(it, )
//                        def implementation = it.getImplementationConfiguration()
//                        implementation.dependencies
//                    }
//                    AdhocComponentWithVariants adhocComponent = (AdhocComponentWithVariants)component;
////                    currentFeature.configure {
////
////                    }
//
//                }
            }
        }
    }

//    void jdkArtifacts(int ... versions) {
//        def compilers = prepareCompilers(versions)
//        if (!compilers.isEmpty()) {
//            AdhocComponentWithVariants java = softwareContainer.java
//
//            for (Map.Entry<Integer, Provider<JavaCompiler>> compilerEntry : compilers.entrySet()) {
//                int version = compilerEntry.getKey();
//                String variantName = "jdk" + version
//                Configuration variantConfiguration = createConfiguration(variantName, version);
//                ConfigurationPublications outgoing = variantConfiguration.getOutgoing();
//                Objects.requireNonNull(outgoing);
//                SourceSet main = sourceSets.find {
//                    SourceSet.isMain(it)
//                }
//
//                TaskProvider jarTask = setupJarBuild(variantName, version, compilerEntry.getValue())
//                outgoing.artifact(new LazyPublishArtifact(jarTask, fileResolver, taskDependencyFactory));
//
//                java.addVariantsFromConfiguration(variantConfiguration,
//                        new JavaConfigurationVariantMapping("runtime", true))
//
//            }
//        }
////        JavaLanguageVersion javaLanguageVersion = JavaLanguageVersion.of(version);
////
////        Provider<JavaCompiler> targetCompiler = javaToolchains.compilerFor {
////            it.getLanguageVersion().set(javaLanguageVersion)
////        }
////
////        String variantName = "jdk${version}"
////        Configuration variantConfiguration = createConfiguration(variantName, version);
////        ConfigurationPublications outgoing = variantConfiguration.getOutgoing();
////        Objects.requireNonNull(outgoing);
////
////        TaskProvider jarTask = setupJarBuild(variantName, version, targetCompiler)
////        outgoing.artifact(new LazyPublishArtifact(jarTask, fileResolver, taskDependencyFactory));
////
////        AdhocComponentWithVariants java = softwareContainer.java
////        java.addVariantsFromConfiguration(variantConfiguration,
////                new JavaConfigurationVariantMapping("runtime", true))
//    }

    protected Map<Integer, Provider<JavaCompiler>> prepareCompilers(int ... versions) {
        Map<Integer, Provider<JavaCompiler>> retValue = new HashMap<>();
        if (versions != null && versions.length > 0) {
            for (int version : versions) {
                JavaLanguageVersion javaLanguageVersion = JavaLanguageVersion.of(version);
                if (javaLanguageVersion != null) {
                    Provider<JavaCompiler> compiler = javaToolchains.compilerFor {
                        it.getLanguageVersion().set(javaLanguageVersion)
                    }
                    if (compiler != null) {
                        retValue.put(version, compiler);
                    }
                }
            }
        }
        return retValue;
    }

//    protected Configuration createConfiguration(String variantName, int version) {
//        Configuration variantConfiguration = configurationContainer.maybeCreateMigratingUnlocked(variantName,
//                ConfigurationRolesForMigration.CONSUMABLE_DEPENDENCY_SCOPE_TO_CONSUMABLE);
//        variantConfiguration.setDescription(" elements for " + variantName + ".");
//
//        AttributeContainer attributes = variantConfiguration.getAttributes();
//        attributes.attribute(Category.CATEGORY_ATTRIBUTE, (Category) objects.named(Category.class, Category.LIBRARY));
//        attributes.attribute(Usage.USAGE_ATTRIBUTE, (Usage) objects.named(Usage.class, Usage.JAVA_RUNTIME));
//        attributes.attribute(Bundling.BUNDLING_ATTRIBUTE, (Bundling) objects.named(Bundling.class, Bundling.EXTERNAL));
//        attributes.attribute(TargetJvmVersion.TARGET_JVM_VERSION_ATTRIBUTE, version);
//
//        def name = configurationContainer.findByName("implementation");
//        variantConfiguration.dependencies.addAll(name.dependencies)
//        return variantConfiguration;
//    }
//
//    protected TaskProvider setupJarBuild(String variantName, int version, Provider<JavaCompiler> targetCompiler) {
//        def sharedSourceSet = sourceSets.main.allSource
//        // Define source set for selected language version to get tasks configured and created automatically.
//        def langSourceSet = sourceSets.create(variantName) {
//            it.java.srcDir(sharedSourceSet)
//
//        }
//        if (!JavaVersion.toVersion(version).isJava9Compatible()) {
//            langSourceSet.java.getFilter().exclude("**/module-info.java")
//        }
//        // Use target compiler obtained by toolchain to compile java sources.
//        tasks.named("compile" + GUtil.toCamelCase(variantName) + "Java", JavaCompile) {
//            it.getJavaCompiler().set(targetCompiler)
//        }
//
//        TaskProvider jarTask = tasks.register("jar${version}jdk", Jar) {
//            it.setGroup("build")
//            it.setDescription("Assemble jar archive with classes compiled by jdk-${version}.")
//            it.from(langSourceSet.output)
//            it.getArchiveClassifier().set("jdk${version}")
//            it.getArchiveExtension().set("jar")
//        }
//
//        if (tasks.getNames().contains("assemble")) {
//            tasks.named("assemble").configure((task) -> {
//                task.dependsOn.add(jarTask)
//            });
//        }
//        return jarTask;
//    }

    protected SourceSet prepareSourceSet(String featureName, int version, SourceSet main, Provider<JavaCompiler> targetCompiler) {
        SourceSet sourceSet = sourceSets.create(featureName, (Action<SourceSet>) {
            SourceDirectorySet source = main.getJava()
            it.getJava().source(source)
            SourceDirectorySet resources = main.getResources()
            it.getResources().source(resources)
//            it.setCompileClasspath(main.getCompileClasspath())
//            it.setAnnotationProcessorPath(main.getAnnotationProcessorPath())
//            it.setRuntimeClasspath(main.getRuntimeClasspath())
//            it.java.srcDir(main)
        })
        JavaVersion javaVersion = JavaVersion.toVersion(version);
        if (!JavaVersion.toVersion(version).isJava9Compatible()) {
            sourceSet.java.getFilter().exclude("**/module-info.java")
        }
        tasks.named("compile" + GUtil.toCamelCase(featureName) + "Java", JavaCompile) {
            it.getJavaCompiler().set(targetCompiler)
            it.setTargetCompatibility(javaVersion.toString())
        }
        return sourceSet
    }

    protected void configureJarTask(String featureName, SourceSet featureSourceSet) {
        TaskProvider<Jar> jarTask = tasks.named(featureName + "Jar")
        ; jarTask.configure {
            it.setDescription("Assemble jar archive with classes compiled by ${featureName}.")
            it.from(featureSourceSet.getOutput())
            it.getArchiveClassifier().set(featureName)
            it.getArchiveExtension().set("jar")
        }
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
                newFeatureDep.addAll((DependencySet) mainFeatureDep.clone())

            }
        }
    }

//    /**
//     * Feature registration to have cance to access feature and make postconfiguration
//     */
//    protected void registerFeature(String name, Action<? super FeatureSpec> configureAction) {
//        DefaultJavaFeatureSpec spec = new DefaultJavaFeatureSpec(validateFeatureName(name), (ProjectInternal) this.project);
//        configureAction.execute(spec);
//        JvmFeatureInternal feature = spec.create();
//        JvmSoftwareComponentInternal component = this.getSingleJavaComponent();
//        if (component != null) {
//            component.getFeatures().add(feature);
//            AdhocComponentWithVariants adhocComponent = (AdhocComponentWithVariants) component;
//            Configuration javadocElements = feature.getJavadocElementsConfiguration();
//            if (javadocElements != null) {
//                adhocComponent.addVariantsFromConfiguration(javadocElements, new JavaConfigurationVariantMapping("runtime", true));
//            }
//
//            Configuration sourcesElements = feature.getSourcesElementsConfiguration();
//            if (sourcesElements != null) {
//                adhocComponent.addVariantsFromConfiguration(sourcesElements, new JavaConfigurationVariantMapping("runtime", true));
//            }
//
//            if (spec.isPublished()) {
//                adhocComponent.addVariantsFromConfiguration(feature.getApiElementsConfiguration(), new JavaConfigurationVariantMapping("compile", true, feature.getCompileClasspathConfiguration()));
//                adhocComponent.addVariantsFromConfiguration(feature.getRuntimeElementsConfiguration(), new JavaConfigurationVariantMapping("runtime", true, feature.getRuntimeClasspathConfiguration()));
//            }
//        }
//
//    }
//
//    private String validateFeatureName(String name) {
//        if (!VALID_FEATURE_NAME.matcher(name).matches()) {
//            throw new InvalidUserDataException("Invalid feature name '" + name + "'. Must match " + VALID_FEATURE_NAME.pattern());
//        } else {
//            return name;
//        }
//    }

//    @Nullable
//    private JvmSoftwareComponentInternal getSingleJavaComponent() {
//        NamedDomainObjectSet<JvmSoftwareComponentInternal> jvmComponents = this.project.getComponents().withType(JvmSoftwareComponentInternal.class);
//        if (jvmComponents.size() > 1) {
//            String componentNames = CollectionUtils.join(", ", jvmComponents.getNames());
//            throw new InvalidUserCodeException("Cannot register feature because multiple JVM components are present. The following components were found: " + componentNames);
//        } else {
//            return !jvmComponents.isEmpty() ? (JvmSoftwareComponentInternal) jvmComponents.iterator().next() : null;
//        }
//    }

    protected JvmSoftwareComponentInternal findComponent(String componentName) {
        return (JvmSoftwareComponentInternal) this.project.getComponents().findByName(componentName);
    }


    protected NamedDomainObjectProvider<JvmFeatureInternal> findFeature(String featureName, JvmSoftwareComponentInternal component) {
        return component.getFeatures().named(featureName);
    }
}

