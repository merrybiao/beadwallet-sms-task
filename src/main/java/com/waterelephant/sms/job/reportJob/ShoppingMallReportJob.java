package com.waterelephant.sms.job.reportJob;

import com.waterelephant.sms.service.es.YiMeiEsService;
import com.waterelephant.sms.service.small.ShoppingMallService;
import com.waterelephant.sms.stateVo.HttpResult;
import com.waterelephant.sms.stateVo.YimeiReportEntity;
import com.waterelephant.sms.utils.CommUtils;
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
public class ShoppingMallReportJob {
  private Logger logger = LoggerFactory.getLogger(ShoppingMallReportJob.class);
  @Autowired
  private ShoppingMallService shoppingMallServiceImpl;
  @Autowired
  private YiMeiEsService yiMeiEsServiceImpl;
  
  //@Scheduled(cron="2/12 * * * * ?")
  public void yiMeiShoppingMallReport()  {
    try {
      HttpResult result = shoppingMallServiceImpl.getReport();
      if (200 == result.getCode()) {
        String resultStr = result.getContent();
        logger.info("商城拉取短信信息：" + resultStr);
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
                yiMeiEsServiceImpl.saveReport(reportEntity.getSrctermid(), reportEntity.getState(), seqid, 1, 1);
                if ("DELIVRD".equals(reportEntity.getState())) {
                  yiMeiEsServiceImpl.update(seqid, 1, reportEntity.getState(), reportEntity.getSrctermid());
                } else {
                  yiMeiEsServiceImpl.update(seqid, 2, reportEntity.getState(), reportEntity.getSrctermid());
                }
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
      logger.error("商城执行获取报告的ShoppingMallReportJob的yiMeiShoppingMallReport方法异常，信息为{}", e.getMessage());
      e.printStackTrace();
    }
  }
}
