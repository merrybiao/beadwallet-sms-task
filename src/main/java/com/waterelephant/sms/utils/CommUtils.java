package com.waterelephant.sms.utils;

import java.io.PrintStream;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.security.MessageDigest;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.Map;
import java.util.Random;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CommUtils
{
  public static boolean isNull(Object obj)
  {
    boolean result = false;
    if (obj != null)
    {
      if ((obj instanceof String))
      {
        if (((String)obj).trim().isEmpty()) {
          result = true;
        }
      }
      else if ((obj instanceof Collection))
      {
        if (((Collection)obj).isEmpty()) {
          result = true;
        }
      }
      else if ((obj instanceof Map))
      {
        if (((Map)obj).isEmpty()) {
          result = true;
        }
      }
      else if ((obj.getClass().isArray()) && 
        (Array.getLength(obj) <= 0)) {
        return true;
      }
    }
    else {
      result = true;
    }
    return result;
  }
  
  public static String getUUID()
  {
    String s = UUID.randomUUID().toString();
    
    return s.substring(0, 8) + s.substring(9, 13) + s.substring(14, 18) + s.substring(19, 23) + s.substring(24);
  }
  
  public static String getMD5(byte[] source)
  {
    String s = null;
    
    char[] hexDigits = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f' };
    try
    {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(source);
      byte[] tmp = md.digest();
      char[] ch = new char[32];
      int k = 0;
      for (int i = 0; i < 16; i++)
      {
        byte byte0 = tmp[i];
        ch[(k++)] = hexDigits[(byte0 >>> 4 & 0xF)];
        ch[(k++)] = hexDigits[(byte0 & 0xF)];
      }
      s = new String(ch);
    }
    catch (Exception ex)
    {
      Logger.getLogger(CommUtils.class.getName()).log(Level.SEVERE, null, ex);
    }
    return s;
  }
  
  public static Date convertStringToDate(String dateString, String pattern)
  {
    Date date = null;
    if ((pattern == null) || (pattern.trim().equals(""))) {
      pattern = "yyyy-MM-dd";
    }
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    try
    {
      date = sdf.parse(dateString);
    }
    catch (ParseException ex)
    {
      Logger.getLogger(CommUtils.class.getName()).log(Level.SEVERE, null, ex);
    }
    return date;
  }
  
  public static String convertDateToString(Date date, String pattern)
  {
    if ((pattern == null) || (pattern.trim().equals(""))) {
      pattern = "yyyy-MM-dd";
    }
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    String dateString = sdf.format(date);
    return dateString;
  }
  
  public static boolean compareDate(Date d1, Date d2, String pattern)
  {
    boolean result = false;
    if ((d1 != null) && (d2 != null))
    {
      String date1 = convertDateToString(d1, pattern);
      String date2 = convertDateToString(d2, pattern);
      if (date1.equals(date2)) {
        result = true;
      }
    }
    return result;
  }
  
  public static double div(double v1, double v2, int scale)
  {
    if (scale < 0) {
      throw new IllegalArgumentException("The scale must be a positive integer or zero");
    }
    BigDecimal b1 = new BigDecimal(Double.toString(v1));
    BigDecimal b2 = new BigDecimal(Double.toString(v2));
    return b1.divide(b2, scale, 4).doubleValue();
  }
  
  public static boolean validate(int dataType, String data)
  {
    String regexStr = "";
    switch (dataType)
    {
    case 1: 
      regexStr = "^([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)*@([a-zA-Z0-9]*[-_]?[a-zA-Z0-9]+)+[\\.][A-Za-z]{2,3}([\\.][A-Za-z]{2})?$";
      break;
    case 2: 
      regexStr = "^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$";
      break;
    case 3: 
      regexStr = "(\\d{11})|^((\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})|(\\d{4}|\\d{3})-(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1})|(\\d{7,8})-(\\d{4}|\\d{3}|\\d{2}|\\d{1}))$";
      break;
    case 4: 
      regexStr = "-?(0|([1-9][0-9]*))(.[0-9]+)?";
    }
    Pattern pattern = Pattern.compile(regexStr);
    Matcher matcher = pattern.matcher(data);
    return matcher.matches();
  }
  
  public static String getRandomNumber(int num)
  {
    Random random = new Random();
    StringBuilder randomCode = new StringBuilder();
    for (int i = 0; i < num; i++)
    {
      String strRand = String.valueOf(random.nextInt(10));
      
      randomCode.append(strRand);
    }
    return randomCode.toString();
  }
  
  public static String formatterPhone(String phone)
  {
    if (!isNull(phone)) {
      return phone.substring(0, 3) + "****" + phone.substring(phone.length() - 4);
    }
    return "";
  }
  
  public static String formatterIdCard(String idCard)
  {
    if (!isNull(idCard)) {
      return idCard.substring(0, 3) + "****" + idCard.substring(idCard.length() - 4);
    }
    return "";
  }
  
  public static void main(String[] args)
  {
    String phone = "420921199410293011";
    
    System.out.println(phone.substring(phone.length() - 4));
  }
}
