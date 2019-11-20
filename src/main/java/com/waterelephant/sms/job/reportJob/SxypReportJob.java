package com.waterelephant.sms.job.reportJob;

import com.waterelephant.sms.service.es.YiMeiEsService;
import com.waterelephant.sms.service.sxyp.SxypSmsService;
import com.waterelephant.sms.stateVo.HttpResult;
import com.waterelephant.sms.stateVo.YimeiReportEntity;
import com.waterelephant.sms.utils.YiMeiXmlTransEntity;
import java.util.List;
import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
public class SxypReportJob {
  private Logger logger = LoggerFactory.getLogger(SxypReportJob.class);
  @Autowired
  private SxypSmsService SxypSmsServiceImpl;
  @Autowired
  private YiMeiEsService yiMeiEsServiceImpl;
  
  //@Scheduled(cron="3/12 * * * * ?")
  public void getSxypYimeiReport() {
    try {
      HttpResult result = this.SxypSmsServiceImpl.getReport();
      if (200 == result.getCode()) {
        String resultStr = result.getContent();
        this.logger.info("水象优品拉取短信信息：" + resultStr);
        int start = resultStr.indexOf("<response>");
        int end = resultStr.indexOf("</response>");
        if (start > -1) {
          String text = resultStr.substring(start, end);
          text = text + "</response>";
          JSONObject xmlJSONObj = XML.toJSONObject(text);
          Object obj = xmlJSONObj.getJSONObject("response").get("message");
          if ((null != obj) && ("" != obj)) {
            List<YimeiReportEntity> listData = YiMeiXmlTransEntity.getXMLEntity(text);
            this.logger.info("水象优品从亿美获取的数据个数：" + listData.size());
            for (int i = 0; i < listData.size(); i++) {
              YimeiReportEntity reportEntity = (YimeiReportEntity)listData.get(i);
              String seqid = reportEntity.getSeqid();
              this.yiMeiEsServiceImpl.saveReport(reportEntity.getSrctermid(), reportEntity.getState(), seqid, 1, 1);
              if ("DELIVRD".equals(reportEntity.getState())) {
                this.yiMeiEsServiceImpl.update(seqid, 1, reportEntity.getState(), reportEntity.getSrctermid());
              } else {
                this.yiMeiEsServiceImpl.update(seqid, 2, reportEntity.getState(), reportEntity.getSrctermid());
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
      this.logger.error("水象优品执行SxypReportJob的getSxypYimeiReport方法异常{}", e.getMessage());
      e.printStackTrace();
    }
  }
}
