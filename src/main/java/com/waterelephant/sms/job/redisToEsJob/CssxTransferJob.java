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

/**
 * 没有用到，暂时停用
 * @author Administrator
 *
 */
//@Component
public class CssxTransferJob
{
  private Logger logger = LoggerFactory.getLogger(CssxTransferJob.class);
  private AtomicBoolean saveMessage = new AtomicBoolean(false);
  public static ExecutorService executorService = Executors.newFixedThreadPool(30);
  @Autowired
  private RedisTemplate<String,String> redisTemplate;
  @Autowired
  private YiMeiEsService yiMeiEsServiceImpl;
  
  private final String CSSX_TRANSFER_REDIS_KEY = "sms_cssx_redis:sendMessage";
  
  //@Scheduled(cron="0/3 * * * * ?")
  public void saveMessageToMongDB(){
    if (saveMessage.compareAndSet(false, true)) {
      try {
        for (int i = 0; i < 5; i++) {
          executorService.execute(new Runnable() {
            public void run() {
              Object value = "";
              try {
                if (redisTemplate.hasKey(CSSX_TRANSFER_REDIS_KEY).booleanValue()) {
                  value = redisTemplate.opsForList().leftPop(CSSX_TRANSFER_REDIS_KEY);
                  if (!CommUtils.isNull(value)) {
                    logger.info("长沙水象从ES转移数据至redis中取出的值为:" + value);
                    JSONObject json = JSONObject.parseObject(value.toString());
                    yiMeiEsServiceImpl.saveInfo(json.getString("phone"), json.getString("msg"), json
                      .getString("seqid"), json.getInteger("chenal").intValue(), json
                      .getInteger("type").intValue());
                  }
                }
              }
              catch (Exception e) {
                logger.error("长沙水象保存短信到数据库异常：，异常信息为{}", e.getMessage());
                e.printStackTrace();
              }
            }
          });
        }
      }
      catch (Exception e) {
        logger.info("长沙水象线程异常：，异常信息为{}", e.getMessage());
        e.printStackTrace();
      }
    }
    saveMessage.set(false);
  }
}
