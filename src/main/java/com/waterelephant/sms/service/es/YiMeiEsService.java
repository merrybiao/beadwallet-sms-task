package com.waterelephant.sms.service.es;

public abstract interface YiMeiEsService
{
  public abstract void saveInfo(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2);
  
  public abstract void update(String paramString1, int paramInt, String paramString2, String paramString3);
  
  public abstract void saveReport(String paramString1, String paramString2, String paramString3, int paramInt1, int paramInt2);
}
