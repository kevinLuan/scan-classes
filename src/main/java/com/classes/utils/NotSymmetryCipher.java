package com.classes.utils;

import java.security.InvalidKeyException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

/**
 * 非对称加密
 * 
 * @author kevin
 *
 */
public class NotSymmetryCipher {
  public static KeyPair getKeyPair() throws NoSuchAlgorithmException {
    KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance("RSA");
    /**
     * 如果使用1024初始化keyPairGenerator，RSA加密后的密文长度为1024位，即128字节， 此时明文的最大长度不能超过117字节，
     * 超过117个字节需要使用2028的KeySize来初始化keyPairGenerator
     * ，超过245个字节则需要使用更高位数的keysize，RSA的keysize位数越高，其产生秘钥对加密，解密的速度越慢，这是基于大素数非对称加密算法缺陷
     */
    keyPairGenerator.initialize(512);// (512/8-1)=可以加解密的字符串长度
    KeyPair keyPair = keyPairGenerator.generateKeyPair();
    return keyPair;
  }

  public static String getPublicKey(KeyPair keyPair) {
    PublicKey publicKey = keyPair.getPublic();
    byte[] bytes = publicKey.getEncoded();
    return byte2base64(bytes);
  }

  public static String getPrivateKey(KeyPair keyPair) {
    PrivateKey privateKey = keyPair.getPrivate();
    byte[] bytes = privateKey.getEncoded();
    return byte2base64(bytes);
  }

  public static String byte2base64(byte[] bytes) {
    return Base64.encode(bytes);
  }

  public static byte[] base42byte(String base64) {
    return Base64.decode(base64);
  }

  public static PublicKey string2PublicKey(String pub) throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte[] encodedKey = base42byte(pub);
    X509EncodedKeySpec keySpec = new X509EncodedKeySpec(encodedKey);
    KeyFactory keyFactory = KeyFactory.getInstance("RSA");
    PublicKey publicKey = keyFactory.generatePublic(keySpec);
    return publicKey;
  }

  public static PrivateKey string2PrivateKey(String priv) throws NoSuchAlgorithmException, InvalidKeySpecException {
    byte encodedKey[] = base42byte(priv);
    PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(encodedKey);
    KeyFactory factory = KeyFactory.getInstance("RSA");
    PrivateKey privateKey = factory.generatePrivate(keySpec);
    return privateKey;
  }


  public static byte[] publicEncrypt(byte[] content, PublicKey publicKey) throws NoSuchAlgorithmException,
      NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, publicKey);
    return cipher.doFinal(content);
  }

  public static byte[] privateDecrypt(byte[] content, PrivateKey privateKey) throws NoSuchAlgorithmException,
      NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, privateKey);
    byte[] bytes = cipher.doFinal(content);
    return bytes;
  }

  public static void main(String[] args) throws NoSuchAlgorithmException, InvalidKeySpecException, InvalidKeyException,
      NoSuchPaddingException, IllegalBlockSizeException, BadPaddingException {
    KeyPair keyPair = getKeyPair();
    String pub = getPublicKey(keyPair);
    String priv = getPrivateKey(keyPair);
    PrivateKey privateKey = string2PrivateKey(priv);
    PublicKey publicKey = string2PublicKey(pub);

    String str = "12345678901234567890123456789012345678901234567890中";
    System.out.println(str.getBytes().length);
    byte[] result = publicEncrypt(str.getBytes(), publicKey);
    System.out.println(byte2base64(result));

    result = privateDecrypt(result, privateKey);
    System.out.println(new String(result));
    System.out.println(512 / 8 - 1);
  }
}
