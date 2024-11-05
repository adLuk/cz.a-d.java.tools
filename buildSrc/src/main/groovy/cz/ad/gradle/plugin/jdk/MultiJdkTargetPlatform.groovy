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

class MultiJdkTargetPlatform {

    protected JavaLanguageVersion targetPlatform

    protected boolean withSource;

    protected boolean withJavaDoc;

    JavaLanguageVersion getTargetPlatform() {
        return targetPlatform
    }

    void setTargetPlatform(JavaLanguageVersion targetPlatform) {
        this.targetPlatform = targetPlatform
    }

    boolean getWithSource() {
        return withSource
    }

    void setWithSource(boolean withSource) {
        this.withSource = withSource
    }

    boolean getWithJavaDoc() {
        return withJavaDoc
    }

    void setWithJavaDoc(boolean withJavaDoc) {
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
