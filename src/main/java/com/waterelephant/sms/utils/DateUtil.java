package com.waterelephant.sms.utils;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class DateUtil
{
  private static final Logger log = LoggerFactory.getLogger(DateUtil.class);
  public static final String yyyy_MM_dd_HHmmss = "yyyy-MM-dd HH:mm:ss";
  public static final String yyyyMMdd_HHmmss = "yyyyMMdd hh:mm:ss";
  public static final String yyyyMMddHHmmss = "yyyyMMddHHmmss";
  public static final String yyyy_MM_dd = "yyyy-MM-dd";
  public static final String yyyyMMdd = "yyyyMMdd";
  public static final String HH_mm_ss = "HHmmss";
  public static final String yyMMdd_HHmmss = "yy/MM/dd HH:mm:ss";
  public static final String yyyy_MM_dd_HHmmssSSS = "yyyy-MM-dd HH:mm:ss.SSS";
  public static final String yyyyMMddHHmmssSSS = "yyyyMMddHHmmssSSS";
  
  public static String getCurrentDateString(String pattern)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    return sdf.format(new Date());
  }
  
  public static String getDateString(Date date, String pattern)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    return sdf.format(date);
  }
  
  public static String getDateString(String dateStr, String pattern)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    try
    {
      return sdf.format(sdf.parse(dateStr));
    }
    catch (ParseException e)
    {
      log.error("日期解析错误,日期:" + dateStr);
    }
    return "";
  }
  
  public static Timestamp getCurrentTimestamp()
  {
    return new Timestamp(new Date().getTime());
  }
  
  public static Date getDate(String dateStr)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    try
    {
      return sdf.parse(dateStr);
    }
    catch (ParseException e)
    {
      throw new RuntimeException("解析异常");
    }
  }
  
  public static int intervalDay(Date start, Date end)
  {
    Long l = Long.valueOf(end.getTime() - start.getTime());
    Long m = Long.valueOf(l.longValue() / 1000L / 60L / 60L / 24L);
    return m.intValue();
  }
  
  public static Date stringToDate(String dateStr, String pattern)
  {
    SimpleDateFormat sdf = new SimpleDateFormat(pattern);
    Date date = new Date();
    try
    {
      date = sdf.parse(dateStr);
    }
    catch (ParseException e)
    {
      log.error("日期解析错误,日期:" + dateStr);
    }
    return date;
  }
  
  public static String getDateString(Date date)
  {
    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    return sdf.format(date);
  }
}
