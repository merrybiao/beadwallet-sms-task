package com.waterelephant.sms.service.olddhst.impl;

import com.alibaba.fastjson.JSONObject;
import com.waterelephant.sms.service.olddhst.DhstSendMessageService;
import com.waterelephant.sms.stateVo.AppResponseResult;
import com.waterelephant.sms.stateVo.HttpResult;
import com.waterelephant.sms.stateVo.Response;
import com.waterelephant.sms.utils.HttpClientUtil;
import com.waterelephant.sms.utils.MD5Tool;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@Service
@PropertySource({"classpath:dahansantong.properties"})
public class DhstSendMessageServiceImpl
  implements DhstSendMessageService
{
  private Logger logger = LoggerFactory.getLogger(DhstSendMessageServiceImpl.class);
  @Value("${dahansantong.sendMsgUrl}")
  private String send_msg_url;
  @Value("${dahansantong.password}")
  private String password;
  @Value("${dahansantong.account}")
  private String account;
  @Value("${dahansantong.getVoiceReportUrl}")
  private String get_voice_report_url;
  
  public boolean sendVoiceSms(String msgId, String callee, String text)
  {
    try
    {
      JSONObject json = new JSONObject();
      json.put("msgId", msgId);
      json.put("callee", callee);
      json.put("text", text);
      String jsonStr = JSONObject.toJSONString(json);
      String resultStr = HttpClientUtil.httpPostToServer(this.send_msg_url, jsonStr);
      JSONObject resultJson = JSONObject.parseObject(resultStr);
      if ("0".equals(resultJson.getString("result"))) {
        return true;
      }
      return false;
    }
    catch (Exception e)
    {
      this.logger.error("执行DhstSendMessageServiceImpl的sendVoiceSms()方法异常：{}", e.getMessage());
      e.printStackTrace();
      
      this.logger.info("结束执行DhstSendMessageServiceImpl的sendVoiceSms()方法！");
    }
    return false;
  }
  
  public HttpResult getVoiceReport()
  {
    HttpResult result = new HttpResult();
    String md5pwd = MD5Tool.md5(this.password);
    Map<String, Object> paramMap = new HashMap();
    paramMap.put("account", this.account);
    paramMap.put("password", md5pwd);
    try
    {
      String json = JSONObject.toJSONString(paramMap);
      String resultStr = HttpClientUtil.httpPostToServer(this.get_voice_report_url, json);
      JSONObject resultJson = JSONObject.parseObject(resultStr);
      if ("DH:0000".equals(resultJson.getString("result")))
      {
        result.setCode(200);
        if (resultJson.getJSONArray("data") != null)
        {
          List<Object> list = resultJson.getJSONArray("data");
          Map<String, Object> mapList = new HashMap();
          mapList.put("voiceSmsReport", list);
          result.setContent(JSONObject.toJSONString(mapList));
        }
      }
      else
      {
        result.setCode(400);
        result.setContent("语音短信发送失败！");
      }
    }
    catch (Exception e)
    {
      result.setCode(900);
      result.setContent("语音短信发送异常！");
      this.logger.error("执行server的DhstSendMessageServiceImpl的getVoiceSmsReport()方法异常：", e);
    }
    this.logger.info("结束执行server的DhstSendMessageServiceImpl的getVoiceSmsReport()方法！");
    return result;
  }
  
  public boolean sendMsg(String msgId, String phone, String msg, String sign)
  {
    Map<String, String> paramMap = new HashMap();
    paramMap.put("msgId", msgId);
    paramMap.put("phones", phone);
    paramMap.put("msg", msg);
    paramMap.put("sign", sign);
    HttpResult result = new HttpResult();
    String json = JSONObject.toJSONString(paramMap);
    try
    {
      String resultStr = HttpClientUtil.httpPostToServer(this.send_msg_url, json);
      JSONObject resultJson = JSONObject.parseObject(resultStr);
      if ("0".equals(resultJson.getString("result")))
      {
        this.logger.info("大汉三通执行手机号：{}短信发送成功！！", phone);
        return true;
      }
      return false;
    }
    catch (Exception e)
    {
      this.logger.error("执行server的DhstSendMessageServiceImpl的sendMsg()方法异常：{}", e.getMessage());
      e.printStackTrace();
    }
    return false;
  }
  
  public boolean batchSendMsg(List<Map<String, String>> mapList)
  {
    try
    {
      Response<Object> response = null;
      if ("200".equals(response.getRequestCode()))
      {
        this.logger.info("短信发送成功！");
        return true;
      }
      this.logger.info("短信发送失败,失败原因:" + response.getRequestMsg());
      return false;
    }
    catch (Exception e)
    {
      this.logger.error("短信发送异常：", e);
    }
    return false;
  }
  
  public AppResponseResult getReport()
  {
    AppResponseResult result = new AppResponseResult();
    try
    {
      Response<Object> response = null;
      result.setCode(response.getRequestCode());
      result.setMsg(response.getRequestMsg());
      result.setResult(response.getObj());
      this.logger.info("获取亿美短信发送状态成功！");
    }
    catch (Exception e)
    {
      result.setCode("900");
      result.setMsg(e.getMessage());
      this.logger.error("获取亿美短信发送状态出错，错误异常：", e.getMessage());
    }
    return result;
  }
}
