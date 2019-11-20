package com.waterelephant.sms.job.sendJob;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import com.alibaba.fastjson.JSON;
import com.waterelephant.sms.entity.yimei.MessageDto;
import com.waterelephant.sms.service.common.SendMessageCommonService;
import com.waterelephant.sms.service.xmjb.XmjbSmsService;
import com.waterelephant.sms.utils.CommUtils;

/**
 * 没有用到，暂时停用
 * @author Administrator
 */
//@Component
public class SendXmjbMsgJob {
  private Logger logger = LoggerFactory.getLogger(SendCssxMsgJob.class);
  private AtomicBoolean sendflag = new AtomicBoolean(false);
  @Autowired
  private XmjbSmsService xmjbSmsServiceImpl;
  @Autowired
  private RedisTemplate<String,String> redisTemplate;
  @Autowired
  private ThreadPoolTaskExecutor xmjbSendMsgTaskExecutor;
  @Autowired
  private SendMessageCommonService sendMessageCommonServiceImpl;
  public static ExecutorService executorService = Executors.newFixedThreadPool(30);
  private final String XMJB_SEND_REDIS_KEY = "system:xmjb_sendMessage";
  
  //@Scheduled(cron="0/10 * * * * ?")
  public void sendMessage() {
    logger.info("【厦门借宝】短信开始发送===》");
    if (sendflag.compareAndSet(false, true)) {
      try {
        for (int i = 0; i < 5; i++) {
          Thread task = new Thread(new Runnable() {
            public void run() {
              Object value = null;
              MessageDto messageDto = null;
              try {
                if (redisTemplate.hasKey(XMJB_SEND_REDIS_KEY).booleanValue()) {
                  synchronized (this) {
                    value = redisTemplate.opsForList().leftPop(XMJB_SEND_REDIS_KEY);
                  }
                  if (!CommUtils.isNull(value)) {
                    logger.info("【厦门借宝】短信发送===》" + value);
                    messageDto = (MessageDto)JSON.parseObject(value.toString(), MessageDto.class);
                    if ((messageDto != null) && (!CommUtils.isNull(messageDto.getPhone())) && (!CommUtils.isNull(messageDto.getMsg())) && (!CommUtils.isNull(messageDto.getType()))) {
                      boolean bo = false;
                      int count = messageDto.getInviteCount() == null ? 0 : messageDto.getInviteCount().intValue();
                      count++;
                      if (count < 4) {
                        bo = xmjbSmsServiceImpl.sendXmjbMessage(messageDto.getPhone(), messageDto.getMsg());
                        if (!bo) {
                          if (count < 3) {
                            logger.info("【厦门借宝】短信发送失败重新放入redis");
                            messageDto.setInviteCount(Integer.valueOf(count));
                            redisTemplate.opsForList().rightPush(XMJB_SEND_REDIS_KEY, JSON.toJSONString(messageDto));
                          } else {
                            String phone = messageDto.getPhone();
                            String msg = messageDto.getMsg();
                            int type = Integer.parseInt(messageDto.getType());
                            int chenal = 1;
                            sendMessageCommonServiceImpl.saveErrorInfo(phone, msg, chenal, type);
                            logger.info(phone + "~~~~~~~~~~~~~~【厦门借宝】保存数据成功~~~~~~~~~~~~~~");
                          }
                        }
                      }
                    }
                  }
                }
              }
              catch (Exception e) {
                logger.error("【厦门借宝】短信发送失败,信息为{}", e.getMessage());
                e.printStackTrace();
                if (messageDto != null) {
                  String phone = messageDto.getPhone();
                  String msg = messageDto.getMsg();
                  int type = Integer.parseInt(messageDto.getType());
                  int chenal = 1;
                  sendMessageCommonServiceImpl.saveErrorInfo(phone, msg, chenal, type);
                  logger.info("~~~~~~~~~~~~~~【厦门借宝】保存数据成功~~~~~~~~~~~~~~");
                }
              }
            }
          });
          xmjbSendMsgTaskExecutor.execute(task);
        }
      }
      catch (Exception e) {
        logger.error("【厦门借宝】短信线程异常", e);
      }
      sendflag.set(false);
    }
  }
}
