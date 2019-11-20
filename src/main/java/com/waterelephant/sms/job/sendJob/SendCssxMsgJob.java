package com.waterelephant.sms.job.sendJob;

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
import com.waterelephant.sms.service.cssx.CssxSmsService;
import com.waterelephant.sms.utils.CommUtils;

/**
 * 没有用到，暂时停用
 * @author Administrator
 *
 */
//@Component
public class SendCssxMsgJob {
  private Logger logger = LoggerFactory.getLogger(SendCssxMsgJob.class);
  private AtomicBoolean sendflag = new AtomicBoolean(false);
  @Autowired
  private CssxSmsService cssxSmsServiceImpl;
  @Autowired
  private ThreadPoolTaskExecutor cssxSendMsgTaskExecutor;
  @Autowired
  private RedisTemplate<String,String> redisTemplate;
  @Autowired
  private SendMessageCommonService sendMessageCommonServiceImpl;
  
  private final String CSSX_SEND_REDIS_KEY = "system:cssx_sendMessage";
  
  //@Scheduled(cron="0/10 * * * * ?")
  public void sendMessage() {
    this.logger.info("【长沙水象】短信开始发送===》");
    if (this.sendflag.compareAndSet(false, true)) {
      try {
        for (int i = 0; i < 5; i++) {
          Thread task = new Thread(new Runnable() {
            public void run() {
              Object value = null;
              MessageDto messageDto = null;
              try {
                boolean ff = redisTemplate.hasKey(CSSX_SEND_REDIS_KEY).booleanValue();
                if (ff) {
                  synchronized (this) {
                    value = redisTemplate.opsForList().leftPop(CSSX_SEND_REDIS_KEY);
                  }
                  if (!CommUtils.isNull(value)) {
                    logger.info("【长沙水象】短信发送===》" + value);
                    messageDto = (MessageDto)JSON.parseObject(value.toString(), MessageDto.class);
                    if (!CommUtils.isNull(messageDto.getPhone()) &&  !CommUtils.isNull(messageDto.getMsg()) &&  !CommUtils.isNull(messageDto.getType())) {
                      boolean bo = false;
                      int count = messageDto.getInviteCount() == null ? 0 : messageDto.getInviteCount().intValue();
                      count++;
                      if (count < 4) {
                        bo = cssxSmsServiceImpl.sendCssxMessage(messageDto.getPhone(), messageDto.getMsg());
                        if (!bo) {
                          if (count < 3) {
                           logger.info("【长沙水象】短信发送失败重新放入redis");
                            messageDto.setInviteCount(Integer.valueOf(count));
                           redisTemplate.opsForList().rightPush(CSSX_SEND_REDIS_KEY, JSON.toJSONString(messageDto));
                          }
                          else {
                            String phone = messageDto.getPhone();
                            String msg = messageDto.getMsg();
                            int type = Integer.parseInt(messageDto.getType());
                            sendMessageCommonServiceImpl.saveErrorInfo(phone, msg, 1, type);
                            logger.info(phone + "~~~~~~~~~~~~~~【长沙水象】保存数据成功~~~~~~~~~~~~~~");
                          }
                        }
                      }
                    }
                  }
                }
              }
              catch (IllegalArgumentException e) {
                SendCssxMsgJob.this.logger.info("【长沙水象】" + e);
              }
              catch (Exception e) {
                SendCssxMsgJob.this.logger.error("【长沙水象】短信发送失败", e);
                if (messageDto != null) {
                  String phone = messageDto.getPhone();
                  String msg = messageDto.getMsg();
                  int type = Integer.parseInt(messageDto.getType());
                  SendCssxMsgJob.this.sendMessageCommonServiceImpl.saveErrorInfo(phone, msg, 1, type);
                  SendCssxMsgJob.this.logger.info("~~~~~~~~~~~~~~【长沙水象】保存数据成功~~~~~~~~~~~~~~");
                }
              }
            }
          });
          this.cssxSendMsgTaskExecutor.execute(task);
        }
      }
      catch (Exception e) {
        this.logger.error("【长沙水象】短信线程异常,信息为{}", e.getMessage());
        e.printStackTrace();
      }
      this.sendflag.set(false);
    }
  }
}
