package com.waterelephant.sms.constant;

import java.util.ResourceBundle;

public class YimeiConstant
{
  public static String cdkey = "";
  public static String cdNewkey = "";
  public static String password = "";
  public static String newPassword = "";
  public static String getReportUrl = "";
  public static String sendMsgUrl = "";
  public static String smsPriority = "";
  
  static {
    ResourceBundle config_yimei = ResourceBundle.getBundle("yimei");
    cdkey = config_yimei.getString("cdkey");
    cdNewkey = config_yimei.getString("cdNewkey");
    password = config_yimei.getString("password");
    newPassword = config_yimei.getString("newPassword");
    getReportUrl = config_yimei.getString("getReportUrl");
    sendMsgUrl = config_yimei.getString("sendMsgUrl");
    smsPriority = config_yimei.getString("smsPriority");
  }
}
