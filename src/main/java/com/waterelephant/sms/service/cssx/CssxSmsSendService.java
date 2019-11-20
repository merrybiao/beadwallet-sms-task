package com.waterelephant.sms.service.cssx;

import com.waterelephant.sms.stateVo.HttpResult;

public abstract interface CssxSmsSendService
{
  public abstract HttpResult getCssxReport();
  
  public abstract boolean sendCssxMsg(String paramString1, String paramString2, String paramString3);
}
