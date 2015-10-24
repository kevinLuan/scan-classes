package com.classes.utils;

import java.security.MessageDigest;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;

import javax.crypto.Cipher;

import com.sun.org.apache.xerces.internal.impl.dv.util.Base64;

public class Md5RSA {
  public static void main(String[] args) throws Exception {}


  public static byte[] sign0(byte[] content, PrivateKey privateKey) throws Exception {
    Signature signature = Signature.getInstance("SHA1withRSA");
    signature.initSign(privateKey);
    signature.update(content);
    return signature.sign();
  }

  public static boolean verify0(byte[] content, byte[] sign, PublicKey publicKey) throws Exception {
    Signature signature = Signature.getInstance("SHA1withRSA");
    signature.initVerify(publicKey);
    signature.update(content);
    return signature.verify(sign);
  }

  public static byte[] sign(byte[] content, PrivateKey privateKey) throws Exception {
    Signature signature = Signature.getInstance("MD5withRSA");
    signature.initSign(privateKey);
    signature.update(content);
    return signature.sign();
  }

  public static boolean verify(byte[] content, byte[] sign, PublicKey publicKey) throws Exception {
    Signature signature = Signature.getInstance("MD5withRSA");
    signature.initVerify(publicKey);
    signature.update(content);
    return signature.verify(sign);
  }

  public static byte[] sign1(byte[] content, PrivateKey privateKey) throws Exception {
    MessageDigest messageDigest = MessageDigest.getInstance("MD5");
    byte[] bytes = messageDigest.digest(content);
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, privateKey);
    return cipher.doFinal(bytes);
  }

  public static boolean verify1(byte[] content, byte[] sign, PublicKey publicKey) throws Exception {
    MessageDigest mDigest = MessageDigest.getInstance("MD5");
    byte[] bytes = mDigest.digest(content);
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, publicKey);
    byte[] decryptBytes = cipher.doFinal(bytes);
    if (Base64.encode(decryptBytes).equals(Base64.encode(sign))) {
      return true;
    } else {
      return false;
    }
  }



  public static byte[] sign2(byte[] content, PrivateKey privateKey) throws Exception {
    MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
    byte[] bytes = messageDigest.digest(content);
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.ENCRYPT_MODE, privateKey);
    return cipher.doFinal(bytes);
  }

  public static boolean verify2(byte[] content, byte[] sign, PublicKey publicKey) throws Exception {
    MessageDigest mDigest = MessageDigest.getInstance("SHA1");
    byte[] bytes = mDigest.digest(content);
    Cipher cipher = Cipher.getInstance("RSA");
    cipher.init(Cipher.DECRYPT_MODE, publicKey);
    byte[] decryptBytes = cipher.doFinal(bytes);
    if (Base64.encode(decryptBytes).equals(Base64.encode(sign))) {
      return true;
    } else {
      return false;
    }
  }
}
