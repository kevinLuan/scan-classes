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
import java.util.jar.JarEntry;

/**
 * Jar class entry
 * This class represents an entry in a JAR file and provides methods to check if the entry is a class file,
 * get the class path, and check if the class path matches a specified package.
 */
public class JarClassEntry extends ClassFileUtils {
    private JarEntry jarEntry;
    private String   scanPackage;

    /**
     * Constructs a JarClassEntry object with the specified JAR entry and scan package.
     *
     * @param entry the JAR entry
     * @param scanPackage the package to scan
     */
    public JarClassEntry(JarEntry entry, String scanPackage) {
        this.jarEntry = entry;
        this.scanPackage = scanPackage;
    }

    /**
     * Checks if the JAR entry is a class file.
     *
     * @return true if the JAR entry is a class file, false otherwise
     */
    public boolean isClass() {
        return jarEntry.getName().endsWith(".class");
    }

    /**
     * Gets the class path in the format x.x.x.MyClass.
     *
     * @return the class path
     */
    public String getClassPath() {
        // entry.getName() = com/xxx/xxx/MyClass.class
        String className = jarEntry.getName();
        if (className.startsWith("BOOT-INF/classes/")) {
            className = className.substring("BOOT-INF/classes/".length());
        }
        className = className.replace(File.separator, ".");
        if (className.endsWith(".class")) {
            className = className.substring(0, className.length() - (".class".length()));
        }
        return className;
    }

    /**
     * Checks if the class path matches the scan package.
     *
     * @return true if the class path matches the scan package, false otherwise
     */
    public boolean isMatches() {
        return getClassPath().startsWith(scanPackage);
    }

    /**
     * Gets the scan package.
     *
     * @return the scan package
     */
    @Override
    public String getScanPackage() {
        if (scanPackage == null) {
            return "";
        }
        return scanPackage;
    }
}
