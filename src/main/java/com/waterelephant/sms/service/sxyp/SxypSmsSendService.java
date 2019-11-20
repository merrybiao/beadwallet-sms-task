package com.waterelephant.sms.service.sxyp;

import com.waterelephant.sms.stateVo.HttpResult;

public abstract interface SxypSmsSendService
{
  public abstract HttpResult getReport();
  
  public abstract boolean sendMsg(String paramString1, String paramString2, String paramString3);
}
