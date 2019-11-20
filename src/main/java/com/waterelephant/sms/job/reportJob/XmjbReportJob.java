package com.waterelephant.sms.job.reportJob;

import java.util.List;

import org.json.JSONObject;
import org.json.XML;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;

import com.waterelephant.sms.service.es.YiMeiEsService;
import com.waterelephant.sms.service.xmjb.XmjbSmsService;
import com.waterelephant.sms.stateVo.HttpResult;
import com.waterelephant.sms.stateVo.YimeiReportEntity;
import com.waterelephant.sms.utils.YiMeiXmlTransEntity;

/**
 * 没有用到，暂时停用
 * @author Administrator
 *
 */
//@Component
public class XmjbReportJob {
  private Logger logger = LoggerFactory.getLogger(CssxReportJob.class);
  @Autowired
  private XmjbSmsService XmjbSmsServiceImpl;
  @Autowired
  private YiMeiEsService yiMeiEsServiceImpl;
  
  //@Scheduled(cron="0/10 * * * * ?")
  public void getXmjbYimeiReport() {
    try {
      HttpResult result = XmjbSmsServiceImpl.getXmjbReport();
      if (200 == result.getCode()) {
        String resultStr = result.getContent();
        logger.info("厦门借宝拉取短信信息：" + resultStr);
        int start = resultStr.indexOf("<response>");
        int end = resultStr.indexOf("</response>");
        if (start > -1) {
          String text = resultStr.substring(start, end);
          text = text + "</response>";
          JSONObject xmlJSONObj = XML.toJSONObject(text);
          Object obj = xmlJSONObj.getJSONObject("response").get("message");
          if ((null != obj) && ("" != obj)) {
            List<YimeiReportEntity> listData = YiMeiXmlTransEntity.getXMLEntity(text);
            logger.info("厦门借宝从亿美获取的数据个数：" + listData.size());
            for (int i = 0; i < listData.size(); i++) {
              YimeiReportEntity reportEntity = (YimeiReportEntity)listData.get(i);
              String seqid = reportEntity.getSeqid();
              yiMeiEsServiceImpl.saveReport(reportEntity.getSrctermid(), reportEntity.getState(), seqid, 1, 1);
              if (!"DELIVRD".equals(reportEntity.getState())) {
                yiMeiEsServiceImpl.update(seqid, 2, reportEntity.getState(), reportEntity.getSrctermid());
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
      logger.error("厦门借宝执行XmjbReportJob的getXmjbYimeiReport方法异常", e.getMessage());
    }
  }
}
