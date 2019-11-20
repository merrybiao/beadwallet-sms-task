package com.waterelephant.sms.job.redisToEsJob;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;

import com.alibaba.fastjson.JSONObject;
import com.waterelephant.sms.service.es.YiMeiEsService;
import com.waterelephant.sms.utils.CommUtils;

//@Component
public class ShoppingMallTransferJob {
  private Logger logger = LoggerFactory.getLogger(ShoppingMallTransferJob.class);
  private static AtomicBoolean sendMessageFlag = new AtomicBoolean(false);
  private static ExecutorService executorService = Executors.newFixedThreadPool(30);
  @Autowired
  private RedisTemplate<String,Object> redisTemplate;
  @Autowired
  private YiMeiEsService yiMeiEsServiceImpl;
  private final String SHOPMALL_TRANSFER_REDIS_KEY = "sms_shoppingMall_redis:sendMessage";
  
  //@Scheduled(cron="2/3 * * * * ?")
  public void transferRedisToEs() {
    if (sendMessageFlag.compareAndSet(false, true)) {
      try {
        for (int i = 0; i < 3; i++) {
          executorService.execute(new Runnable() {
            public void run() {
              Object redisValue = "";
              try {
                if (redisTemplate.hasKey(SHOPMALL_TRANSFER_REDIS_KEY).booleanValue()) {
                  redisValue = redisTemplate.opsForList().leftPop(SHOPMALL_TRANSFER_REDIS_KEY);
                  if (!CommUtils.isNull(redisValue)) {
                    logger.info("商城亿美从ES转移数据至redis中取出的值为:" + redisValue);
                    JSONObject json = JSONObject.parseObject(redisValue.toString());
                    yiMeiEsServiceImpl.saveInfo(json.getString("phone"), json
                      .getString("msg"), json.getString("seqid"), json.getInteger("chenal").intValue(), json.getInteger("type").intValue());
                  }
                }
              }
              catch (Exception e) {
                logger.error("商城亿美从ES转移数据至redis异常，异常信息为{}：", e.getMessage());
                e.printStackTrace();
              }
            }
          });
        }
      }
      catch (Exception e) {
        logger.error("商城转移数据线程异常：,异常信息为{}", e.getMessage());
        e.printStackTrace();
      }
      finally {
        sendMessageFlag.set(false);
      }
    }
  }
}
