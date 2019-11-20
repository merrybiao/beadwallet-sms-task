package com.waterelephant.sms.job.sendJob;

import java.util.Date;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.waterelephant.sms.entity.tryun.BwTryunSendMessageInfo;
import com.waterelephant.sms.service.es.TyrunEsService;
import com.waterelephant.sms.service.tryun.TryunSmsService;
import com.waterelephant.sms.utils.CommUtils;
import com.waterelephant.sms.utils.DateUtil;

@Component
public class SendTryunMsgJob {
	
	  private Logger logger = LoggerFactory.getLogger(SendTryunMsgJob.class);
	  
	  private AtomicBoolean flag = new AtomicBoolean(false);
	  
	  private static ExecutorService executorService = Executors.newFixedThreadPool(10);	  
	  
	  @Autowired
	  private RedisTemplate<String,String> redisTemplate;
	  
	  @Autowired
	  private TryunSmsService tryunSmsServiceImpl;
	  
	  @Autowired
	  private TyrunEsService tyrunEsServiceImpl;
	  
	  
	  private final String TRYUN_SEND_REDIS_KEY = "system:tryun_sendMessage";

	  //@Scheduled(cron="1/3 * * * * ?")
	  private void sendMsgJob()  {
	    if (flag.compareAndSet(false, true)) {
	      try {
	    	logger.info("【天瑞云信息】短信开始发送===》");
	        for (int i = 0; i < 3; i++)  {
	          Thread task = new Thread(new Runnable() {
	            public void run() {
	              Object redisSms = redisTemplate.opsForList().rightPop(TRYUN_SEND_REDIS_KEY);
	              if (!CommUtils.isNull(redisSms)) {
	                logger.info("redis取出的值为：" + redisSms);
	                BwTryunSendMessageInfo bwtry = JSONObject.toJavaObject(JSONObject.parseObject(redisSms.toString()), BwTryunSendMessageInfo.class);
	                Integer count = Integer.valueOf(CommUtils.isNull(bwtry.getCount()) ? 1 : bwtry.getCount().intValue());
	                bwtry.setCount(count);
	                	try {
		                	boolean ifsuccess = tryunSmsServiceImpl.sendMessage(bwtry.getMobile(), bwtry.getSign(), bwtry.getContent(),bwtry.getCount(),
		                			bwtry.getUseTemplate(),bwtry.getTemplateNo(),bwtry.getNoticeOrMarketing());
		                    if(ifsuccess) {	          
		                    	logger.info("【天瑞云信息】执行手机号码：{}，短信内容：{}，发送成功！！", bwtry.getMobile(), bwtry.getContent());	
		                    }
		                    else {
		                    	if(bwtry.getCount() <4) {
			                    	 	logger.info("【天瑞云信息】执行短信内容：{}，发送失败！！进行count+1", JSON.toJSONString(bwtry));	
				                    	bwtry.setCount(bwtry.getCount()+1);
				                    	redisTemplate.opsForList().rightPush(TRYUN_SEND_REDIS_KEY, JSON.toJSONString(bwtry));	              
		                    	} else {
		                    		   logger.error("【天瑞云信息】发送手机号码{}短信出现第{}次失败,达到规定次数，不再发送，存储ES", bwtry.getMobile(), bwtry.getCount());
			   		                   bwtry.setSendTime(new Date().getTime());
			   		                   bwtry.setCreateTime(DateUtil.getDateString(new Date()));
			   		                   bwtry.setId(UUID.randomUUID().toString().replace("-", ""));
			   		                   tyrunEsServiceImpl.saveTryunSmsErrorInfo(bwtry);
		                    	}		                        
		                    }
		                }catch (Exception e) {
		                  Integer num = bwtry.getCount();
		                  if (num.intValue() < 4) {
		                    num = Integer.valueOf(num.intValue() + 1);
		                    bwtry.setCount(num);
		                    redisTemplate.opsForList().rightPush(TRYUN_SEND_REDIS_KEY, JSON.toJSONString(bwtry));
		                    logger.error("【天瑞云信息】发送手机号码{}短信出现第{}次失败，执行第{}次重发！！,异常信息为：{}", new Object[] { bwtry.getMobile(), Integer.valueOf(num.intValue() - 1), num, e.getMessage()});
		                  }
		                  else {
		                    logger.error("【天瑞云信息】发送手机号码{}短信出现第{}次失败,达到规定次数，不再发送，存储ES", bwtry.getMobile(), num);
		                    bwtry.setSendTime(new Date().getTime());
	   		                bwtry.setCreateTime(DateUtil.getDateString(new Date()));
	   		                bwtry.setId(UUID.randomUUID().toString().replace("-", ""));
		                    tyrunEsServiceImpl.saveTryunSmsErrorInfo(bwtry);
		                  }
		                }	                
	                 }
	              }
	          });
	          executorService.execute(task);
	        }
	      } catch (Exception e) {
	       logger.error("【天瑞云信息】发送短信出现异常，异常信息为：", e.getMessage());
	        e.printStackTrace();
	      } finally {
	       flag.getAndSet(false);
	      }
	   }
	}
}
