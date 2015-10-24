package com.classes.utils;

import java.util.concurrent.Semaphore;

/**
 * 信号量
 * 
 * @author SHOUSHEN LUAN
 *
 */
public class SemaphoreDemo {
  public static void main(String[] args) {
    // 信号量定义
    Semaphore semaphore = new Semaphore(1000);
    System.out.println(semaphore.getQueueLength());
    if (semaphore.getQueueLength() > 0) {
      return;
    }
    try {
      System.out.println("---");
      semaphore.acquire();
      System.out.println("--->>>");
      // TODO 业务处理
    } catch (InterruptedException ex) {
      ex.printStackTrace();
    } finally {
      semaphore.release();
    }
  }
}
