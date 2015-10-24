package com.classes.scan;

import java.io.File;

public class ClassEntity extends ClassFile {
  private File file;
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
