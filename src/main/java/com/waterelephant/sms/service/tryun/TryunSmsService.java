package com.waterelephant.sms.service.tryun;

import com.alibaba.fastjson.JSONArray;

public interface TryunSmsService {
	
	  public abstract boolean sendMessage(String mobile, String sign, String content,Integer count,boolean useTemplate, String templateNo, String noticeOrMarketing) throws Exception ;
	  
	  public abstract JSONArray getReport(String type) throws Exception ;

}
