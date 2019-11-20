package com.waterelephant.sms.service.xmjb;

import com.waterelephant.sms.stateVo.HttpResult;

public abstract interface XmjbSmsService  {
	
  public abstract boolean sendXmjbMessage(String paramString1, String paramString2);
  
  public abstract HttpResult getXmjbReport();
}
