package com.waterelephant.sms.job.sendJob;

import com.alibaba.fastjson.JSON;
import com.waterelephant.sms.entity.yimei.MessageDto;
import com.waterelephant.sms.service.common.SendMessageCommonService;
import com.waterelephant.sms.utils.CommUtils;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;
import javax.annotation.Resource;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ListOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
@SuppressWarnings("all")
public class SendCommonMsgJob {
  private Logger logger = LoggerFactory.getLogger(SendCommonMsgJob.class);
  private static AtomicBoolean sendMessageFlag = new AtomicBoolean(false);
  private static AtomicBoolean sendMessageFlag2 = new AtomicBoolean(false);
  private static AtomicBoolean sendMessageFlag3 = new AtomicBoolean(false);
  @Resource
  private SendMessageCommonService sendMessageCommonService;
  @Autowired
  private RedisTemplate redisTemplate;
  public static ExecutorService executorService = Executors.newFixedThreadPool(30);
  public static ExecutorService executorService2 = Executors.newFixedThreadPool(30);
  public static ExecutorService executorService3 = Executors.newFixedThreadPool(30);
  private final String COMMOM_SEND_REDIS_KEY = "system:sendMessage";
  private final String GAME_SEND_REDIS_KEY = "system:game_sendMessage";
  private final String CUTPAY_SEND_REDIS_KEY = "system:cutPaySendMessage";
  
  /**
   * 发送redis的key为system:sendMessage的短信，一般为通用短信key
   */
  //@Scheduled(cron="0/3 * * * * ?")
  public void sendMessage() {
    logger.info("【亿美信息】短信开始发送===》 ");
    if (sendMessageFlag.compareAndSet(false, true)) {
      for (int i = 0; i < 3; i++) {
        Thread task = new Thread(new Runnable() {          
		public void run() {
            MessageDto messageDto = null;
            try {
              Object value = null;
              if (redisTemplate.hasKey(COMMOM_SEND_REDIS_KEY).booleanValue()) {
                synchronized (this) {
                  value = redisTemplate.opsForList().leftPop(COMMOM_SEND_REDIS_KEY);
                }
                if (!CommUtils.isNull(value)) {
                  logger.info("短信发送===》" + value);
                  messageDto = JSON.parseObject(value.toString(), MessageDto.class);
                  boolean bo = false;
                  int count = messageDto.getInviteCount() == null ? 1 : messageDto.getInviteCount().intValue();
                  messageDto.setInviteCount(count);
                  if (count < 4) {
                      if ("1".equals(messageDto.getType())) {
                        bo = sendMessageCommonService.commonSendMessage(messageDto.getPhone(), messageDto.getMsg());
                      } else if ("2".equals(messageDto.getType())) {
                        bo = sendMessageCommonService.commonSendMessageVoice(messageDto.getPhone(), messageDto.getMsg());
                      }
                      if (!bo) {
                        if (count < 3) {
                          logger.info("短信发送失败重新放入redis");
                          messageDto.setInviteCount(messageDto.getInviteCount()+1);
                          redisTemplate.opsForList().rightPush(COMMOM_SEND_REDIS_KEY, JSON.toJSONString(messageDto));
                        }
                        else {
                          sendMessageCommonService.saveErrorInfo(messageDto.getPhone(), messageDto.getMsg(), 0, Integer.parseInt(messageDto.getType()));
                          logger.info("~~~~~~~~~~~~~~手机号{}，短信内容，{}，未发出，保存数据成功~~~~~~~~~~~~~~",messageDto.getPhone(),messageDto.getMsg());
                        }
                      }
                    }
                }
              }
            }
            catch (Exception e) {
              logger.error("短信发送失败,程序发生异常，信息为{}", e.getMessage());
              if (messageDto != null) {
                String phone = messageDto.getPhone();
                String msg = messageDto.getMsg();
                int type = Integer.parseInt(messageDto.getType());
                int chenal = 0;
                sendMessageCommonService.saveErrorInfo(phone, msg, chenal, type);
                logger.error("~~~~~~~~~~~~~~手机号{}，短信内容，{}，未发出，保存数据成功~~~~~~~~~~~~~~",phone,msg);
                e.printStackTrace();
              }
            }
            finally {
              sendMessageFlag.set(false);
            }
          }
        });
        executorService.execute(task);
      }
    }
  }
  
