package com.waterelephant.sms.sdk;

import com.waterelephant.sms.stateVo.StatusReport;
import java.rmi.Remote;
import java.rmi.RemoteException;

public abstract interface SDKClient
  extends Remote
{
  public abstract String sendVoice(String paramString1, String paramString2, String paramString3, String[] paramArrayOfString, String paramString4, String paramString5, String paramString6, int paramInt, long paramLong)
    throws RemoteException;
  
  public abstract StatusReport[] getReport(String paramString1, String paramString2)
    throws RemoteException;
}
