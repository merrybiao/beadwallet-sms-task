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
import com.waterelephant.sms.service.es.YiMeiEsService;
import com.waterelephant.sms.utils.CommUtils;

//@Component
public class SxypTransferJob {
  private Logger logger = LoggerFactory.getLogger(SxypTransferJob.class);
  public static ExecutorService executor = Executors.newFixedThreadPool(30);
  private AtomicBoolean flag = new AtomicBoolean(false);
  @Autowired
  private RedisTemplate<String,Object> redisTemplate;
  @Autowired
  private YiMeiEsService yiMeiEsServiceImpl;
  public final String SYXP_REDIS_READ_SEDMSG = "sms_sxyp_redis:sendMessage";
  
  //@Scheduled(cron="0/3 * * * * ?")
  public void saveMessageToMongDB() {
    if (flag.compareAndSet(false, true)) {
      try {
        for (int i = 0; i < 5; i++) {
          executor.execute(new Runnable() {
            public void run() {
              Object value = "";
              try {
                if (redisTemplate.hasKey(SYXP_REDIS_READ_SEDMSG).booleanValue()) {
                  value = redisTemplate.opsForList().leftPop(SYXP_REDIS_READ_SEDMSG);
                  if (!CommUtils.isNull(value)) {
                    logger.info("水象优品亿美从ES转移数据至redis中取出的值为:" + value);
                    JSONObject json = JSONObject.parseObject(value.toString());
                    yiMeiEsServiceImpl.saveInfo(json.getString("phone"), json.getString("msg"), json.getString("seqid"), json.getIntValue("chenal"), json.getIntValue("type"));
                  }
                }
              }
              catch (Exception e) {
                logger.error("水象优品保存短信到数据库异常：{}", e.getMessage());
                e.printStackTrace();
              }
            }
          });
        }
      }
      catch (Exception e) {
    	logger.error("水象优品转移数据线程异常：,异常信息为{}", e.getMessage());
        e.printStackTrace();
      }
      finally {
        flag.getAndSet(false);
      }
    }
  }
}
