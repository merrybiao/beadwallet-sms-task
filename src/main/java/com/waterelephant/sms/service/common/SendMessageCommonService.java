package com.waterelephant.sms.service.common;

public abstract interface SendMessageCommonService {
	
  public abstract boolean commonSendMessage(String paramString1, String paramString2);
  
  public abstract boolean commonSendMessageVoice(String paramString1, String paramString2);
  
  public abstract boolean dhstSendMessage(String paramString1, String paramString2);
  
  public abstract void saveErrorInfo(String paramString1, String paramString2, int paramInt1, int paramInt2);
}
