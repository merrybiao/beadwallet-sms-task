package com.waterelephant.sms.service.cssx.impl;

import com.alibaba.fastjson.JSONObject;
import com.waterelephant.sms.service.cssx.CssxSmsSendService;
import com.waterelephant.sms.stateVo.DataCaller;
import com.waterelephant.sms.stateVo.HttpResult;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Service;

@PropertySource({"classpath:smsYimei.properties"})
@Service
public class CssxSmsSendServiceImpl
  implements CssxSmsSendService
{
  private Logger logger = LoggerFactory.getLogger(CssxSmsSendServiceImpl.class);
  @Value("${yimei.cssxSmskey}")
  private String cssxSmskey;
  @Value("${yimei.cssxSmsPassword}")
  private String cssxSmsPassword;
  @Value("${yimei.sendUrl}")
  private String sendUrl;
  @Value("${yimei.reportUrl}")
  private String reportUrl;
  
  public HttpResult getCssxReport()
  {
    this.logger.info("----【长沙水象】----开始执行的CssxSendMessageServiceImpl的getCssxReport方法");
    HttpResult result = new HttpResult();
    
    Map<String, String> paramMap = new HashMap();
    paramMap.put("cdkey", this.cssxSmskey);
    paramMap.put("password", this.cssxSmsPassword);
    try
    {
      DataCaller dataCaller = DataCaller.getCaller();
      result = dataCaller.httpPost2(this.reportUrl, null, paramMap);
      this.logger.info("----【长沙水象】----获取亿美短信发送状态成功：" + JSONObject.toJSONString(result));
    }
    catch (Exception e)
    {
      result.setCode(900);
      this.logger.error("----【长沙水象】----获取亿美短信发送状态出错，错误异常：", e.getMessage());
    }
    return result;
  }
  
  public boolean sendCssxMsg(String seqid, String phones, String msg)
  {
    String paramStr = "?cdkey=" + this.cssxSmskey + "&password=" + this.cssxSmsPassword + "&phone=" + phones + "&message=" + msg + "&seqid=" + seqid;
    
    String url = this.sendUrl + paramStr;
    try
    {
      DataCaller dataCaller = DataCaller.getCaller();
      HttpResult result = dataCaller.httpGet(url, null);
      if (result.getCode() == 200)
      {
        this.logger.info("长沙水象短信发送请求成功！请求返回结果：" + result.getContent());
        return true;
      }
      this.logger.info("长沙水象短信发送请求失败！请求返回结果：" + result.getContent());
      return false;
    }
    catch (Exception e)
    {
      this.logger.error("----长沙水象----通过亿美发送短信出现异常：{}" + e.getMessage());
      e.printStackTrace();
    }
    return false;
  }
}
