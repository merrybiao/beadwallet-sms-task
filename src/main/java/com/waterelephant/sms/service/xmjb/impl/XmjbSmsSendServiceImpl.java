package com.waterelephant.sms.service.xmjb.impl;

import com.alibaba.fastjson.JSONObject;
import com.waterelephant.sms.service.xmjb.XmjbSmsSendService;
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
public class XmjbSmsSendServiceImpl
  implements XmjbSmsSendService
{
  private Logger logger = LoggerFactory.getLogger(XmjbSmsSendServiceImpl.class);
  @Value("${yimei.xmjbSmskey}")
  private String xmjbSmskey;
  @Value("${yimei.xmjbSmsPassword}")
  private String xmjbSmsPassword;
  @Value("${yimei.sendUrl}")
  private String sendUrl;
  @Value("${yimei.reportUrl}")
  private String reportUrl;
  
  public boolean sendXmjbMsg(String seqid, String phones, String msg) {
    HttpResult result = new HttpResult();
    String paramStr = "?cdkey=" + xmjbSmskey + "&password=" + xmjbSmsPassword + "&phone=" + phones + "&message=" + msg + "&seqid=" + seqid;
    try {
      DataCaller dataCaller = DataCaller.getCaller();
      result = dataCaller.httpGet(sendUrl + paramStr, null);
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
      logger.info("----【厦门借宝】----通过亿美发送短信成功，返回参数：" + JSONObject.toJSONString(result));
      return 200 == result.getCode();
    }
    catch (Exception e) {
      result.setCode(900);
      result.setContent(null);
      logger.error("----【厦门借宝】----通过亿美发送短信出现异常：" + e.getMessage());
    }
    return false;
  }
  
  public HttpResult getXmjbReport() {
    logger.info("----【厦门借宝】----开始执行的CssxSendMessageServiceImpl的getCssxReport方法");
    HttpResult result = new HttpResult();
    
    Map<String, String> paramMap = new HashMap<>();
    paramMap.put("cdkey", xmjbSmskey);
    paramMap.put("password", xmjbSmsPassword);
    try {
      DataCaller dataCaller = DataCaller.getCaller();
      result = dataCaller.httpPost2(reportUrl, null, paramMap);
      logger.info("----【厦门借宝】----获取亿美短信发送状态成功：" + JSONObject.toJSONString(result));
    }
    catch (Exception e) {
      result.setCode(900);
      logger.error("----【厦门借宝】----获取亿美短信发送状态出错，错误异常：", e.getMessage());
    }
    return result;
  }
}
