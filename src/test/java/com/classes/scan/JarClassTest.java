package com.classes.scan;

import java.util.jar.JarEntry;

import junit.framework.Assert;

import org.junit.Test;

public class JarClassTest {
  @Test
  public void test() {
    JarEntry entry = new JarEntry("com/xxx/xxx/MyClass.class");
    JarClass jarClass = new JarClass(entry, "com");
    Assert.assertEquals(jarClass.getClassPath(), "com.xxx.xxx.MyClass");
  }

  @Test
  public void matches() {
    JarEntry entry = new JarEntry("com/xxx/xxx/MyClass.class");
    JarClass jarClass = new JarClass(entry, "com");
    Assert.assertTrue(jarClass.isMatches());
  }
}
