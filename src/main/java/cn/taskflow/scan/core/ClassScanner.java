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
 * This class is responsible for scanning and loading classes from the specified package.
 * It supports scanning from directories and JAR files.
 * 
 * Usage:
 * - To scan a package without any filter: ClassScanner.scanPackage("com.example");
 * - To scan a package with a filter: ClassScanner.scanPackage("com.example", classFilter);
 * - To scan system class paths: ClassScanner.scanSystemClass("com.example", classFilter, classes);
 * 
 * The scanning process involves:
 * - Decoding the class path
 * - Logging the class path being scanned
 * - Filling the set of classes by processing directories, class files, and JAR files
 * 
 * The class also provides utility methods to get class paths and system class paths.
 * 
 * Note: The class uses SLF4J for logging.
 * 
 * @see ClassFileUtils
 * @see ClassFilter
 * @see ClassAndJarFileFilter
 * @see ClassInfo
 * @see JarClassEntry
 * 
 * Author: KEVIN LUAN
 */
public class ClassScanner {
    private static final Logger log = LoggerFactory.getLogger(ClassScanner.class);

    /**
     * Scans the default package and returns a set of classes.
     * 
     * @return a set of classes found in the default package
     */
    public static Set<Class<?>> scanPackage() {
        return scanPackage("", null);
    }

    /**
     * Scans the specified package and returns a set of classes.
     * 
     * @param scanPackage the package to scan
     * @return a set of classes found in the specified package
     */
    public static Set<Class<?>> scanPackage(String scanPackage) {
        return scanPackage(scanPackage, null);
    }

    /**
     * Scans the specified package with the given class filter and returns a set of classes.
     * 
     * @param scanPackage the package to scan
     * @param classFilter the filter to apply while scanning classes
     * @return a set of classes found in the specified package that match the filter
     */
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

    /**
     * Scans the specified class paths with the given package and class filter, and returns a set of classes.
     * 
     * @param classPath the set of class paths to scan
     * @param scanPackage the package to scan
     * @param classFilter the filter to apply while scanning classes
     * @return a set of classes found in the specified class paths that match the filter
     */
    public static Set<Class<?>> scanPackage(Set<String> classPath, String scanPackage, ClassFilter classFilter) {
        final Set<Class<?>> classes = new HashSet<Class<?>>();
        for (String path : classPath) {
            path = ClassFileUtils.decode(path);
            log.info("Scan java classpath:[{}]", classPath);
            fillClasses(path, scanPackage, classFilter, classes);
        }
        return classes;
    }

    /**
     * Scans the system class paths with the given package and class filter, and returns a set of classes.
     * 
     * @param scanPackage the package to scan
     * @param classFilter the filter to apply while scanning classes
     * @param classes the set of classes to fill
     * @return a set of classes found in the system class paths that match the filter
     */
    public static Set<Class<?>> scanSystemClass(String scanPackage, ClassFilter classFilter, Set<Class<?>> classes) {
        for (String classPath : getSystemClassPaths()) {
            classPath = ClassFileUtils.decode(classPath);
            log.info("Scan java classpath:[{}]", classPath);
            fillClasses(classPath, new File(classPath), scanPackage, classFilter, classes);
        }
        return classes;
    }

    /**
     * Fills the set of classes by processing the given path with the specified package and class filter.
     * 
     * @param path the path to process
     * @param scanPackage the package to scan
     * @param classFilter the filter to apply while scanning classes
     * @param classes the set of classes to fill
     */
    public static void fillClasses(String path, String scanPackage, ClassFilter classFilter, Set<Class<?>> classes) {
        if (ClassFileUtils.isJar(path)) {
            processJarFile(new File(ClassFileUtils.parserJarPath(path)), scanPackage, classFilter, classes);
        } else {
            fillClasses(path, new File(path), scanPackage, classFilter, classes);
        }
    }

    /**
     * Fills the set of classes by processing the given file with the specified package and class filter.
     * 
     * @param classPath the class path
     * @param file the file to process
     * @param scanPackage the package to scan
     * @param classFilter the filter to apply while scanning classes
     * @param classes the set of classes to fill
     */
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

    /**
     * Processes the given directory and fills the set of classes by scanning the specified package and applying the class filter.
     * 
     * @param classPath the class path
     * @param directory the directory to process
     * @param packageName the package to scan
     * @param classFilter the filter to apply while scanning classes
     * @param classes the set of classes to fill
     */
    private static void processDirectory(String classPath, File directory, String packageName, ClassFilter classFilter,
                                         Set<Class<?>> classes) {
        for (File file : directory.listFiles(ClassAndJarFileFilter.INSTANCE)) {
            fillClasses(classPath, file, packageName, classFilter, classes);
        }
    }

    /**
     * Processes the given class file and fills the set of classes by scanning the specified package and applying the class filter.
     * 
     * @param classPath the class path
     * @param file the class file to process
     * @param packageName the package to scan
     * @param classFilter the filter to apply while scanning classes
     * @param classes the set of classes to fill
     */
    private static void processClassFile(String classPath, File file, String packageName, ClassFilter classFilter,
                                         Set<Class<?>> classes) {
        ClassInfo filePathEntity = new ClassInfo(file, packageName, classPath);
        Class<?> clazz = loadClass(filePathEntity, classFilter);
        if (clazz != null) {
            classes.add(clazz);
        }
    }

    /**
     * Processes the given JAR file and fills the set of classes by scanning the specified package and applying the class filter.
     * 
     * @param file the JAR file to process
     * @param scanPackage the package to scan
     * @param classFilter the filter to apply while scanning classes
     * @param classes the set of classes to fill
     */
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

    /**
     * Loads the class from the given class file and applies the class filter.
     * 
     * @param classFile the class file to load
     * @param classFilter the filter to apply while loading the class
     * @return the loaded class if it matches the filter, null otherwise
     */
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

    /**
     * Returns the set of class paths for the specified package.
     * 
     * @param scanPackage the package to get class paths for
     * @return the set of class paths for the specified package
     */
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

    /**
     * Returns the set of class paths for the application.
     * 
     * @return the set of class paths for the application
     */
    public static Set<String> getAppClassPath() {
        return getClassPaths("");
    }

    /**
     * Returns the array of system class paths.
     * 
     * @return the array of system class paths
     */
    public static String[] getSystemClassPaths() {
        String data = System.getProperty("java.class.path");
        if (data != null) {
            String[] classPaths = data.split(System.getProperty("path.separator"));
            return classPaths;
        } else {
            return new String[0];
        }
    }

    /**
     * Returns the class loader to use for loading classes.
     * 
     * @return the class loader to use for loading classes
     */
    public static ClassLoader getClassLoader() {
        ClassLoader classLoader;// = Thread.currentThread().getContextClassLoader();
        // if (classLoader == null) {
        classLoader = ClassScanner.class.getClassLoader();
        // }
        return classLoader;
    }
}