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

import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.JavaLibraryPlugin
import org.gradle.api.plugins.JavaPluginExtension
import org.gradle.api.tasks.SourceSetContainer
import org.gradle.jvm.toolchain.JavaToolchainService

/**
 * Multiple JDK release version plugin for allowing simple configuration based release of java code compiled by multiple
 * different version of java compiler and producing selected target versions of byte code.
 */
class MultiJdkPlugin implements Plugin<Project> {
    /**
     * Apply plugin to project by ensuring java-lib plugin was applied and registering extension where all logic of
     * plugin is implemented.
     * @param project instance where plugin is applied.
     */
    @Override
    void apply(Project project) {
        applyPlugins(project)
        SourceSetContainer sourceSets = getSourceSets(project)
        JavaPluginExtension extension = ((JavaPluginExtension) project.getExtensions().getByType(JavaPluginExtension.class))
        JavaToolchainService service = (JavaToolchainService) project.getExtensions().getByType(JavaToolchainService.class);
        createExtension(project, sourceSets, extension, service)
    }

    /**
     * Apply java library plugin to project.
     * @param project project instance where plugin is applied.
     */
    protected void applyPlugins(Project project) {
        project.getPluginManager().apply(JavaLibraryPlugin.class);
    }

    /**
     * Create new instance of multi jdk plugin extension and register it into project.
     * @param project project project instance where plugin is applied.
     * @param sourceSets used to create jdk specific sourceSets
     * @param javaPluginExtension used to register new JDK version specific feature.
     * @param service used to obtain/ install requested version of JDK toolchain
     * @return
     */
    protected void createExtension(
            Project project, SourceSetContainer sourceSets,
            JavaPluginExtension javaPluginExtension, JavaToolchainService service
    ) {
        project.getExtensions()
                .create("multiJar", MultiJdkExtension.class,
                        new Object[]{project, sourceSets, javaPluginExtension, service})
    }

    /**
     * Provide sourceSet container defined in project.
     * @param project project project project instance where plugin is applied and sourSet container needs to be located
     * @return container with sourceSets defined in project java plugin extension.
     */
    protected SourceSetContainer getSourceSets(Project project) {
        return ((JavaPluginExtension) project.getExtensions().getByType(JavaPluginExtension.class)).getSourceSets();
    }

}
