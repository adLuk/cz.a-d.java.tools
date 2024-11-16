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

package cz.ad.gradle.plugin.jdk.spec;

import cz.ad.gradle.plugin.jdk.MultiJdkTargetPlatform;
import org.gradle.api.Action;

/**
 * Interface used to support multiple targets version definition in extension to be loaded from build gradle file.
 */
public interface JdkTargetVersionSpec {

    /**
     * Allow configuration of single target from build gradle file as is commonly defined for other plugins.
     * @param action configure action with data from build file.
     */
    void target(Action<? super MultiJdkTargetPlatform> action);
}
