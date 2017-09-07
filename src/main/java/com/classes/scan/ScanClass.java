package com.classes.scan;

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
 * 类扫描
 * 
 * @author KEVIN LUAN
 *
 */
public class ScanClass {

  public static Set<Class<?>> scanPackage() {
    return scanPackage("", null);
  }

  public static Set<Class<?>> scanPackage(String scanPackage) {
    return scanPackage(scanPackage, null);
  }

  public static Set<Class<?>> scanPackage(String scanPackage, ClassFilter classFilter) {
    scanPackage = ClassFile.formatScanPackage(scanPackage);
    final Set<Class<?>> classes = new HashSet<Class<?>>();
    for (String classPath : getClassPaths(scanPackage)) {
      classPath = ClassFile.decode(classPath);
      System.out.println(String.format("Scan java classpath:[%s]", classPath));
      fillClasses(classPath, scanPackage, classFilter, classes);
    }
    // 如果在项目依赖ClassPath中未找到，在去系统定义ClassPath中查找
		// if (classes.isEmpty()) {
		// scanSystemClass(scanPackage, classFilter, classes);
		// }
    return classes;
  }

  public static Set<Class<?>> scanPackage(Set<String> classPath, String scanPackage, ClassFilter classFilter) {
    final Set<Class<?>> classes = new HashSet<Class<?>>();
    for (String path : classPath) {
      path = ClassFile.decode(path);
      System.out.println(String.format("Scan java classpath:[%s]", classPath));
      fillClasses(path, scanPackage, classFilter, classes);
    }
    return classes;
  }

  public static Set<Class<?>> scanSystemClass(String scanPackage, ClassFilter classFilter, Set<Class<?>> classes) {
    for (String classPath : getSystemClassPaths()) {
      classPath = ClassFile.decode(classPath);
      System.out.println(String.format("Scan java classpath:[%s]", classPath));
      fillClasses(classPath, new File(classPath), scanPackage, classFilter, classes);
    }
    return classes;
  }

  private static void fillClasses(String path, String scanPackage, ClassFilter classFilter, Set<Class<?>> classes) {
    if (ClassFile.isJar(path)) {
      processJarFile(new File(ClassFile.parserJarPath(path)), scanPackage, classFilter, classes);
    } else {
      fillClasses(path, new File(path), scanPackage, classFilter, classes);
    }
  }

  private static void fillClasses(String classPath, File file, String scanPackage, ClassFilter classFilter,
      Set<Class<?>> classes) {
    if (file.isDirectory()) {
      processDirectory(classPath, file, scanPackage, classFilter, classes);
    } else if (ClassFile.isClassFile(file)) {
      processClassFile(classPath, file, scanPackage, classFilter, classes);
    } else if (ClassFile.isJarFile(file)) {
      processJarFile(file, scanPackage, classFilter, classes);
    }
  }

  private static void processDirectory(String classPath, File directory, String packageName, ClassFilter classFilter,
      Set<Class<?>> classes) {
    for (File file : directory.listFiles(ClassFileFilter.INSTANCE)) {
      fillClasses(classPath, file, packageName, classFilter, classes);
    }
  }

  private static void processClassFile(String classPath, File file, String packageName, ClassFilter classFilter,
      Set<Class<?>> classes) {
    ClassEntity filePathEntity = new ClassEntity(file, packageName, classPath);
    Class<?> clazz = loadClass(filePathEntity, classFilter);
    if (clazz != null) {
      classes.add(clazz);
    }
  }

  private static void processJarFile(File file, String scanPackage, ClassFilter classFilter, Set<Class<?>> classes) {
    try {
      for (JarEntry entry : Collections.list(new JarFile(file).entries())) {
        JarClass jarClass = new JarClass(entry, scanPackage);
        if (jarClass.isClass()) {
          Class<?> clazz = loadClass(jarClass, classFilter);
          if (clazz != null) {
            classes.add(clazz);
          }
        }
      }
    } catch (Throwable ex) {
      ex.printStackTrace();
    }
  }

  private static Class<?> loadClass(ClassFile classFile, ClassFilter classFilter) {
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
    try {// 如果传入“”返回classes路径
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
    String data= System.getProperty("java.class.path");
    if(data!=null){
        String[] classPaths = data.split(System.getProperty("path.separator"));
        return classPaths;
    }else{
        return new String[0];
    }
  }

  public static ClassLoader getClassLoader() {
    ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    if (classLoader == null) {
      classLoader = ScanClass.class.getClassLoader();
    }
    return classLoader;
  }
}
