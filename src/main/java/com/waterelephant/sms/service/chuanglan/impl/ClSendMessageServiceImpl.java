package com.waterelephant.sms.service.chuanglan.impl;

import com.alibaba.fastjson.JSONObject;
import com.waterelephant.sms.service.chuanglan.ClSendMessageService;
import com.waterelephant.sms.stateVo.HttpResult;
import com.waterelephant.sms.utils.HttpClientUtil;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource({"classpath:chuanglan.properties"})
public class ClSendMessageServiceImpl implements ClSendMessageService {
  private Logger logger = LoggerFactory.getLogger(ClSendMessageServiceImpl.class);
  @Value("chuanglan.method")
  private String method;
  @Value("chuanglan.sendMsgUrl")
  private String sendMsgUrl;
  @Value("chuanglansendMsgTextUrl")
  private String sendMsgTextUrl;
  
  public boolean sendMsgVoice(String msgId, String phones, String msg) {
    Map<String, String> paramMap = new HashMap<>();
    paramMap.put("seqid", msgId);
    paramMap.put("phones", phones);
    paramMap.put("msg", msg);
    HttpResult result = new HttpResult();
    String jsonStr = JSONObject.toJSONString(paramMap);
    Map<String, String> params = new HashMap<>();
    params.put("method", this.method);
    params.put("voiceinfo", jsonStr);
    try
    {
      String resultStr = HttpClientUtil.httpPostMap(this.sendMsgUrl, params);
      JSONObject resultJson = JSONObject.parseObject(resultStr);
      if ("0".equals(resultJson.getString("code"))) {
        return true;
      }
      return false;
    }
    catch (Exception e)
    {
      this.logger.error("执行ClSendMessageServiceImpl的sendMsgVoice方法异常：{}", e.getMessage());
      e.printStackTrace();
    }
    return false;
  }
  
  public boolean sendMsg(String seqid, String phones, String msg)
  {
    this.logger.info("开始执行server的ClSendMsgServiceImpl的sendMsg()方法！");
    HttpResult result = new HttpResult();
    try
    {
      Map<String, String> paramMap = new HashMap();
      paramMap.put("seqid", seqid);
      paramMap.put("phones", phones);
      paramMap.put("msg", msg);
      String jsonStr = JSONObject.toJSONString(paramMap);
      JSONObject resultJson = HttpClientUtil.httpPostToServer2(this.sendMsgTextUrl, jsonStr);
      this.logger.info("resultJson:" + resultJson);
      if ("0".equals(resultJson.getString("code"))) {
        return true;
      }
      return false;
    }
    catch (Exception e)
    {
      this.logger.error("执行ClSendMessageServiceImpl的sendMsg()方法异常：", e);
    }
    return false;
  }
}
