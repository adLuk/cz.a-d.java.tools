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
import org.gradle.api.InvalidUserDataException
import org.gradle.api.NamedDomainObjectProvider
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.artifacts.Configuration
import org.gradle.api.artifacts.PublishArtifact
import org.gradle.api.file.SourceDirectorySet
import org.gradle.api.internal.plugins.DefaultArtifactPublicationSet
import org.gradle.api.internal.project.ProjectInternal
import org.gradle.api.model.ObjectFactory
import org.gradle.api.plugins.ExtensionContainer
import org.gradle.api.plugins.JavaBasePlugin
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.plugins.PluginContainer
import org.gradle.api.plugins.internal.DefaultJavaPluginConvention
import org.gradle.api.plugins.internal.DefaultJavaPluginExtension
import org.gradle.api.plugins.internal.JavaConfigurationVariantMapping
import org.gradle.api.plugins.internal.NaggingJavaPluginConvention
import org.gradle.api.plugins.jvm.internal.DefaultJvmFeature
import org.gradle.api.plugins.jvm.internal.JvmFeatureInternal
import org.gradle.api.plugins.jvm.internal.JvmPluginServices
import org.gradle.api.publish.PublishingExtension
import org.gradle.api.publish.internal.PublicationInternal
import org.gradle.api.publish.internal.versionmapping.VersionMappingStrategyInternal
import org.gradle.api.publish.ivy.IvyPublication
import org.gradle.api.publish.maven.MavenPublication
import org.gradle.api.publish.plugins.PublishingPlugin
import org.gradle.api.tasks.SourceSet
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.internal.deprecation.DeprecationLogger
import org.gradle.internal.execution.BuildOutputCleanupRegistry
import org.gradle.jvm.component.internal.DefaultJvmSoftwareComponent
import org.gradle.jvm.component.internal.JvmSoftwareComponentInternal
import org.gradle.jvm.toolchain.JavaToolchainService
import org.gradle.jvm.toolchain.internal.DefaultToolchainSpec

import javax.inject.Inject
import java.util.regex.Pattern

class MultiJdkPlugin implements Plugin<Project> {

    protected static final Pattern VALID_FEATURE_NAME = Pattern.compile("[a-zA-Z0-9]+");

    protected static final String JAVA_COMPONENT_NAME = "java";

    protected static final String JAVA_COMPONENT_MAIN_SOURCE_SET_NAME="main";

    protected ObjectFactory objectFactory;

    private JvmPluginServices jvmPluginServices;
    private boolean javaClasspathPackaging;

    @Inject
    public MultiJdkPlugin(ObjectFactory objectFactory, JvmPluginServices jvmPluginServices) {
        this.objectFactory = objectFactory;
        this.javaClasspathPackaging = Boolean.getBoolean("org.gradle.java.compile-classpath-packaging");
        this.jvmPluginServices = jvmPluginServices;
    }

    @Override
    void apply(Project project) {
        applyPlugins(project)
        ProjectInternal projectInternal = (ProjectInternal)project;
        SourceSetContainer sourceSets = getSourceSets(project)
        DefaultJvmSoftwareComponent mainComponent = getMainComponent(projectInternal)
        JavaPluginExtension extension = ((JavaPluginExtension)project.getExtensions().getByType(JavaPluginExtension.class))
        JavaToolchainService service = (JavaToolchainService)project.getExtensions().getByType(JavaToolchainService.class);

        MultiJdkExtension multiJarExtension = createExtension(projectInternal, sourceSets, mainComponent, extension, service)

//        SourceSet mainSourceSet = getMainSourceSet(mainComponent);



//        Set<MultiJdkTargetPlatform> platforms = multiJarExtension.getTargetPlatforms();
//        for(MultiJdkTargetPlatform platform: platforms){
//            String name="jdk"+platform.targetPlatform.get().asInt();
//
//            SourceSet featureSourceSet = createSourceSet(name, sourceSets, mainSourceSet)
//            JvmFeatureInternal newFeature = createFeature(name, projectInternal, featureSourceSet)
//            addFeature(mainComponent,newFeature)
//
//            configurePublishing(project.getPlugins(), project.getExtensions(), newFeature.getSourceSet());
////        Configuration defaultConfiguration = project.getConfigurations().getByName("default");
////        defaultConfiguration.extendsFrom(new Configuration[]{newFeature.getRuntimeElementsConfiguration()})
//
//            DefaultArtifactPublicationSet publicationSet = (DefaultArtifactPublicationSet)project.getExtensions().getByType(DefaultArtifactPublicationSet.class);
//            publicationSet.addCandidate((PublishArtifact)newFeature.getRuntimeElementsConfiguration().getArtifacts().iterator().next());
//
//            BuildOutputCleanupRegistry buildOutputCleanupRegistry = (BuildOutputCleanupRegistry)projectInternal.getServices().get(BuildOutputCleanupRegistry.class);
//
////        JavaPluginExtension javaExtension = ((JavaPluginExtension)project.getExtensions().getByType(JavaPluginExtension.class))
//        }
//

    }


