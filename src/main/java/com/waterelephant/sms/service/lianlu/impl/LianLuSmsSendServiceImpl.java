package com.waterelephant.sms.service.lianlu.impl;

import com.waterelephant.sms.service.lianlu.LianLuSmsSendService;
import com.waterelephant.sms.utils.CommUtils;
import com.waterelephant.sms.utils.HttpUtil;
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
@PropertySource({"classpath:lianlu.properties"})
public class LianLuSmsSendServiceImpl implements LianLuSmsSendService {
	
  private Logger logger = LoggerFactory.getLogger(LianLuSmsSendServiceImpl.class);
  
  @Value("${lianlu.account}")
  private String account;
  @Value("${lianlu.password}")
  private String password;
  @Value("${lianlu.actionSend}")
  private String actionSend;
  @Value("${lianlu.extno}")
  private String extno;
  @Value("${lianlu.sendUrl}")
  private String sendUrl;
  @Value("${lianlu.actionReport}")
  private String actionReport;
  
  public Map<String, String> sendSms(String mobile, String content) throws Exception {
    Map<String, String> map = new HashMap<>();
    StringBuffer sms = new StringBuffer();
    sms.append("account=" + account);
    sms.append("&password=" + password);
    sms.append("&action=" + actionSend);
    sms.append("&extno=" + extno);
    sms.append("&content=" + content);
    sms.append("&mobile=" + mobile);
    String resp = HttpUtil.sendPostRequestBody(sendUrl, sms.toString());
    logger.info("~【联麓信息】返回的消息内容为：{}~", resp);
    Document document = DocumentHelper.parseText(resp);
    Element element = document.getRootElement();
    String status = element.element("returnstatus").getData().toString();
    String taskID = element.element("taskID").getData().toString();
    if ("Success".equals(status)) {
      map.put("requestCode", "200");
      map.put("taskid", taskID);
    }
    else {
      map.put("requestCode", "400");
      logger.error("短信未正常发出，发送短信回执的信息为：{}", resp);
    }
    return map;
  }
  
  public Map<String, String> getRerort() {
    Map<String, String> map = new HashMap<>();
    StringBuffer sms = new StringBuffer();
    sms.append("account=" + account);
    sms.append("&password=" + password);
    sms.append("&action=" + actionReport);
    String resp = null;
    try {
      resp = HttpUtil.sendPostRequestBody(sendUrl, sms.toString());
    }
    catch (Exception e) {
      logger.error("【联麓信息】返回报告发生异常，异常信息为{}", e.getMessage());
      e.printStackTrace();
    }
    logger.info("~【联麓信息】报告返回的消息内容为：{}~", resp);
    if (!CommUtils.isNull(resp)) {
      map.put("requestCode", "200");
      map.put("requestData", resp);
    }
    else {
      map.put("requestCode", "400");
      logger.error("【联麓信息】报告未能正常获取，获取报告返回的信息为：{}", resp);
    }
    return map;
  }
}
