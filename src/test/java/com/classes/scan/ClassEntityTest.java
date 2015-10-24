package com.classes.scan;

import java.io.File;
import org.junit.Test;

import junit.framework.Assert;

public class ClassEntityTest {
  @Test
  public void test() {
    File file = new File("/r1/r2/r3/com/xx/MyClass.class");
    String scanPackage = "com.xx";
    String searchClassPath = "/r1/r2/r3/com/xx";
    ClassEntity classEntity = new ClassEntity(file, scanPackage, searchClassPath);
    Assert.assertEquals(classEntity.getClassPath(), "com.xx.MyClass");
  }

  @Test
  public void test_1() {
    File file =
        new File(
            "/Users/kevin/JAVA_HOME/vdianApi/target/test-classes/com/vdian/api/controller/order/OrderDetailControllerTest.class");
    String scanPackage = "com.vdian.api.controller.cps.";
    String searchClassPath = "/Users/kevin/JAVA_HOME/vdianApi/target/test-classes/";
    ClassEntity entity = new ClassEntity(file, scanPackage, searchClassPath);
    Assert.assertEquals(entity.getClassPath(), "com.vdian.api.controller.order.OrderDetailControllerTest");
  }

  @Test
  public void matches() {
    File file = new File("/r1/r2/r3/com/xx/MyClass.class");
    String scanPackage = "com.xx";
    String searchClassPath = "/r1/r2/r3/com/xx";
    ClassEntity classEntity = new ClassEntity(file, scanPackage, searchClassPath);
    Assert.assertEquals(classEntity.getClassPath(), "com.xx.MyClass");
    Assert.assertTrue(classEntity.isMatches());
  }
}