    protected void applyPlugins(Project project){
        project.getPluginManager().apply(JavaLibraryPlugin.class);
    }

    protected MultiJdkExtension createExtension(
            ProjectInternal project,SourceSetContainer sourceSets,DefaultJvmSoftwareComponent mainComponent,
            JavaPluginExtension javaPluginExtension, JavaToolchainService service
    ){
        project.getExtensions().create("multiJar",MultiJdkExtension.class,new Object[]{project, sourceSets, mainComponent, javaPluginExtension, service} )
    }

    protected DefaultJvmSoftwareComponent getMainComponent(ProjectInternal project){
        return (DefaultJvmSoftwareComponent) project.getComponents().findByName(JAVA_COMPONENT_NAME);
//        return project.getObjects().named(DefaultJvmSoftwareComponent.class, JAVA_COMPONENT_NAME)
    }

    protected SourceSetContainer getSourceSets(Project project){
        return ((JavaPluginExtension)project.getExtensions().getByType(JavaPluginExtension.class)).getSourceSets();
    }

    protected SourceSet getMainSourceSet(JvmSoftwareComponentInternal javaComponent){
        return javaComponent.getMainFeature().getSourceSet()
    }

    protected SourceSet createSourceSet(String name,SourceSetContainer sourceSets,  SourceSet mainSourceSet){
        return sourceSets.create(name, (Action<SourceSet>) {
            SourceDirectorySet source = mainSourceSet.getJava()
            it.getJava().source(source)
            SourceDirectorySet resources = mainSourceSet.getResources()
            it.getResources().source(resources)
        });
    }

    protected JvmFeatureInternal createFeature(
            String name, ProjectInternal project, SourceSet sourceSet
    ){
        name = validateFeatureName(name)
        JvmFeatureInternal feature = new DefaultJvmFeature(name, sourceSet, Collections.emptySet(), project, false, false);
        feature.withSourceElements();
        return feature;
    }

    protected void addFeature(DefaultJvmSoftwareComponent target, JvmFeatureInternal feature){
        target.getFeatures().add(feature);
        target.addVariantsFromConfiguration(feature.getApiElementsConfiguration(), new JavaConfigurationVariantMapping("compile", false, feature.getCompileClasspathConfiguration()));
        target.addVariantsFromConfiguration(feature.getRuntimeElementsConfiguration(), new JavaConfigurationVariantMapping("runtime", false, feature.getRuntimeClasspathConfiguration()));
    }

    protected String validateFeatureName(String name){
        if (!VALID_FEATURE_NAME.matcher(name).matches()) {
            throw new InvalidUserDataException("Invalid feature name '" + name + "'. Must match " + VALID_FEATURE_NAME.pattern());
        } else {
            return name;
        }
    }

    protected void configurePublishing(PluginContainer plugins, ExtensionContainer extensions, SourceSet sourceSet){
        plugins.withType(PublishingPlugin.class, (plugin) -> {
            PublishingExtension publishing = (PublishingExtension)extensions.getByType(PublishingExtension.class);
            publishing.getPublications().withType(IvyPublication.class, (publication) -> {
                VersionMappingStrategyInternal strategy = ((PublicationInternal)publication).getVersionMappingStrategy();
                strategy.defaultResolutionConfiguration("java-api", sourceSet.getCompileClasspathConfigurationName());
                strategy.defaultResolutionConfiguration("java-runtime", sourceSet.getRuntimeClasspathConfigurationName());
            });
            publishing.getPublications().withType(MavenPublication.class, (publication) -> {
                VersionMappingStrategyInternal strategy = ((PublicationInternal)publication).getVersionMappingStrategy();
                strategy.defaultResolutionConfiguration("java-api", sourceSet.getCompileClasspathConfigurationName());
                strategy.defaultResolutionConfiguration("java-runtime", sourceSet.getRuntimeClasspathConfigurationName());
            });
        });
    }


}
