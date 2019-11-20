package com.waterelephant.sms.service.tryun;

import java.util.Map;

import com.alibaba.fastjson.JSONArray;

public interface TryunSendSmsService {
	

	public Map<String,String> sendMsg(String mobile,String content,String sign,boolean useTemplate,String templateNo,String noticeOrMarking) throws Exception;
	
	public JSONArray getReport(String type) throws Exception;

}
