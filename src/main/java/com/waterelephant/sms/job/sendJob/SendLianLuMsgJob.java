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
import com.waterelephant.sms.entity.lianlu.BwLianluMessageError;
import com.waterelephant.sms.exception.LianluException;
import com.waterelephant.sms.service.es.LianLuEsService;
import com.waterelephant.sms.service.lianlu.LianluSmsService;
import com.waterelephant.sms.utils.CommUtils;

//@Component
public class SendLianLuMsgJob {
  private Logger logger = LoggerFactory.getLogger(SendLianLuMsgJob.class);
  private AtomicBoolean flag = new AtomicBoolean(false);
  private static ExecutorService executorService = Executors.newFixedThreadPool(10);
  @Autowired
  private LianluSmsService lianluSmsServiceImpl;
  @Autowired
  private RedisTemplate<String,String> redisTemplate;
  @Autowired
  private LianLuEsService lianLuEsServiceImpl;
  
  private final String LINALU_SEND_REDIS_KEY = "system:lianlu_sendMessage";
  
  //@Scheduled(cron="2/3 * * * * ?")
  private void sendMsgJob()  {
    if (flag.compareAndSet(false, true)) {
      try {
    	logger.info("【联麓信息】短信开始发送===>");
        for (int i = 0; i < 3; i++)  {
          Thread task = new Thread(new Runnable() {
            public void run() {
              Object redisSms = redisTemplate.opsForList().rightPop(LINALU_SEND_REDIS_KEY);
              if (!CommUtils.isNull(redisSms)) {
                logger.info("redis取出的值为：" + redisSms);
                BwLianluMessageError bwsms = JSONObject.parseObject(redisSms.toString(), BwLianluMessageError.class);
                Integer count = Integer.valueOf(CommUtils.isNull(bwsms.getCount()) ? 1 : bwsms.getCount().intValue());
                bwsms.setCount(count);
                try {
                  lianluSmsServiceImpl.sendMsg(bwsms.getMobile(), bwsms.getContent(), bwsms.getSign(), bwsms.getCount());
                  logger.info("【联麓信息】执行手机号码：{}，短信内容：{}，发送成功！！", bwsms.getMobile(), bwsms.getContent());
                } catch (LianluException e) {
                  Integer num = bwsms.getCount();
                  if (num.intValue() < 4) {
                    num = Integer.valueOf(num.intValue() + 1);
                    bwsms.setCount(num);
                    redisTemplate.opsForList().rightPush(LINALU_SEND_REDIS_KEY, JSON.toJSONString(bwsms));
                    logger.error("【联麓信息】发送手机号码{}短信出现第{}次失败，执行第{}次重发！！,异常信息编码为：{}，异常信息为：{}", new Object[] { bwsms.getMobile(), Integer.valueOf(num.intValue() - 1), num, e.getCode(), e.getMessage() });
                  }
                  else {
                    logger.error("【联麓信息】发送手机号码{}短信出现第{}次失败,达到规定次数，不再发送，存储ES", bwsms.getMobile(), num);
                    bwsms.setCreateTime(new Date());
                    bwsms.setId(UUID.randomUUID().toString().replace("-", ""));
                    lianLuEsServiceImpl.saveErrorInfo(bwsms);
                  }
                }
              }
            }
          });
          executorService.execute(task);
        }
      }
      catch (Exception e) {
       logger.error("【联麓信息】发送短信出现异常，异常信息为：", e.getMessage());
        e.printStackTrace();
      }
      finally {
       flag.getAndSet(false);
      }
    }
  }
}
