package com.waterelephant.sms.utils;

import java.security.MessageDigest;

public class MD5Tool
{
  public static final String md5(String text)
  {
    char[] md5String = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };
    try
    {
      byte[] btInput = text.getBytes();
      MessageDigest mdInput = MessageDigest.getInstance("MD5");
      mdInput.update(btInput);
      byte[] md = mdInput.digest();
      

      int i = md.length;
      char[] str = new char[i * 2];
      int k = 0;
      for (int j = 0; j < i; j++)
      {
        byte byteStr = md[j];
        str[(k++)] = md5String[(byteStr >>> 4 & 0xF)];
        str[(k++)] = md5String[(byteStr & 0xF)];
      }
      return new String(str).toLowerCase();
    }
    catch (Exception e) {}
    return null;
  }
}
