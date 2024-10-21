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
import java.io.FileFilter;

/**
 * Class file filter
 * This filter is used to accept files that are either class files, directories, or jar files.
 */
public class ClassAndJarFileFilter implements FileFilter {
    public static final ClassAndJarFileFilter INSTANCE = new ClassAndJarFileFilter();

    /**
     * Accepts a file if it is a class file, a directory, or a jar file.
     *
     * @param pathname the file to be tested
     * @return true if the file is a class file, a directory, or a jar file; false otherwise
     */
    @Override
    public boolean accept(File pathname) {
        return ClassFileUtils.isClass(pathname.getName()) || pathname.isDirectory()
               || ClassFileUtils.isJarFile(pathname);
    }
}
