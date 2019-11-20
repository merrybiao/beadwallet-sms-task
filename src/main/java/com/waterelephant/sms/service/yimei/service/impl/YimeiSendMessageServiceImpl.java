package com.waterelephant.sms.service.yimei.service.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.dom4j.Document;
import org.dom4j.DocumentHelper;
import org.dom4j.Element;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.waterelephant.sms.service.yimei.service.YimeiSendMessageService;
import com.waterelephant.sms.stateVo.DataCaller;
import com.waterelephant.sms.stateVo.HttpResult;
import com.waterelephant.sms.stateVo.StatusReport;
import com.waterelephant.sms.utils.SingletonClient;

@Service
@PropertySource({"classpath:yimei.properties"})
public class YimeiSendMessageServiceImpl implements YimeiSendMessageService {
  private static Logger logger = LoggerFactory.getLogger(YimeiSendMessageServiceImpl.class);
  @Value("${yimei.smsPriority}")
  public int smsPriority;
  @Value("${yimei.cdkey}")
  public String cdkey;
  @Value("${yimei.password}")
  public String password;
  @Value("${yimei.sendMsgUrl}")
  public String sendMsgUrl;
  @Value("${yimei.reportUrl}")
  public String reportUrl;
  
  public boolean sendMsg(String seqid, String phones, String msg) {
    try {
      String paramStr = "?cdkey=" + cdkey + "&password=" + password + "&phone=" + phones + "&message=" + msg + "&seqid=" + seqid;
      
      String url = sendMsgUrl + paramStr;
      
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
      logger.info("短信发送请求失败！请求返回结果：" + JSONObject.toJSONString(result.getContent()));
      return false;   
    }
    catch (Exception e) {
      logger.error("短信发送出现异常，异常信息：" + e.getMessage());
    }
    return false;
  }
  
  public  HttpResult getReport() {
    logger.info("开始执行亿美的获取报告的getReport方法");
    HttpResult result = new HttpResult();
    
    Map<String, String> paramMap = new HashMap<>();
    paramMap.put("cdkey", "8SDK-EMY-6699-RJUMO");
    paramMap.put("password", "563319");
    try {
      DataCaller dataCaller = DataCaller.getCaller();
      result = dataCaller.httpPost2("http://www.btom.cn:8080", null, paramMap);
      logger.info("获取亿美短信发送状态成功：" + JSONObject.toJSONString(result));
    }
    catch (Exception e) {
      result.setCode(900);
      logger.error("获取亿美短信发送状态出错，错误异常：", e.getMessage());
    }
    return result;
  }
  
  public boolean sendVoiceMsg(String phones, String msg, String seqid) {
    String[] phone = phones.split(",");
    try {
      String value = SingletonClient.getClient().sendVoice(phone, msg, "", "GBK", smsPriority, Long.parseLong(seqid));
      logger.info("语音验证码返回结果：" + value);
      int code = Integer.parseInt(value.substring(0, value.indexOf(":")));
      if (code == 0) {
        return true;
      }
      return false;
    }
    catch (Exception e) {
      logger.error("语音验证码发送异常:，异常信息{}", e.getMessage());
      e.printStackTrace();
    }
    return false;
  }
  
  public List<StatusReport> getVoiceReport() {
    List<StatusReport> list = null;
    try {
      list = SingletonClient.getClient().getReport();
    }
    catch (Exception e) {
      logger.error("更新语音短信状态异常：{}", e.getMessage());
    }
    return list;
  }
  
  public static void main(String[] args) {
	HttpResult report = new YimeiSendMessageServiceImpl().getReport();
	System.out.println(JSON.toJSONString(report));
}
}
