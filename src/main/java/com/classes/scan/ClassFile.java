package com.classes.scan;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

public abstract class ClassFile {
  /** Jar中的文件路径jar的扩展名形式 */
  public static final String JAR_PATH_EXT = ".jar!";

  public abstract String getClassPath();

  public abstract boolean isMatches();

  public abstract String getScanPackage();

  /**
   * @param file 文件
   * @return 是否为类文件
   */
  public static boolean isClassFile(File file) {
    return isClass(file.getName());
  }

  /**
   * @param fileName 文件名
   * @return 是否为类文件
   */
  public static boolean isClass(String fileName) {
    return fileName.endsWith(".class");
  }

  public static boolean isJarFile(File file) {
    return file.getName().endsWith(".jar");
  }

  public static boolean isJar(String path) {
    int index = path.lastIndexOf(JAR_PATH_EXT);
    return index != -1;
  }

  public static String removeFilePrefix(String path) {
    if (path.startsWith("file:")) {
      return path.substring("file:".length());
    }
    return path;
  }

  /**
   * Class文件路径或者所在目录Jar包路径
   * 
   * @param path
   * @return
   */
  public static String parserJarPath(String path) {
    int index = path.lastIndexOf(JAR_PATH_EXT);
    if (index != -1) {
      path = path.substring(0, index + ".jar".length()); // 截取jar路径
    }
    path = ClassFile.removeFilePrefix(path);
    return path;
  }

  public static String decode(String str) {
    try {
      str = URLDecoder.decode(str, "UTF-8");
    } catch (UnsupportedEncodingException e) {
    }
    return str;
  }

  public static String formatScanPackage(String packagePath) {
    if (StringUtils.isNotBlank(packagePath)) {
      if (packagePath.charAt(packagePath.length() - 1) != '.') {
        packagePath += ".";// 处理扫描：com 把 comx 给扫描出来的现象
      }
    } else if (packagePath == null) {
      return "";
    }
    return packagePath;
  }
}
