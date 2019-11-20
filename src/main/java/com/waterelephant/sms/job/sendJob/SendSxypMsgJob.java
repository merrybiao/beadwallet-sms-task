package com.waterelephant.sms.job.sendJob;

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
import com.waterelephant.sms.entity.yimei.MessageDto;
import com.waterelephant.sms.service.common.SendMessageCommonService;
import com.waterelephant.sms.service.sxyp.SxypSmsService;
import com.waterelephant.sms.utils.CommUtils;

//@Component
public class SendSxypMsgJob {
  private Logger logger = LoggerFactory.getLogger(SendSxypMsgJob.class);
  public static ExecutorService executorSendMsg = Executors.newFixedThreadPool(30);
  private AtomicBoolean flag1 = new AtomicBoolean(false);
  @Autowired
  private SxypSmsService SxypSmsServiceImpl;
  @Autowired
  private RedisTemplate<String,String> redisTemplate;
  @Autowired
  private SendMessageCommonService sendMessageCommonServiceImpl;
  private final String SXYP_SEND_REDIS_KEY = "system:sxyp_sendMessage";
  
  //@Scheduled(cron="2/3 * * * * ?")
  public void sendMessage(){
    logger.info("【水象优品】短信开始发送===》");
    if (flag1.compareAndSet(false, true)) {
      try {
        for (int i = 0; i < 2; i++) {
          Thread task = new Thread(new Runnable() {
            public void run() {
              Object value = null;
              MessageDto messageDto = null;
              try {
                if (redisTemplate.hasKey(SXYP_SEND_REDIS_KEY).booleanValue()) {
                  synchronized (this) {
                    value = redisTemplate.opsForList().leftPop(SXYP_SEND_REDIS_KEY);
                  }
                  if (!CommUtils.isNull(value)) {
                    logger.info("【水象优品】短信发送===》" + value);
                    messageDto = JSON.parseObject(value.toString(), MessageDto.class);
                    if (!CommUtils.isNull(messageDto.getPhone()) && !CommUtils.isNull(messageDto.getMsg()) && !CommUtils.isNull(messageDto.getType())) {
                      boolean bo = false;
                      int count = messageDto.getInviteCount() == null ? 1 : messageDto.getInviteCount().intValue();
                      messageDto.setInviteCount(count);
                      if (count < 4) {
                        bo = SxypSmsServiceImpl.sendMessage(messageDto.getPhone(), messageDto.getMsg(), "system:sxypSing");
                        if (!bo) {
                          if (count < 3) {
                            logger.info("短信发送失败重新放入redis");
                            messageDto.setInviteCount(messageDto.getInviteCount()+1);
                            redisTemplate.opsForList().rightPush(SXYP_SEND_REDIS_KEY, JSON.toJSONString(messageDto));
                          } else {
                            String phone = messageDto.getPhone();
                            String msg = messageDto.getMsg();
                            int type = Integer.parseInt(messageDto.getType());
                            int chenal = 0;
                            sendMessageCommonServiceImpl.saveErrorInfo(phone, msg, chenal, type);
                            logger.info("~~~~~~~~~~~~~~手机号{}，短信内容，{}，未发出，保存数据成功~~~~~~~~~~~~~~",messageDto.getPhone(),messageDto.getMsg());
                          }
                        }
                      }
                    }
                  }
                }
              }
              catch (Exception e) {
                logger.error("短信发送失败,异常信息为{}", e.getMessage());
                if (messageDto != null) {
                  String phone = messageDto.getPhone();
                  String msg = messageDto.getMsg();
                  int type = Integer.parseInt(messageDto.getType());
                  int chenal = 1;
                  sendMessageCommonServiceImpl.saveErrorInfo(phone, msg, chenal, type);
                  logger.error("~~~~~~~~~~~~~~手机号{}，短信内容，{}，未发出，保存数据成功~~~~~~~~~~~~~~",phone,msg);
                }
              }
            }
          });
          executorSendMsg.execute(task);
        }
      }
      catch (Exception e) {
        logger.error("短信线程异常", e);
      }
      finally {
        flag1.set(false);
      }
    }
  }
}
