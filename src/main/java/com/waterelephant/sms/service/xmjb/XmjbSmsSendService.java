package com.waterelephant.sms.service.xmjb;

import com.waterelephant.sms.stateVo.HttpResult;

public abstract interface XmjbSmsSendService {
	
  public abstract boolean sendXmjbMsg(String paramString1, String paramString2, String paramString3);
  
  public abstract HttpResult getXmjbReport();
}
