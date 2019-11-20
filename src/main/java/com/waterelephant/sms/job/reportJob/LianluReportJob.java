package com.waterelephant.sms.job.reportJob;

import com.waterelephant.sms.entity.lianlu.BwLianluMessageReport;
import com.waterelephant.sms.service.es.LianLuEsService;
import com.waterelephant.sms.service.lianlu.LianluSmsService;
import com.waterelephant.sms.utils.LianLuXmlTransEntity;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
public class LianluReportJob {
  private Logger logger = LoggerFactory.getLogger(LianluReportJob.class);
  @Autowired
  private LianLuEsService lianLuEsServiceImpl;
  @Autowired
  private LianluSmsService LianluSmsServiceImpl;
  
  //@Scheduled(cron="0/12 * * * * ?")
  public void getLianluReport() {
    try {
      Map<String, String> resultmap = LianluSmsServiceImpl.getReport();
      if ("200".equals(resultmap.get("requestCode"))) {
        logger.info("联麓拉取短信信息：" + (String)resultmap.get("requestData"));
        String result = (String)resultmap.get("requestData");
        int start = result.indexOf("<returnsms>");
        int end = result.indexOf("</returnsms>");
        if (start > -1) {
          String text = result.substring(start, end);
          text = text + "</returnsms>";         
          List<BwLianluMessageReport> listData = LianLuXmlTransEntity.getXMLEntity(text);
          if (listData.size() > 0) {
            logger.info("从联麓获取的数据个数：" + listData.size());
            for (int i = 0; i < listData.size(); i++) {
              BwLianluMessageReport reportEntity = (BwLianluMessageReport)listData.get(i);
              lianLuEsServiceImpl.saveReport(reportEntity.getMobile(), reportEntity.getTaskid(), reportEntity.getStatus(), reportEntity.getErrorcode(), reportEntity.getReceivetime());
              lianLuEsServiceImpl.update(reportEntity.getTaskid(), reportEntity.getStatus(), reportEntity.getReceivetime(), reportEntity.getErrorcode());
            }
          }
        }
      }
      else {
        logger.error("联麓信息获取报告出现错误！！");
      }
    }
    catch (Exception e) {
      logger.error("执行LianluReportJob的getLianluReport的方法异常,异常信息为{}", e.getMessage());
      e.printStackTrace();
    }
  }
}
