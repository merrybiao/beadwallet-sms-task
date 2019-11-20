package com.waterelephant.sms.service.olddhst;

import com.waterelephant.sms.stateVo.AppResponseResult;
import com.waterelephant.sms.stateVo.HttpResult;
import java.util.List;
import java.util.Map;

public abstract interface DhstSendMessageService
{
  public abstract boolean sendMsg(String paramString1, String paramString2, String paramString3, String paramString4);
  
  public abstract boolean batchSendMsg(List<Map<String, String>> paramList);
  
  public abstract AppResponseResult getReport();
  
  public abstract boolean sendVoiceSms(String paramString1, String paramString2, String paramString3);
  
  public abstract HttpResult getVoiceReport();
}
