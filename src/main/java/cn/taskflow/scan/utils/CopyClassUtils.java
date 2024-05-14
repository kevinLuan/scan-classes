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
/**
 * 
 */
package cn.taskflow.scan.utils;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import cn.taskflow.scan.core.ScanClass;
import cn.taskflow.scan.core.ClassFile;

/**
 * Copying class file
 * 
 * @CreateTime 2016年5月18日 上午10:08:20
 * @author KEVIN LUAN
 */
public class CopyClassUtils {
    /**
     * 输出路径, 例如：/data/code/api/
     */
    public static String OutputPath;

    public static void main(String[] args) throws IOException {
        CopyClassUtils.copying("com", "*");
    }

    private static String formatName(String name) {
        int index = name.indexOf(".");
        if (index > 0) {
            return name.substring(0, index);
        }
        return name;
    }

    /**
     * 验证匹配Copy Class，如果是内部类，也可以通过验证
     * 
     * @param name Class 文件名称
     * @param matchesModel 匹配模式
     * @return
     */
    private static boolean isMatches(String name, String matchesModel) {
        if (name.length() == 0) {
            return false;
        }
        name = formatName(name);
        matchesModel = formatName(matchesModel);
        matchesModel = matchesModel.replace("*", ".*");
        { // 处理匿名内部类
            if (name.indexOf("$") > 0) {
                name = name.substring(0, name.indexOf("$"));
            }
        }
        return name.matches(matchesModel);
    }

    public static void copying(Class<?>... clazzs) throws IOException {
        for (int i = 0; i < clazzs.length; i++) {
            Class<?> clazz = clazzs[i];
            String pack = clazz.getPackage().getName();

            copying(pack, getClassName(clazz));
        }
    }

    private static String getClassName(Class<?> clazz) {
        String name = clazz.getName();
        name = name.substring(name.lastIndexOf(".") + 1);
        if (name.indexOf("$") != -1) {
            name = name.replace("$", "\\$");
        }
        return name;
    }

    /**
     * Copy the Class file, supporting Jar package or the classpath
     * 
     * @param scanPackage 扫描包路径
     * @param matchesModel 匹配模式，支持*任意字符，例如 ab* or *b* or *bc
     * @throws IOException
     */
    public static void copying(String scanPackage, final String matchesModel) throws IOException {
        scanPackage = ClassFile.formatScanPackage(scanPackage);
        for (String classPath : getClassPaths(scanPackage)) {
            classPath = ClassFile.decode(classPath);
            if (classPath.startsWith("file:") && classPath.indexOf(".jar!") > 0) {
                System.out.println(String.format("Scan jar classpath:[%s]", classPath));
                String jarPath = classPath.substring(5, classPath.indexOf("!"));
                String pack = scanPackage.replace(".", "/");
                fromJarCopy(jarPath, pack, matchesModel);
            } else {
                System.out.println(String.format("Scan java classpath:[%s]", classPath));
                File file = new File(classPath);
                File[] files = file.listFiles(new FileFilter() {

                    @Override
                    public boolean accept(File pathname) {
                        if (pathname.isFile()) {
                            return isMatches(pathname.getName(), matchesModel);
                        }
                        return false;
                    }
                });
                for (int i = 0; i < files.length; i++) {
                    FileInputStream inputStream = new FileInputStream(files[i]);
                    byte[] data = new byte[inputStream.available()];
                    inputStream.read(data);
                    inputStream.close();

                    if (scanPackage.endsWith(".")) {
                        scanPackage = scanPackage.substring(0, scanPackage.length() - 1);
                    }
                    String destPath = getOutPath() + scanPackage.replace(".", "/") + "/" + files[i].getName();
                    File destFile = new File(destPath);
                    File parentFile = destFile.getParentFile();
                    if (!parentFile.exists()) {
                        parentFile.mkdirs();
                    }
                    System.out.println("[copying] " + scanPackage + "." + files[i].getName() + " to "
                                       + destFile.getAbsolutePath());
                    FileOutputStream fos = new FileOutputStream(destFile);
                    fos.write(data);
                    fos.flush();
                    fos.close();
                }
            }
        }
    }

    public static Set<String> getClassPaths(String scanPackage) {
        scanPackage = scanPackage.replace(".", "/");
        Enumeration<URL> resources = null;
        try {// 如果传入“”返回classes路径
            resources = ScanClass.getClassLoader().getResources(scanPackage);
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
     * 默认拷贝到当前项目中的./target/api/ 下面
     * 
     * @return
     */
    private static String getOutPath() {
        if (OutputPath != null) {
            if (OutputPath.endsWith("/")) {
                return OutputPath;
            } else {
                return OutputPath + "/";
            }

        } else {
            String path = CopyClassUtils.class.getResource(".").getFile();
            int index = path.indexOf("/target/") + "/target".length();
            return path.substring(0, index) + "/api/";
        }
    }

    private static void fromJarCopy(String jarPath, String pack, String matchesModel) throws IOException {
        JarFile jar = new JarFile(new File(jarPath));
        Enumeration<JarEntry> enumeration = jar.entries();
        while (enumeration.hasMoreElements()) {
            JarEntry entry = enumeration.nextElement();
            if (entry.getName().startsWith(pack)) {
                String fileName = entry.getName();
                int index = fileName.lastIndexOf("/");
                fileName = fileName.substring(index + 1);
                if (isMatches(fileName, matchesModel)) {
                    copy(entry, jar);
                }
            }
        }
    }

    private static void copy(JarEntry entry, JarFile jarFile) {
        try {
            InputStream inputStream = jarFile.getInputStream(entry);
            byte[] data = new byte[inputStream.available()];
            inputStream.read(data);
            inputStream.close();
            File file = new File(getOutPath() + entry.getName());
            if (file.isDirectory()) {
                if (!file.exists()) {
                    file.mkdirs();
                    System.out.println("create path:" + file.getParentFile() + "|status:" + file.mkdirs());
                }
                return;
            } else {
                File parentFile = file.getParentFile();
                if (!parentFile.exists()) {
                    System.out
                        .println("create path:" + parentFile.getAbsolutePath() + "|status:" + parentFile.mkdirs());
                }
            }
            System.out.println("[copying] " + entry.getName() + " to " + file.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(file);
            fos.write(data);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
