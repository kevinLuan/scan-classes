package com.classes.utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;
/**
 * DES 对称加密
 * 明文按64位进行分组，秘钥长度64位，但事实上只有56位参与DES运算
 * @author kevin
 *
 */
public class SecriptDES {
  /**
   * 生成DES秘钥
   * 
   * @return
   * @throws Exception
   */
  public static String getKeyDES() throws Exception {
    KeyGenerator generator = KeyGenerator.getInstance("DES");
    generator.init(56);// 设置DES算法的秘钥为56位
    SecretKey secretKey = generator.generateKey();
    String base64Str = Base64.encode(secretKey.getEncoded());
    return base64Str;
  }

  public static SecretKey loadKeyDES(String base64STR) {
    byte[] bytes = Base64.decode(base64STR);
    SecretKey secretKey = new SecretKeySpec(bytes, "DES");
    return secretKey;
  }

  public static byte[] encryptDES(byte[] bytes, SecretKey key) throws Exception, NoSuchPaddingException {
    Cipher cipher = Cipher.getInstance("DES");
    cipher.init(Cipher.ENCRYPT_MODE, key);
    return cipher.doFinal(bytes);
  }

  public static byte[] decryptDES(byte[] bytes, SecretKey key) throws Exception {
    Cipher cipher = Cipher.getInstance("DES");
    cipher.init(Cipher.DECRYPT_MODE, key);
    return cipher.doFinal(bytes);
  }

  public static void main(String[] args) throws Exception {
    String key = getKeyDES();
    System.out.println(key);
    SecretKey secretKey = loadKeyDES(key);
    String data = "中国人民";
    byte[] result = encryptDES(data.getBytes(), secretKey);
    result = decryptDES(result, secretKey);
    System.out.println(new String(result, "UTF-8"));


  }
}
