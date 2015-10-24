package com.classes.scan;

class StringUtils {

  public static boolean isBlank(String str) {
    return !isNotBlank(str);
  }

  public static boolean isNotBlank(String str) {
    if (str != null && str.trim().length() > 0) {
      return true;
    }
    return false;
  }

}
