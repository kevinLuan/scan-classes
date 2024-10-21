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

import cn.taskflow.scan.utils.SCUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public abstract class ClassFileUtils {
    /** File path extension for JAR files */
    public static final String JAR_PATH_EXT = ".jar!";

    public abstract String getClassPath();

    public abstract boolean isMatches();

    public abstract String getScanPackage();

    /**
     * Checks if the given file is a class file.
     *
     * @param file File
     * @return Whether it is a class file
     */
    public static boolean isClassFile(File file) {
        return isClass(file.getName());
    }

    /**
     * Checks if the given file name corresponds to a class file.
     *
     * @param fileName File name
     * @return Whether it is a class file
     */
    public static boolean isClass(String fileName) {
        return fileName.endsWith(".class");
    }

    /**
     * Checks if the given file is a JAR file.
     *
     * @param file File
     * @return Whether it is a JAR file
     */
    public static boolean isJarFile(File file) {
        return file.getName().endsWith(".jar");
    }

    /**
     * Checks if the given path corresponds to a JAR file.
     *
     * @param path Path
     * @return Whether it is a JAR file
     */
    public static boolean isJar(String path) {
        int index = path.lastIndexOf(JAR_PATH_EXT);
        return index != -1;
    }

    /**
     * Removes the "file:" prefix from the given path if it exists.
     *
     * @param path Path
     * @return Path without the "file:" prefix
     */
    public static String removeFilePrefix(String path) {
        if (path.startsWith("file:")) {
            return path.substring("file:".length());
        }
        return path;
    }

    /**
     * Parses the JAR file path from the given path.
     * 
     * @param path Path
     * @return JAR file path
     */
    public static String parserJarPath(String path) {
        int index = path.lastIndexOf(JAR_PATH_EXT);
        if (index != -1) {
            path = path.substring(0, index + ".jar".length()); // Extract JAR path
        }
        path = ClassFileUtils.removeFilePrefix(path);
        return path;
    }

    /**
     * Decodes the given string using UTF-8 encoding.
     *
     * @param str String to decode
     * @return Decoded string
     */
    public static String decode(String str) {
        try {
            str = URLDecoder.decode(str, "UTF-8");
        } catch (UnsupportedEncodingException e) {
        }
        return str;
    }

    /**
     * Formats the scan package path by ensuring it ends with a dot.
     *
     * @param packagePath Package path
     * @return Formatted package path
     */
    public static String formatScanPackage(String packagePath) {
        if (SCUtils.isNotBlank(packagePath)) {
            if (packagePath.charAt(packagePath.length() - 1) != '.') {
                packagePath += ".";// Handle scanning: prevent 'com' from scanning out 'comx'
            }
        } else if (packagePath == null) {
            return "";
        }
        return packagePath;
    }
}
