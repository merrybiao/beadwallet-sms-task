package com.waterelephant.sms.service.sxyp.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.waterelephant.sms.service.sxyp.SxypSmsSendService;
import com.waterelephant.sms.stateVo.DataCaller;
import com.waterelephant.sms.stateVo.HttpResult;
import java.util.HashMap;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource({"classpath:smsYimei.properties"})
public class SxypSmsSendServiceImpl implements SxypSmsSendService {
	
  private Logger logger = LoggerFactory.getLogger(SxypSmsSendServiceImpl.class);
  @Value("${yimei.sxypSmskey}")
  private String sxypSmskey;
  @Value("${yimei.sxypSmsPassword}")
  private String sxypSmsPassword;
  @Value("${yimei.reportUrl}")
  private String reportUrl;
  @Value("${yimei.sendUrl}")
  private String sendUrl;
  
  public HttpResult getReport() {
    HttpResult result = new HttpResult();
    
    Map<String, String> paramMap = new HashMap<>();
    paramMap.put("cdkey", sxypSmskey);
    paramMap.put("password", sxypSmsPassword);
    try {
      DataCaller dataCaller = DataCaller.getCaller();
      result = dataCaller.httpPost2(reportUrl, null, paramMap);
      logger.info("水象优品获取亿美短信发送状态成功：" + JSONObject.toJSONString(result));
    }
    catch (Exception e) {
      result.setCode(900);
      logger.error("水象优品获取亿美短信发送状态出错，错误异常：", e.getMessage());
    }
    return result;
  }
  
  public boolean sendMsg(String seqid, String phones, String msg) {
    String paramStr = "?cdkey=" + sxypSmskey + "&password=" + sxypSmsPassword + "&phone=" + phones + "&message=" + msg + "&seqid=" + seqid;
    String url = sendUrl + paramStr;
    try {
      DataCaller dataCaller = DataCaller.getCaller();
      HttpResult result = dataCaller.httpGet(url, null);
      if (result.getCode() == 200) {
    	  String content = result.getContent();
    	  Document doc = DocumentHelper.parseText(content);
    	  Element root = doc.getRootElement();
    	  String error = root.elementText("error");
    	  if("0".equals(error)) {
    		 logger.info("短信发送请求成功！请求返回结果：" + JSONObject.toJSONString(result.getContent()));
    		 return true;
    	  }
      }
      logger.info("水象优品通过亿美发短信的返回值为:{}", JSON.toJSONString(result));
      return 200 == result.getCode();
    }
    catch (Exception e) {
      logger.error("水象优品通过亿美发送短信出现异常：" + e.getMessage());
    }
    return false;
  }
}
