package com.waterelephant.sms.job.reportJob;

import com.waterelephant.sms.service.cssx.CssxSmsService;
import com.waterelephant.sms.service.es.YiMeiEsService;
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

/**
 * 没有用到，暂时停用
 * @author Administrator
 *
 */
//@Component
public class CssxReportJob {
  private Logger logger = LoggerFactory.getLogger(YiMeiReportJob.class);
  @Autowired
  private YiMeiEsService yiMeiEsServiceImpl;
  @Autowired
  private CssxSmsService cssxSmsServiceImpl;
  
  //@Scheduled(cron="0/10 * * * * ?")
  public void yiMeiCssxReport() {
    try {
      HttpResult result = cssxSmsServiceImpl.getSendReport();
      if (200 == result.getCode()) {
        String resultStr = result.getContent();
        logger.info("长沙水象亿美拉取短信信息：" + resultStr);
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
                  yiMeiEsServiceImpl.update(seqid, 1, reportEntity.getState(), reportEntity
                    .getSrctermid());
                } else {
                  yiMeiEsServiceImpl.update(seqid, 2, reportEntity.getState(), reportEntity
                    .getSrctermid());
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
      logger.error("执行获取报告的CssxReportJob的yiMeiCssxReport方法异常，信息为{}", e.getMessage());
      e.printStackTrace();
    }
  }
}
