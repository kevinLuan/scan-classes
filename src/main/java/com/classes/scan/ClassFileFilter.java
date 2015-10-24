package com.classes.scan;

import java.io.File;
import java.io.FileFilter;

public class ClassFileFilter implements FileFilter {
  public static final ClassFileFilter INSTANCE = new ClassFileFilter();

  @Override
  public boolean accept(File pathname) {
    return ClassFile.isClass(pathname.getName()) || pathname.isDirectory() || ClassFile.isJarFile(pathname);
  }
}
