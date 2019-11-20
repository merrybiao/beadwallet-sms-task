package com.waterelephant.sms.service.common.impl;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONObject;
import com.waterelephant.sms.elasticRepos.CommonMessageErrorRepos;
import com.waterelephant.sms.entity.commom.CommonMessageError;
import com.waterelephant.sms.entity.yimei.SmsConfig;
import com.waterelephant.sms.mapper.SmsConifigMapper;
import com.waterelephant.sms.service.chuanglan.ClSendMessageService;
import com.waterelephant.sms.service.common.SendMessageCommonService;
import com.waterelephant.sms.service.olddhst.DhstSendMessageService;
import com.waterelephant.sms.service.yimei.service.YimeiSendMessageService;

@Service
public class SendMessageCommonServiceImpl implements SendMessageCommonService {
	
  private Logger logger = LoggerFactory.getLogger(SendMessageCommonServiceImpl.class);
  @Autowired
  private SmsConifigMapper smsConifigMapper;
  @Autowired
  private YimeiSendMessageService yimeiSendMessageService;
  @Autowired
  private DhstSendMessageService dhstSendMessageService;
  @Autowired
  private ClSendMessageService clSendMessageService;
  @Autowired
  private RedisTemplate<String,String> redisTemplate;
  @Autowired
  private CommonMessageErrorRepos commonMessageErrorRepos;
  
  private final String redisKey = "sms_common_redis:sendMessage";
  
  public boolean commonSendMessage(String phone, String msg) {
    try {
      boolean bo = false;
      String config = redisTemplate.opsForHash().get("system:sms", "1").toString();
      int id = Integer.parseInt(config);
      String seqid = String.valueOf(System.currentTimeMillis());
      String sign = redisTemplate.opsForHash().get("system:sms", "3").toString();
      SmsConfig smsConfig = smsConifigMapper.querySmsConfig(Integer.valueOf(id));
      if (smsConfig != null) {
        try  {
          if (smsConfig.getChenal() == 1) {
            bo = yimeiSendMessageService.sendMsg(seqid, phone, msg);
            if ((smsConfig.getChenal() == 2) && (smsConfig.getType() == 1)) {
              bo = dhstSendMessageService.sendMsg(seqid, phone, msg, sign);
            }
            if ((smsConfig.getChenal() == 3) && (smsConfig.getType() == 1)) {
              bo = clSendMessageService.sendMsg(seqid, phone, sign + msg);
            }
          }
          if (bo) {
            addRedis(phone, msg, seqid, String.valueOf(smsConfig.getChenal()), "1");
            return true;
          }
        }
        catch (Exception e) {
          logger.error("redis保存已发短信异常：", e);
          return false;
        }
      }
    }
    catch (Exception e) {
      logger.error("commonSendMessage方法异常：", e);
      return false;
    }
    return false;
  }
  
  /**
   * 发送语音短信
   */
  public boolean commonSendMessageVoice(String phones, String msg) {
    try {
      boolean bo = false;      
      String config = redisTemplate.opsForHash().get("system:sms", "2").toString();
      int id = Integer.parseInt(config);
      String seqid = String.valueOf(System.currentTimeMillis());
      SmsConfig smsConfig = smsConifigMapper.querySmsConfig(Integer.valueOf(id));
      if (smsConfig != null) {
        try {
          if (smsConfig.getChenal() == 1) {
            bo = yimeiSendMessageService.sendVoiceMsg(seqid, phones, msg);
          }
          if (smsConfig.getChenal() == 2) {
            bo = dhstSendMessageService.sendVoiceSms(seqid, phones, msg);
          }
          if (smsConfig.getChenal() == 3) {
            bo = clSendMessageService.sendMsgVoice(seqid, phones, msg);
          }
        }
        catch (Exception e) {
          logger.error("发送语音短信异常：", e);
          return false;
        }
        try {
          if (bo) {
            addRedis(phones, msg, seqid, String.valueOf(smsConfig.getChenal()), "2");
            return true;
          }
        }
        catch (Exception e) {
          logger.error("redis保存已发语音短信异常：", e);
          return true;
        }
      }
    }
    catch (Exception e) {
      logger.error("commonSendMessageVoice方法异常：", e);
      return false;
    }
    return false;
    }
  
  /**
   * 通过大汉三通发送短信。
   */
  public boolean dhstSendMessage(String phones, String msg) {
    try {
      boolean bo = false;
      
      String config = redisTemplate.opsForHash().get("system:sms", "4").toString();

      int id = Integer.parseInt(config);
      
      String seqid = String.valueOf(System.currentTimeMillis());
      SmsConfig smsConfig = smsConifigMapper.querySmsConfig(Integer.valueOf(id));
      String sign = redisTemplate.opsForHash().get("system:sms", "3").toString();
      
      msg = msg.replace("水象借点花", sign.replace("【", "").replace("】", "")).replace("【水象分期】", "").replace("【", "").replace("】", "").replace(" ", "");
      if (smsConfig != null) {
        try {
          if (smsConfig.getId() == 1) {
            bo = yimeiSendMessageService.sendMsg(seqid, phones, sign + msg);
          }
          if (smsConfig.getId() == 2) {
            bo = dhstSendMessageService.sendMsg(seqid, phones, msg, sign);
          }
          if (smsConfig.getId() == 5) {
            bo = clSendMessageService.sendMsg(seqid, phones, sign + msg);
          }
        }
        catch (Exception e) {
          logger.error("dhst发送短信异常：", e);
          return false;
        }
        try {
          if ((bo) && (smsConfig.getId() == 1)) {
            addRedis(phones, smsConfig.getSign() + msg, seqid, 
              String.valueOf(smsConfig.getChenal()), "1");
            return true;
          }
          if ((bo) && (smsConfig.getId() == 2)) {
            addRedis(phones, msg, seqid, String.valueOf(smsConfig.getChenal()), "1");
            return true;
          }
          if ((bo) && (smsConfig.getId() == 5)) {
            addRedis(phones, smsConfig.getSign() + msg, seqid, 
              String.valueOf(smsConfig.getChenal()), "1");
            return true;
          }
        }
        catch (Exception e) {
          logger.error("redis保存已发短信异常：", e);
          return true;
        }
      }
    }
    catch (Exception e) {
      logger.error("dhstSendMessage方法异常：", e);
      return false;
    }
    return false;
  }
  
	  public void addRedis(String phones, String msg, String seqid, String chenal, String type) {
	    Map<String, String> map = new HashMap<>();
	    map.put("phone", phones);
	    map.put("msg", msg);
	    map.put("seqid", seqid);
	    map.put("chenal", chenal);
	    map.put("type", type);
	    String value = JSONObject.toJSONString(map);
	    redisTemplate.opsForList().leftPush(redisKey, value);
  }
  
	  public void saveErrorInfo(String phone, String msg, int chenal, int type) {
	    CommonMessageError error = new CommonMessageError();
	    error.setId(UUID.randomUUID().toString().replace("-", ""));
	    error.setChenal(chenal);
	    error.setMsg(msg);
	    error.setPhone(phone);
	    error.setType(type);
	    commonMessageErrorRepos.save(error);
	  }
}
