package com.waterelephant.sms.utils;

import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;

public class SingletonClient
{
  private static Client client = null;
  
  public static synchronized Client getClient(String softwareSerialNo, String key)
  {
    if (client == null) {
      try
      {
        client = new Client(softwareSerialNo, key);
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    return client;
  }
  
  public static synchronized Client getClient()
  {
    ResourceBundle bundle = PropertyResourceBundle.getBundle("yimei");
    if (client == null) {
      try
      {
        client = new Client(bundle.getString("yimei.softwareSerialNo"), bundle.getString("yimei.key"));
      }
      catch (Exception e)
      {
        e.printStackTrace();
      }
    }
    return client;
  }
}
