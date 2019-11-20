package com.waterelephant.sms.service.chuanglan;

public abstract interface ClSendMessageService
{
  public abstract boolean sendMsgVoice(String paramString1, String paramString2, String paramString3);
  
  public abstract boolean sendMsg(String paramString1, String paramString2, String paramString3);
}
