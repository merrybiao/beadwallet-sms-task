package com.waterelephant.sms.service.lianlu.impl;

import com.alibaba.fastjson.JSON;
import com.waterelephant.sms.entity.lianlu.BwLianluMessageInfo;
import com.waterelephant.sms.enumData.LianluEnum;
import com.waterelephant.sms.exception.LianluException;
import com.waterelephant.sms.service.lianlu.LianLuSmsSendService;
import com.waterelephant.sms.service.lianlu.LianluSmsService;
import com.waterelephant.sms.utils.CommUtils;
import java.util.Date;
import java.util.Map;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

@Service
public class LianluSmsServiceImpl implements LianluSmsService {
	
  private Logger logger = LoggerFactory.getLogger(LianluSmsServiceImpl.class);
  
  @Autowired
  private LianLuSmsSendService lianLuSmsSendServiceImpl;
  
  @Autowired
  private RedisTemplate<String,String> redisTemplate;
  
  public String sendMsg(String mobile, String content, String sign, Integer count) throws LianluException {
	  Map<String, String> respmap = null;
     try {
    	 respmap = lianLuSmsSendServiceImpl.sendSms(mobile, sign + content);
    	 if (!CommUtils.isNull(respmap)) {
    	      String requestCode = respmap.get("requestCode");
    	      if ("200".equals(requestCode)) {
    	        BwLianluMessageInfo info = new BwLianluMessageInfo();
    	        info.setTaskid(respmap.get("taskid"));
    	        info.setContent(content);
    	        info.setSign(sign);
    	        info.setCreateTime(new Date());
    	        info.setMobile(mobile);
    	        info.setMsgid(UUID.randomUUID().toString().replace("-", ""));
    	        info.setCount(count);
    	        info.setErrorcode("");
    	        info.setReceivetime("");
    	        info.setStatus("");
    	        redisTemplate.opsForList().leftPush("sms_lianlu_redis:sendMessage", JSON.toJSONString(info));
    	        return "success";
    	      }
    	      throw LianluEnum.CODE_4040.getErrorInstence();
    	    }
	} catch (Exception e) {
		 throw LianluEnum.CODE_3030.getErrorInstence();
	}
   
    throw LianluEnum.CODE_5050.getErrorInstence();
  }
  
  public Map<String, String> getReport() throws LianluException {
    return lianLuSmsSendServiceImpl.getRerort();
  }
}
