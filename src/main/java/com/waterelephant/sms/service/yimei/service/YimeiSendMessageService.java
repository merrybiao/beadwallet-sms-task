package com.waterelephant.sms.service.yimei.service;

import com.waterelephant.sms.stateVo.HttpResult;
import com.waterelephant.sms.stateVo.StatusReport;
import java.util.List;

public abstract interface YimeiSendMessageService {
	
  public abstract HttpResult getReport();
  
  public abstract boolean sendMsg(String paramString1, String paramString2, String paramString3);
  
  public abstract boolean sendVoiceMsg(String paramString1, String paramString2, String paramString3);
  
  public abstract List<StatusReport> getVoiceReport();
  
}
