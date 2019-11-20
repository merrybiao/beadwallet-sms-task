package com.waterelephant.sms.job.reportJob;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicBoolean;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSON;
import com.waterelephant.sms.service.es.YiMeiEsService;
import com.waterelephant.sms.service.yimei.service.YimeiSendMessageService;
import com.waterelephant.sms.stateVo.HttpResult;
import com.waterelephant.sms.stateVo.YimeiReportEntity;
import com.waterelephant.sms.utils.CommUtils;
import com.waterelephant.sms.utils.YiMeiXmlTransEntity;

@Component
public class YiMeiReportJob {
	
  private Logger logger = LoggerFactory.getLogger(YiMeiReportJob.class);
  
  private static AtomicBoolean sendMessageFlag = new AtomicBoolean(false);
  
  private static ExecutorService executorService = Executors.newFixedThreadPool(30);
    
  @Autowired
  private YiMeiEsService yiMeiEsServiceImpl;
  
  @Autowired
  private RedisTemplate<String,Object> redisTemplate;
  
  @Autowired
  private YimeiSendMessageService yimeiSendMessageServiceImpl;
  
  @Scheduled(cron="2/12 * * * * ?")
  public void yiMeiCommonReport() {
    try {
      HttpResult result = yimeiSendMessageServiceImpl.getReport();
      if (200 == result.getCode()) {
        String resultStr = result.getContent();
        logger.info("公共亿美拉取短信信息：" + resultStr);
        int start = resultStr.indexOf("<response>");
        int end = resultStr.indexOf("</response>");
        if (start > -1) {
          String text = resultStr.substring(start, end);
          text = text + "</response>";
          JSONObject xmlJSONObj = XML.toJSONObject(text);
          Object obj = xmlJSONObj.getJSONObject("response").get("message");
          if ((null != obj) && ("" != obj)) {
            List<YimeiReportEntity> listData = YiMeiXmlTransEntity.getXMLEntity(text);
            for (int i = 0; i < listData.size(); i++) {
              YimeiReportEntity reportEntity = (YimeiReportEntity)listData.get(i);
              String seqid = reportEntity.getSeqid();
              if (!CommUtils.isNull(seqid)) {
            	  Long val = redisTemplate.opsForList().rightPush("yimeireport", JSON.toJSONString(reportEntity));
            	  if(!CommUtils.isNull(val))logger.info("亿美转移到redis的报告数据为成功，值为："+reportEntity);
              }
            }
          }
          else {
            return;
          }
        }
      }
    }
    catch (Exception e) {
      logger.error("执行获取报告的YiMeiReportJob的yiMeiCommonReport方法异常，信息为{}", e.getMessage());
      e.printStackTrace();
    }
  }
  
  @Scheduled(cron="* * * * * ?")
  public void transferRedisToEs() {
    if (sendMessageFlag.compareAndSet(false, true)) {
      try {
        for (int i = 0; i < 1; i++) {
          executorService.execute(new Runnable() {
            public void run() {
              Object redisValue = "";
              try {
                if (redisTemplate.hasKey("yimeireport").booleanValue()) {
                  redisValue = redisTemplate.opsForList().rightPop("yimeireport");
                  if (!CommUtils.isNull(redisValue)) {
                    logger.info("亿美从redis中取出的报告值为：" + redisValue);
                    YimeiReportEntity  reportEntity = com.alibaba.fastjson.JSONObject.parseObject(redisValue.toString(), YimeiReportEntity.class);
                    yiMeiEsServiceImpl.saveReport(reportEntity.getSrctermid(), reportEntity.getState(), reportEntity.getSeqid(), 1, 1);
                    if ("DELIVRD".equals(reportEntity.getState())) {
                      yiMeiEsServiceImpl.update(reportEntity.getSeqid(), 1, reportEntity.getState(), reportEntity.getSrctermid());
                    } else {
                      yiMeiEsServiceImpl.update(reportEntity.getSeqid(), 2, reportEntity.getState(), reportEntity.getSrctermid());
                    }
                  }
                }
              }
              catch (Exception e) {
                logger.error("亿美修改数据异常，异常信息为{}：", e.getMessage());
                e.printStackTrace();
              }
            }
          });
        }
      }
      catch (Exception e) {
        logger.error("亿美修改数据异常：,异常信息为{}", e.getMessage());
        e.printStackTrace();
      }
      finally {
        sendMessageFlag.set(false);
      }
    }
  }
}
