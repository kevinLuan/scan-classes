/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package cn.taskflow.scan.utils;

import java.io.File;

public class JavaEntity {
    private Class<?> clazz;

    private JavaEntity(Class<?> clazz) {
        this.clazz = clazz;
    }

    public String getClassPath() {
        return clazz.getProtectionDomain().getCodeSource().getLocation().getPath();
    }

    public static JavaEntity getInstance(Class<?> clazz) {
        return new JavaEntity(clazz);
    }

    public String getJavaCodePath() {
        String projectPath = getProjectPath();
        String pack = getPackPath();
        String javaFileName = clazz.getSimpleName() + ".java";
        String javaCodePath = projectPath + "src/main/java/" + pack + "/" + javaFileName;
        return javaCodePath;
    }

    public String getPackPath() {
        return clazz.getPackage().getName().replace(".", "/");
    }

    public String getProjectPath() {
        String path = getClassPath();
        if (path != null) {
            int end = path.length() - "target/classes/".length();
            return path.substring(0, end);
        } else {
            return null;
        }
    }

    public File getJavaFile() {
        return new File(getJavaCodePath());
    }

    public String getJavaFileName() {
        return getJavaFile().getName();
    }

    public String getTargetPath() {
        return getProjectPath() + "target/";
    }
}
