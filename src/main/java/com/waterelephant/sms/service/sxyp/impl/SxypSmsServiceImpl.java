package com.waterelephant.sms.service.sxyp.impl;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.waterelephant.sms.service.sxyp.SxypSmsSendService;
import com.waterelephant.sms.service.sxyp.SxypSmsService;
import com.waterelephant.sms.stateVo.HttpResult;

@Service
public class SxypSmsServiceImpl implements SxypSmsService {
	
  private Logger logger = LoggerFactory.getLogger(SxypSmsServiceImpl.class);
  @Autowired
  private RedisTemplate<String,String> redisTemplate;
  @Autowired
  private SxypSmsSendService sxypSmsSendServiceImpl;
  
  private final String REDIS_CATCH_KEY = "sms_sxyp_redis:sendMessage";
  
  public boolean sendMessage(String phone, String msg, String singKey) {
    boolean flag = false;
    String seqid = String.valueOf(System.currentTimeMillis());
    try {
      flag = sxypSmsSendServiceImpl.sendMsg(seqid, phone, msg);
      if (flag) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("msg", msg);
        map.put("seqid", seqid);
        map.put("chenal", "1");
        map.put("type", "1");
        redisTemplate.opsForList().rightPush(REDIS_CATCH_KEY, JSON.toJSONString(map));
      }
    }
    catch (Exception e) {
      logger.info("水象优品发送短信异常,异常信息为：{}" + e.getMessage());
      e.printStackTrace();
    }
    return flag;
  }
  
  public HttpResult getReport() {
    return sxypSmsSendServiceImpl.getReport();
  }
}
