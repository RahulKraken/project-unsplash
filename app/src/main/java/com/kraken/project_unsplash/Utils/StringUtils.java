package com.kraken.project_unsplash.Utils;

public class StringUtils {

  public static String getKeyword(String s) {
    // todo : handle multiple spaces
    return s.replace(" ", "+");
  }
}
