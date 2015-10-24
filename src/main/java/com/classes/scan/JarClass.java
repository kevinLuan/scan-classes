package com.classes.scan;

import java.io.File;
import java.util.jar.JarEntry;

public class JarClass extends ClassFile {
  private JarEntry jarEntry;
  private String scanPackage;

  public JarClass(JarEntry entry, String scanPackage) {
    this.jarEntry = entry;
    this.scanPackage = scanPackage;
  }

  public boolean isClass() {
    return jarEntry.getName().endsWith(".class");
  }

  /**
   * 获取Class类名称 x.x.x.MyClass
   * 
   * @param className
   * @return
   */
  public String getClassPath() {
    // entry.getName() = com/xxx/xxx/MyClass.class
    String className = jarEntry.getName();
    return className.replace(File.separator, ".").replace(".class", "");
  }

  public boolean isMatches() {
    return getClassPath().startsWith(scanPackage);
  }

  @Override
  public String getScanPackage() {
    if (scanPackage == null) {
      return "";
    }
    return scanPackage;
  }
}
