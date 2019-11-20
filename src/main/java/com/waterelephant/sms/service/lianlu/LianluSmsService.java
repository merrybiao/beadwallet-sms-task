package com.waterelephant.sms.service.lianlu;

import com.waterelephant.sms.exception.LianluException;
import java.util.Map;
import org.dom4j.DocumentException;

public abstract interface LianluSmsService {
	
  public abstract String sendMsg(String paramString1, String paramString2, String paramString3, Integer paramInteger) throws LianluException;
  
  public abstract Map<String, String> getReport() throws LianluException, DocumentException;
}
