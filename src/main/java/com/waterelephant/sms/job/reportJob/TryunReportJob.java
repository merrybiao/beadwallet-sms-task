package com.waterelephant.sms.job.reportJob;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.alibaba.fastjson.JSONArray;
import com.waterelephant.sms.service.es.TyrunEsService;
import com.waterelephant.sms.service.tryun.TryunSmsService;

@Component
public class TryunReportJob {
	
  private Logger logger = LoggerFactory.getLogger(TryunReportJob.class);
  
  @Autowired
  private TryunSmsService tryunSmsServiceImpl;
  
  @Autowired
  private TyrunEsService tyrunEsServiceImpl;
  
  private final String notice_type = "notice";
  
  private final String marketing_type = "marketing";
  
  /**
        * 获取天瑞云通知类短信报告结果。
   */
  //@Scheduled(cron="1/12 * * * * ?")
  public void getTryunNoticeReport() {
    try {
    	JSONArray report = tryunSmsServiceImpl.getReport(notice_type);
      if (!report.isEmpty()) {
    	  for(int i =0 ;i<report.size();i++) {
    		  String smUuid = report.getJSONObject(i).getString("smUuid");
        	  String deliverTime = report.getJSONObject(i).getString("deliverTime");
        	  String deliverResult = report.getJSONObject(i).getString("deliverResult");
        	  String mobile = report.getJSONObject(i).getString("mobile");
        	  String batchId = report.getJSONObject(i).getString("batchId");
        	  tyrunEsServiceImpl.saveTryunSmsReport(smUuid, deliverTime, deliverResult, mobile, batchId);
        	  tyrunEsServiceImpl.updateTryunSmsInfo(batchId, deliverTime, deliverResult);
    	  }
      }
      else {
    	  logger.info("天瑞云信息获取通知类短信报告数据为空！！");
        }
    }
    catch (Exception e) {
	      logger.error("执行TryunReportJob的getTryunNoticeReport的方法异常,异常信息为{}", e.getMessage());
	      e.printStackTrace();
       }
  }
  
  
  /**
        * 获取天瑞云营销类短信报告结果。
   */
  //@Scheduled(cron="3/10 * * * * ?")
  public void getTryunMarkingReport() {
    try {
    	JSONArray report = tryunSmsServiceImpl.getReport(marketing_type);
      if (!report.isEmpty()) {
    	  for(int i =0 ;i<report.size();i++) {
    		  String smUuid = report.getJSONObject(i).getString("smUuid");
        	  String deliverTime = report.getJSONObject(i).getString("deliverTime");
        	  String deliverResult = report.getJSONObject(i).getString("deliverResult");
        	  String mobile = report.getJSONObject(i).getString("mobile");
        	  String batchId = report.getJSONObject(i).getString("batchId");
        	  tyrunEsServiceImpl.saveTryunSmsReport(smUuid, deliverTime, deliverResult, mobile, batchId);
        	  tyrunEsServiceImpl.updateTryunSmsInfo(batchId, deliverTime, deliverResult);
    	  }
      }
      else {
    	  	logger.info("天瑞云信息获取营销类短信报告数据为空！！");
      	 }
    }
    catch (Exception e) {
	      logger.error("执行TryunReportJob的getTryunMarkingReport的方法异常,异常信息为{}", e.getMessage());
	      e.printStackTrace();
    	}
  	}
}
