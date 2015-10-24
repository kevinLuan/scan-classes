package com.classes.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class AES {
  // AES 即高级加密标准
  public static String genKeyAES() throws Exception {
    KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
    keyGenerator.init(128);// 128,192,256
    // 但由于美国对于加密软件出口的控制，如果使用192，256位的秘钥，则需要另外下载无政策和司法限制的文件，否则程序运行时出现异常
    SecretKey secretKey = keyGenerator.generateKey();
    return Base64.encode(secretKey.getEncoded());
  }

  public static SecretKey loadKeyAES(String base64) {
    byte[] bytes = Base64.decode(base64);
    SecretKey key = new SecretKeySpec(bytes, "AES");
    return key;

  }

  public static byte[] encryptAES(byte[] source, SecretKey key) throws Exception {
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    return cipher.doFinal(source);
  }

  public static byte[] decryptAES(byte[] source, SecretKey key) throws Exception {
    Cipher cipher = Cipher.getInstance("AES");
    cipher.init(Cipher.DECRYPT_MODE, key);
    return cipher.doFinal(source);
  }

  public static void main(String[] args) throws Exception{
    String key = genKeyAES();
    System.out.println(key);
    SecretKey secretKey = loadKeyAES(key);
    String data = "中国人民";
    byte[] result = encryptAES(data.getBytes(), secretKey);
    result = decryptAES(result, secretKey);
    System.out.println(new String(result, "UTF-8"));

  }
}
