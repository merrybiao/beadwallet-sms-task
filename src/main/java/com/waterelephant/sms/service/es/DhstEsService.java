package com.waterelephant.sms.service.es;

import com.waterelephant.sms.entity.dhst.BwDhstMessageReport;
import java.util.List;

public abstract interface DhstEsService
{
  public abstract boolean saveInfo(String paramString1, String paramString2, String paramString3, String paramString4, String paramString5);
  
  public abstract boolean saveReport(List<BwDhstMessageReport> paramList);
  
  public abstract boolean updatetInfo(List<BwDhstMessageReport> paramList);
}
