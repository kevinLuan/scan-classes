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
package cn.taskflow.scan.core;

import java.io.File;

public class ClassEntity extends ClassFile {
    private File   file;
    /**
     * 扫描包
     */
    private String scanPackage;
    /**
     * Class 文件查找路径
     */
    private String classPath;

    public ClassEntity(File file, String scanPackage, String searchClassPath) {
        if (false == searchClassPath.endsWith(File.separator)) {
            searchClassPath += File.separator;// 扫描CLASS 目录 项目Class目录+PackageName
        }
        this.file = file;
        this.scanPackage = scanPackage;
        this.classPath = searchClassPath;

    }

    /**
     * 返回com.xx.MyClass
     * 
     * @return
     */
    public String getClassPath() {
        int index = classPath.indexOf(scanPackage.replace(".", File.separator));
        int last = ".class".length();
        String path = file.getAbsolutePath();
        if (StringUtils.isBlank(scanPackage)) {
            index = classPath.length();
        }
        if (index == -1) {
            index = classPath.length();
        }
        String class_path = path.substring(index, path.length() - last);
        class_path = class_path.replace(File.separator, ".");
        return class_path;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("ClassEntity{\n");
        builder.append("file:" + file.getAbsolutePath() + "\n");
        builder.append("scanPackage:" + scanPackage + "\n");
        builder.append("classPath:" + classPath + "\n");
        builder.append("}");
        return builder.toString();
    }

    public boolean isMatches() {
        String classPath = getClassPath();
        if (classPath != null) {
            return getClassPath().startsWith(scanPackage);
        }
        return false;
    }

    @Override
    public String getScanPackage() {
        if (scanPackage == null) {
            return "";
        }
        return scanPackage;
    }
}
