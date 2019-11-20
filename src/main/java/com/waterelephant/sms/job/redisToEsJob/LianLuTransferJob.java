package com.waterelephant.sms.job.redisToEsJob;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONObject;
import com.waterelephant.sms.service.es.LianLuEsService;
import com.waterelephant.sms.utils.CommUtils;

//@Component
public class LianLuTransferJob {
  private Logger logger = LoggerFactory.getLogger(LianLuTransferJob.class);
  private static AtomicBoolean sendMessageFlag = new AtomicBoolean(false);
  private static ExecutorService executorService = Executors.newFixedThreadPool(30);
  @Autowired
  private RedisTemplate<String,Object> redisTemplate;
  @Autowired
  private LianLuEsService lianLuEsServiceImpl;
  private final String LAINLU_TRANSFER_REDIS_KEY = "sms_lianlu_redis:sendMessage";
  
  //@Scheduled(cron="1/3 * * * * ?")
  public void transferRedisToEs() {
    if (sendMessageFlag.compareAndSet(false, true)) {
      try {
        for (int i = 0; i < 3; i++) {
          executorService.execute(new Runnable() {
            public void run() {
              Object redisValue = "";
              try {
                if (redisTemplate.hasKey(LAINLU_TRANSFER_REDIS_KEY).booleanValue()) {
                  redisValue = redisTemplate.opsForList().leftPop(LAINLU_TRANSFER_REDIS_KEY);
                  if (!CommUtils.isNull(redisValue)) {
                    logger.info("联麓从ES转移数据至redis中取出的值为:" + redisValue);
                    JSONObject json = JSONObject.parseObject(redisValue.toString());
                    lianLuEsServiceImpl.saveInfo(json.getString("mobile"), json.getString("content"), json.getString("sign"), json
                      .getInteger("count"), json.getString("taskid"));
                  }
                }
              }
              catch (Exception e) {
                logger.error("亿美从ES转移数据至redis异常，异常信息为{}：", e.getMessage());
                e.printStackTrace();
              }
            }
          });
        }
      }
      catch (Exception e) {
        logger.error("联麓转移数据线程异常：,异常信息为{}", e.getMessage());
        e.printStackTrace();
      }
      finally {
        sendMessageFlag.set(false);
      }
    }
  }
}
