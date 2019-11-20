package com.waterelephant.sms.service.small.impl;

import com.alibaba.fastjson.JSONObject;
import com.waterelephant.sms.service.small.ShoppingMallSendService;
import com.waterelephant.sms.service.small.ShoppingMallService;
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
public class ShoppingMallServiceImpl implements ShoppingMallService  {
  private Logger logger = LoggerFactory.getLogger(ShoppingMallServiceImpl.class);
  
  @Autowired
  private ShoppingMallSendService shoppingMallSendServiceImpl;
  @Autowired
  private RedisTemplate<String,String> redisTemplate;
  
  private final String redisKey = "sms_shoppingMall_redis:sendMessage";
  
  public boolean sendShoppingMallMsg(String phone, String msg) {
    boolean flag = false;
    String seqid = String.valueOf(System.currentTimeMillis());
    try  {
      flag = shoppingMallSendServiceImpl.sendShoppingMallMsg(seqid, phone, msg);
      if (flag) {
        Map<String, String> map = new HashMap<>();
        map.put("phone", phone);
        map.put("msg", msg);
        map.put("seqid", seqid);
        map.put("chenal", "1");
        map.put("type", "1");
        String value = JSONObject.toJSONString(map);
        redisTemplate.opsForList().rightPush(redisKey, value);
      }
    }
    catch (Exception e) {
      logger.info("【商城短信】发送短信异常,异常信息为：" + e);
    }
    return flag;
  }
  
  public HttpResult getReport() {
    return shoppingMallSendServiceImpl.getShoppingMallReport();
  }
}
