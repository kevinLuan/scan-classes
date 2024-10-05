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

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.Collections;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Class Scanner
 *
 * @author KEVIN LUAN
 */
public class ClassScanner {
    private static final Logger log = LoggerFactory.getLogger(ClassScanner.class);

    public static Set<Class<?>> scanPackage() {
        return scanPackage("", null);
    }

    public static Set<Class<?>> scanPackage(String scanPackage) {
        return scanPackage(scanPackage, null);
    }

    public static Set<Class<?>> scanPackage(String scanPackage, ClassFilter classFilter) {
        scanPackage = ClassFileUtils.formatScanPackage(scanPackage);
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        for (String classPath : getClassPaths(scanPackage)) {
            classPath = ClassFileUtils.decode(classPath);
            log.info("Scan java classpath:[{}]", classPath);
            fillClasses(classPath, scanPackage, classFilter, classes);
        }
        // If not found in project dependencies ClassPath, search in system-defined ClassPath
        // if (classes.isEmpty()) {
        // scanSystemClass(scanPackage, classFilter, classes);
        // }
        return classes;
    }

    public static Set<Class<?>> scanPackage(Set<String> classPath, String scanPackage, ClassFilter classFilter) {
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        for (String path : classPath) {
            path = ClassFileUtils.decode(path);
            log.info("Scan java classpath:[{}]", classPath);
            fillClasses(path, scanPackage, classFilter, classes);
        }
        return classes;
    }

    public static Set<Class<?>> scanSystemClass(String scanPackage, ClassFilter classFilter, Set<Class<?>> classes) {
        for (String classPath : getSystemClassPaths()) {
            classPath = ClassFileUtils.decode(classPath);
            log.info("Scan java classpath:[{}]", classPath);
            fillClasses(classPath, new File(classPath), scanPackage, classFilter, classes);
        }
        return classes;
    }

    public static void fillClasses(String path, String scanPackage, ClassFilter classFilter, Set<Class<?>> classes) {
        if (ClassFileUtils.isJar(path)) {
            processJarFile(new File(ClassFileUtils.parserJarPath(path)), scanPackage, classFilter, classes);
        } else {
            fillClasses(path, new File(path), scanPackage, classFilter, classes);
        }
    }

    private static void fillClasses(String classPath, File file, String scanPackage, ClassFilter classFilter,
                                    Set<Class<?>> classes) {
        if (file.isDirectory()) {
            processDirectory(classPath, file, scanPackage, classFilter, classes);
        } else if (ClassFileUtils.isClassFile(file)) {
            processClassFile(classPath, file, scanPackage, classFilter, classes);
        } else if (ClassFileUtils.isJarFile(file)) {
            processJarFile(file, scanPackage, classFilter, classes);
        }
    }

    private static void processDirectory(String classPath, File directory, String packageName, ClassFilter classFilter,
                                         Set<Class<?>> classes) {
        for (File file : directory.listFiles(ClassAndJarFileFilter.INSTANCE)) {
            fillClasses(classPath, file, packageName, classFilter, classes);
        }
    }

    private static void processClassFile(String classPath, File file, String packageName, ClassFilter classFilter,
                                         Set<Class<?>> classes) {
        ClassInfo filePathEntity = new ClassInfo(file, packageName, classPath);
        Class<?> clazz = loadClass(filePathEntity, classFilter);
        if (clazz != null) {
            classes.add(clazz);
        }
    }

    private static void processJarFile(File file, String scanPackage, ClassFilter classFilter, Set<Class<?>> classes) {
        try {
            for (JarEntry entry : Collections.list(new JarFile(file).entries())) {
                JarClassEntry jarClassEntry = new JarClassEntry(entry, scanPackage);
                if (jarClassEntry.isClass()) {
                    Class<?> clazz = loadClass(jarClassEntry, classFilter);
                    if (clazz != null) {
                        classes.add(clazz);
                    }
                }
            }
        } catch (Throwable ex) {
            ex.printStackTrace();
        }
    }

    private static Class<?> loadClass(ClassFileUtils classFile, ClassFilter classFilter) {
        if (classFile.isMatches()) {
            try {
                final Class<?> clazz = Class.forName(classFile.getClassPath(), false, getClassLoader());
                if (classFilter == null) {
                    return clazz;
                } else if (classFilter.accept(clazz)) {
                    return clazz;
                }
            } catch (Throwable ex) {
                // ex.printStackTrace();
            }
        }
        return null;
    }

    public static Set<String> getClassPaths(String scanPackage) {
        scanPackage = scanPackage.replace(".", "/");
        Enumeration<URL> resources = null;
        try {// If "" is passed, return classes path
            resources = getClassLoader().getResources(scanPackage);
        } catch (IOException e) {
            e.printStackTrace();
        }
        Set<String> paths = new HashSet<String>();
        while (resources.hasMoreElements()) {
            paths.add(resources.nextElement().getPath());
        }
        return paths;
    }

    public static Set<String> getAppClassPath() {
        return getClassPaths("");
    }

    public static String[] getSystemClassPaths() {
        String data = System.getProperty("java.class.path");
        if (data != null) {
            String[] classPaths = data.split(System.getProperty("path.separator"));
            return classPaths;
        } else {
            return new String[0];
        }
    }

    public static ClassLoader getClassLoader() {
        ClassLoader classLoader;// = Thread.currentThread().getContextClassLoader();
        // if (classLoader == null) {
        classLoader = ClassScanner.class.getClassLoader();
        // }
        return classLoader;
    }
}