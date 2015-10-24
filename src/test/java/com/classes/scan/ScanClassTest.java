package com.classes.scan;

import java.util.Set;

import junit.framework.Assert;

import org.junit.Test;

public class ScanClassTest {

  @Test
  public void test() {
    Set<Class<?>> packageSet = ScanClass.scanPackage();
    // for (Class<?> clazz : packageSet) {
    // System.out.println(clazz);
    // }
    Assert.assertEquals(packageSet.size(), 62);
  }

  @Test
  public void testScan() {
    Set<Class<?>> packageSet = ScanClass.scanPackage("com.classes.scan");
    // for (Class<?> clazz : packageSet) {
    // System.out.println(clazz);
    // }
    Assert.assertEquals(packageSet.size(), 10);
  }

  @Test
  public void testScan_com() {
    Set<Class<?>> packageSet = ScanClass.scanPackage("com");
    // for (Class<?> clazz : packageSet) {
    // System.out.println(clazz);
    // }
    Assert.assertEquals(packageSet.size(), 2060);
  }
}
