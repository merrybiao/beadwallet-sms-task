package com.waterelephant.sms.service.lianlu;

import java.util.Map;

public abstract interface LianLuSmsSendService {
  public abstract Map<String, String> sendSms(String paramString1, String paramString2) throws Exception;
  
  public abstract Map<String, String> getRerort();
}
