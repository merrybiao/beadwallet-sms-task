package com.waterelephant.sms.service.tryun.impl;

import java.net.URLEncoder;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.httpclient.DefaultHttpMethodRetryHandler;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.NameValuePair;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.params.HttpMethodParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.waterelephant.sms.config.TryunProperties;
import com.waterelephant.sms.service.tryun.TryunSendSmsService;
import com.waterelephant.sms.utils.CommUtils;
import com.waterelephant.sms.utils.HttpUtil;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
@EnableConfigurationProperties(value = TryunProperties.class)
public class TryunSendSmsServiceImpl implements TryunSendSmsService{
	
	@Autowired
	private TryunProperties tryunProperties;
	
	private final String notice_type = "notice";
		
	public Map<String, String> sendMsg(String mobile, String content, String sign, boolean useTemplate,String templateNo,String noticeOrMarking)  throws Exception {
		Map<String, String> responseMap = new HashMap<String, String>();
	    HttpClient httpClient = new HttpClient();
        PostMethod postMethod = new PostMethod(tryunProperties.getSend_url());
        postMethod.getParams().setContentCharset("UTF-8");
        postMethod.getParams().setParameter(HttpMethodParams.RETRY_HANDLER,new DefaultHttpMethodRetryHandler());
    	NameValuePair[] sendData = {};
    	NameValuePair[] baseData = {};
    	//判断是通知类还是营销类
        if(tryunProperties.getUse_notice().equals(noticeOrMarking)) {
            NameValuePair[] data = {
                    new NameValuePair("accesskey", tryunProperties.getNotice_accesskey()),
                    new NameValuePair("secret", tryunProperties.getNotice_accessSecret()),
                    new NameValuePair("sign", sign),
                    new NameValuePair("mobile", mobile)
            };  
            baseData = Arrays.copyOf(data,data.length);
        } else {
        	   NameValuePair[] data = {
                       new NameValuePair("accesskey", tryunProperties.getMarketing_accesskey()),
                       new NameValuePair("secret", tryunProperties.getMarketing_accessSecret()),
                       new NameValuePair("sign", sign),
                       new NameValuePair("mobile", mobile)
               };  
            baseData = Arrays.copyOf(data,data.length);
        }
        
        //判断是否使用模板发送
        if(useTemplate) {
          	String[] param = content.split("-");
          	String joindata = StringUtils.join(param,"##");
          	sendData = Arrays.copyOf(baseData, baseData.length+2);
          	sendData[sendData.length-1] = new NameValuePair("content", URLEncoder.encode(joindata, "utf-8"));
          	sendData[sendData.length-2] = new NameValuePair("templateId", templateNo);
          } else {
          	sendData = Arrays.copyOf(baseData, baseData.length+1);
          	sendData[sendData.length-1] = new NameValuePair("content", URLEncoder.encode(content, "utf-8"));
          }
        
        //开始执行发送流程
        postMethod.setRequestBody(sendData);
        
        postMethod.setRequestHeader("Connection", "close");

        int statusCode = httpClient.executeMethod(postMethod);
        
        log.info("{}发送手机号码：{}，类容：{} 返回值为：{}",sign,mobile,content,"statusCode: " + statusCode + ", body: " + postMethod.getResponseBodyAsString());
        
        if(200 == statusCode) {
        	 String response = postMethod.getResponseBodyAsString();
        	 JSONObject responsejson = JSONObject.parseObject(response);
        	 if("0".equals(responsejson.getString("code"))) {
        		 responseMap.put("msg", "SUCCESS");
        		 responseMap.put("batchId", responsejson.getString("batchId"));
        	 } else {
        		 responseMap.put("msg", "FAIL");
        	 }
        } else {
        	 responseMap.put("msg", "FAIL");
        }
		return responseMap;
	}
	
	@Override
	public JSONArray getReport(String type) throws Exception {
		  StringBuffer sms = new StringBuffer();
		  if(type.equals(notice_type)) {
			  sms.append("accesskey=" + tryunProperties.getNotice_accesskey());
			  sms.append("&secret=" + tryunProperties.getNotice_accessSecret());
		  } else {
			  sms.append("accesskey=" + tryunProperties.getMarketing_accesskey());
			  sms.append("&secret=" + tryunProperties.getMarketing_accessSecret());
		  }
	     String  resp = HttpUtil.sendPostRequestBody(tryunProperties.getReport_url(), sms.toString());
	     if(!CommUtils.isNull(resp)) {
	    	 JSONObject reportjson = JSONObject.parseObject(resp);
	    	 JSONArray jsonArray = reportjson.getJSONArray("data");
	 	     if("0".equals(reportjson.getString("code"))&&!jsonArray.isEmpty()) {
	 	    	 return jsonArray;
	 	     } else {
	 	    	 return new JSONArray();
	 	     }
	     } else {
	    	 return new JSONArray();
	     }
	}
}
