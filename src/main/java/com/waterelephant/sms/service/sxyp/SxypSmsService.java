package com.waterelephant.sms.service.sxyp;

import com.waterelephant.sms.stateVo.HttpResult;

public abstract interface SxypSmsService{
	
  public abstract boolean sendMessage(String paramString1, String paramString2, String paramString3);
  
  public abstract HttpResult getReport();
  
}