  /**
   * 发送redis的key为system:game_sendMessage的短信，一般用作游戏类的短信发送
   */
  //@Scheduled(cron="2/3 * * * * ?")
  public void sendGameMessage() {
    logger.info("【亿美游戏信息】短信开始发送===》");
    if (sendMessageFlag2.compareAndSet(false, true)) {
      for (int i = 0; i < 1; i++) {
        Thread task = new Thread(new Runnable() {
          public void run() {
            MessageDto messageDto = null;
            try {
              String value = null;
              if (redisTemplate.hasKey(GAME_SEND_REDIS_KEY).booleanValue()) {
                synchronized (this) {
                  value = redisTemplate.opsForList().leftPop(GAME_SEND_REDIS_KEY).toString();
                }
                if (!CommUtils.isNull(value)) {
                  logger.info("游戏短信发送===》" + value);
                  messageDto = JSON.parseObject(value, MessageDto.class);
                  if (!CommUtils.isNull(messageDto.getPhone()) && !CommUtils.isNull(messageDto.getMsg()) && !CommUtils.isNull(messageDto.getType())) {
                    boolean bo = false;
                    int count = messageDto.getInviteCount() == null ? 0 : messageDto.getInviteCount().intValue();
                    count++;
                    if (count < 4) {
                      if ("1".equals(messageDto.getType())) {
                        bo = sendMessageCommonService.commonSendMessage(messageDto.getPhone(), messageDto.getMsg());
                      } else if ("2".equals(messageDto.getType())) {
                        bo = sendMessageCommonService.commonSendMessageVoice(messageDto.getPhone(), messageDto.getMsg());
                      }
                      if (!bo) {
                        if (count < 3) {
                          logger.info("游戏短信发送失败重新放入redis");
                          messageDto.setInviteCount(messageDto.getInviteCount()+1);
                          redisTemplate.opsForList().rightPush(GAME_SEND_REDIS_KEY, JSON.toJSONString(messageDto));
                        }
                        else {
                          sendMessageCommonService.saveErrorInfo(messageDto.getPhone(), messageDto.getMsg(), 0, Integer.parseInt(messageDto.getType()));
                          logger.info(messageDto.getPhone() + "~~~~~~~~~~~~~~保存数据成功~~~~~~~~~~~~~~");
                        }
                      }
                    }
                  }
                }
              }
            }
            catch (Exception e) {
              logger.error("游戏短信发送失败,程序发生异常，信息为{}", e.getMessage());
              if (messageDto != null) {
                String phone = messageDto.getPhone();
                String msg = messageDto.getMsg();
                int type = Integer.parseInt(messageDto.getType());
                int chenal = 0;
                sendMessageCommonService.saveErrorInfo(phone, msg, chenal, type);
                logger.info("~~~~~~~~~~~~~~游戏保存数据成功~~~~~~~~~~~~~~");
                e.printStackTrace();
              }
            }
            finally {
              sendMessageFlag2.set(false);
            }
          }
        });
        executorService2.execute(task);
      }
    }
  }
  
  /**
   * 发送Redis的key为system:cutPaySendMessage的短信，用于还款提醒类的短信
   */
  //@Scheduled(cron="3/3 * * * * ?")
  public void sendCutPayMessage() {
    logger.info("【审核信息】短信开始发送===》");
    if (sendMessageFlag3.compareAndSet(false, true)) {
      for (int i = 0; i < 1; i++) {
        Thread task = new Thread(new Runnable() {
          public void run() {
            MessageDto messageDto = null;
            try {
              String value = null;
              if (redisTemplate.hasKey(CUTPAY_SEND_REDIS_KEY).booleanValue()) {
                synchronized (this) {
                  value = redisTemplate.opsForList().leftPop(CUTPAY_SEND_REDIS_KEY).toString();
                }
                if (!CommUtils.isNull(value)) {
                  logger.info("审核短信发送===》" + value);
                  messageDto = (MessageDto)JSON.parseObject(value, MessageDto.class);
                  if ((messageDto != null) && (!CommUtils.isNull(messageDto.getPhone())) && 
                    (!CommUtils.isNull(messageDto.getMsg())) &&  (!CommUtils.isNull(messageDto.getType()))) {
                    boolean bo = false;
                    int count = messageDto.getInviteCount() == null ? 0 : messageDto.getInviteCount().intValue();
                    count++;
                    if (count < 4) {
                      if ("1".equals(messageDto.getType())) {
                        bo = sendMessageCommonService.commonSendMessage(messageDto.getPhone(), messageDto.getMsg());
                      } else if ("2".equals(messageDto.getType())) {
                        bo = sendMessageCommonService.commonSendMessageVoice(messageDto.getPhone(), messageDto.getMsg());
                      }
                      if (!bo) {
                        if (count < 3) {
                          logger.info("审核信发送失败重新放入redis");
                          messageDto.setInviteCount(Integer.valueOf(count));
                          redisTemplate.opsForList().rightPush(CUTPAY_SEND_REDIS_KEY, JSON.toJSONString(messageDto));
                        }
                        else {
                          sendMessageCommonService.saveErrorInfo(messageDto.getPhone(), messageDto.getMsg(), 0, Integer.parseInt(messageDto.getType()));
                          logger.info(messageDto.getPhone() + "~~~~~~~~~~~~~~审核保存数据成功~~~~~~~~~~~~~~");
                        }
                      }
                    }
                  }
                }
              }
            }
            catch (Exception e) {
              logger.error("审核短信发送失败,程序发生异常，信息为{}", e.getMessage());
              if (messageDto != null) {
                String phone = messageDto.getPhone();
                String msg = messageDto.getMsg();
                int type = Integer.parseInt(messageDto.getType());
                int chenal = 0;
                sendMessageCommonService.saveErrorInfo(phone, msg, chenal, type);
                logger.info("~~~~~~~~~~~~~~审核保存数据成功~~~~~~~~~~~~~~");
                e.printStackTrace();
              }
            }
            finally {
              sendMessageFlag3.set(false);
            }
          }
        });
        executorService3.execute(task);
      }
    }
  }
}
