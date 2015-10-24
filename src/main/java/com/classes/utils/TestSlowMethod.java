package com.classes.utils;

public class TestSlowMethod {
  private static int num=0;
  public void test() throws InterruptedException {
    System.out.println("start....");
    Thread.sleep(1000);
    System.out.println("end ....");
    test1(TestSlowMethod.class);
  }

  public static void main(String[] args) throws InterruptedException {
    for (int i = 0; i < 1000000; i++) {
      new TestSlowMethod().test();
    }
  }
  
  public void test1(Class<?>clazz){
  }
}
