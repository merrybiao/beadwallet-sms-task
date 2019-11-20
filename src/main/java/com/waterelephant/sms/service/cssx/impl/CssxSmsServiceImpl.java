package com.waterelephant.sms.service.cssx.impl;

import com.alibaba.fastjson.JSONObject;
import com.waterelephant.sms.service.cssx.CssxSmsSendService;
import com.waterelephant.sms.service.cssx.CssxSmsService;
import com.waterelephant.sms.stateVo.HttpResult;
import java.util.HashMap;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class CssxSmsServiceImpl
  implements CssxSmsService
{
  private Logger logger = LoggerFactory.getLogger(CssxSmsServiceImpl.class);
  @Autowired
  private CssxSmsSendService cssxSmsSendServiceImpl;
  @Autowired
  private RedisTemplate redisTemplate;
  private final String REDIS_CATCH_KEY = "sms_cssx_redis:sendMessage";
  
  public boolean sendCssxMessage(String phone, String msg)
  {
    boolean flag = false;
    String seqid = String.valueOf(System.currentTimeMillis());
    try
    {
      flag = this.cssxSmsSendServiceImpl.sendCssxMsg(seqid, phone, msg);
      if (flag)
      {
        Map<String, String> map = new HashMap();
        map.put("phone", phone);
        map.put("msg", msg);
        map.put("seqid", seqid);
        map.put("chenal", "1");
        map.put("type", "1");
        String value = JSONObject.toJSONString(map);
        this.redisTemplate.opsForList().rightPush("sms_cssx_redis:sendMessage", value);
      }
    }
    catch (Exception e)
    {
      this.logger.info("【长沙水象】发送短信异常,异常信息为：{}", e.getMessage());
      e.printStackTrace();
    }
    return flag;
  }
  
  public HttpResult getSendReport()
  {
    return this.cssxSmsSendServiceImpl.getCssxReport();
  }
}
