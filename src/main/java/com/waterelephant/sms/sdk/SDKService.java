package com.waterelephant.sms.sdk;

import java.net.URL;
import javax.xml.rpc.Service;
import javax.xml.rpc.ServiceException;

public abstract interface SDKService
  extends Service
{
  public abstract String getSDKServiceAddress();
  
  public abstract SDKClient getSDKService()
    throws ServiceException;
  
  public abstract SDKClient getSDKService(URL paramURL)
    throws ServiceException;
}
