package com.waterelephant.sms.service.small.impl;

import com.alibaba.fastjson.JSONObject;
import com.waterelephant.sms.service.small.ShoppingMallSendService;
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
@PropertySource({"classpath:yimei.properties"})
public class ShoppingMallSendServiceImpl
  implements ShoppingMallSendService
{
  private static Logger logger = LoggerFactory.getLogger(ShoppingMallSendServiceImpl.class);
  @Value("${yimei.smsPriority}")
  public int smsPriority;
  @Value("${yimei.cdSckey}")
  public String cdSckey;
  @Value("${yimei.scPassword}")
  public String scPassword;
  @Value("${yimei.sendMsgUrl}")
  public String sendMsgUrl;
  @Value("${yimei.reportUrl}")
  public String reportUrl;
  
  public boolean sendShoppingMallMsg(String seqid, String phone, String msg) {
    try {
      String paramStr = "?cdkey=" + cdSckey + "&password=" + scPassword + "&phone=" + phone + "&message=" + msg + "&seqid=" + seqid;
      
      String url = sendMsgUrl + paramStr;
      DataCaller dataCaller = DataCaller.getCaller();
      HttpResult result = dataCaller.httpGet(url, null);
      if (result.getCode() == 200)  {
    	  String content = result.getContent();
    	  Document doc = DocumentHelper.parseText(content);
    	  Element root = doc.getRootElement();
    	  String error = root.elementText("error");
    	  if("0".equals(error)) {
	        logger.info("商城短信发送请求成功！请求返回结果：" + JSONObject.toJSONString(result.getContent()));
	        return true;
    	  }
      }
      logger.info("商城短信发送请求失败！请求返回结果：" + JSONObject.toJSONString(result.getContent()));
      return false;
    }
    catch (Exception e) {
      logger.error("商城短信发送出现异常，异常信息：" + e.getMessage());
    }
    return false;
  }
  
  public HttpResult getShoppingMallReport()
  {
    logger.info("商城开始执行亿美的获取报告的getReport方法");
    HttpResult result = new HttpResult();
    Map<String, String> paramMap = new HashMap();
    paramMap.put("cdkey", cdSckey);
    paramMap.put("password", scPassword);
    try
    {
      DataCaller dataCaller = DataCaller.getCaller();
      result = dataCaller.httpPost2(reportUrl, null, paramMap);
      logger.info("商城获取亿美短信发送状态成功：" + JSONObject.toJSONString(result));
    }
    catch (Exception e)
    {
      result.setCode(900);
      logger.error("商城获取亿美短信发送状态出错，错误异常：", e.getMessage());
    }
    return result;
  }
}
