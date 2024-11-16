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

import org.gradle.jvm.toolchain.JavaLanguageVersion

/**
 * Bean with ability to define target JDK platform to be produced by compile process.
 */
class MultiJdkTargetPlatform {
    /**
     * Version of JDK witch should be used for output of compile process
     */
    protected JavaLanguageVersion targetPlatform
    /**
     * Flag allowing to add java source jar for specific target. (Currently not supported)
     */
    protected Boolean withSource;
    /**
     * Flag allowing to add java doc jar for specific target. (Currently not supported)
     */
    protected Boolean withJavaDoc;

    JavaLanguageVersion getTargetPlatform() {
        return targetPlatform
    }

    void setTargetPlatform(JavaLanguageVersion targetPlatform) {
        this.targetPlatform = targetPlatform
    }

    Boolean getWithSource() {
        return withSource
    }

    void setWithSource(Boolean withSource) {
        this.withSource = withSource
    }

    Boolean getWithJavaDoc() {
        return withJavaDoc
    }

    void setWithJavaDoc(Boolean withJavaDoc) {
        this.withJavaDoc = withJavaDoc
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (o == null || getClass() != o.class) return false

        MultiJdkTargetPlatform that = (MultiJdkTargetPlatform) o

        if (withJavaDoc != that.withJavaDoc) return false
        if (withSource != that.withSource) return false
        if (targetPlatform != that.targetPlatform) return false

        return true
    }

    int hashCode() {
        int result
        result = (targetPlatform != null ? targetPlatform.hashCode() : 0)
        result = 31 * result + (withSource ? 1 : 0)
        result = 31 * result + (withJavaDoc ? 1 : 0)
        return result
    }


    @Override
    public String toString() {
        return "MultiJdkTargetPlatform{" +
                "targetPlatform=" + targetPlatform +
                ", withSource=" + withSource +
                ", withJavaDoc=" + withJavaDoc +
                '}';
    }
}
