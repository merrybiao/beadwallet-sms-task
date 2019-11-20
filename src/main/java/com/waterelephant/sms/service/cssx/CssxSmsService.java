package com.waterelephant.sms.service.cssx;

import com.waterelephant.sms.stateVo.HttpResult;

public abstract interface CssxSmsService
{
  public abstract boolean sendCssxMessage(String paramString1, String paramString2);
  
  public abstract HttpResult getSendReport();
}
