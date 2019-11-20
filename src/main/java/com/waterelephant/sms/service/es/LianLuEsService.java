package com.waterelephant.sms.service.es;

import com.waterelephant.sms.entity.lianlu.BwLianluMessageError;

public abstract interface LianLuEsService
{
  public abstract void saveInfo(String paramString1, String paramString2, String paramString3, Integer paramInteger, String paramString4);
  
  public abstract void update(String paramString1, String paramString2, String paramString3, String paramString4);
  
  public abstract boolean saveReport(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5);
  
  public abstract boolean saveErrorInfo(BwLianluMessageError paramBwLianluMessageError);
}
