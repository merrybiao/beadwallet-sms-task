package com.waterelephant.sms.service.tryun.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.waterelephant.sms.config.TryunProperties;
import com.waterelephant.sms.service.tryun.TryunSendSmsService;
import com.waterelephant.sms.service.tryun.TryunSmsService;
import com.waterelephant.sms.utils.DateUtil;

@Service
public class TryunSmsServiceImpl implements TryunSmsService{
	
	private Logger logger = LoggerFactory.getLogger(TryunSmsServiceImpl.class);
	
	@Autowired
	private TryunSendSmsService tryunSendSmsServiceImpl;
		
	@Autowired
	private RedisTemplate<String, String> redisTemplate;
	
	@Autowired
	private TryunProperties tryunProperties;
	
	private final String notice_type = "notice";
	
	private final String marking_type = "marking";
	
	private final String redisSaveKey = "sms_tryun_redis:sendMessage";
	
	@Override
	public  boolean sendMessage(String mobile, String sign, String content,Integer count,boolean useTemplate, String templateNo, String noticeOrMarketing) throws Exception {
		Map<String, Object> data = new HashMap<String, Object>();
		String redisContent = content;
		if (useTemplate) redisContent = getContent(useTemplate, noticeOrMarketing, templateNo, content);
		data.put("mobile", mobile);
		data.put("sign", sign);
		data.put("content", redisContent);
		data.put("useTemplate", useTemplate);
		data.put("templateNo", templateNo);
		data.put("noticeOrMarketing", noticeOrMarketing);
		Map<String, String> response = tryunSendSmsServiceImpl.sendMsg(mobile, content, sign, useTemplate, templateNo, noticeOrMarketing);
		if("SUCCESS".equals(response.get("msg"))) {
			data.put("sendTime", System.currentTimeMillis());
			data.put("count", count);
			data.put("batchId", response.get("batchId"));
			data.put("createTime", DateUtil.getDateString(new Date()));
			Long leftPush = redisTemplate.opsForList().leftPush(redisSaveKey, JSON.toJSONString(data));
			logger.info("手机号{}存储redis成功,返回值为{}",mobile,leftPush);
			return true;
		} else {
			return false;
		}
	}

	public String getContent(boolean useTemplate,String noticeOrMarking,String templateNo,String args) {
		String[] argsArray = args.split("-");
		String content = null;
		if(useTemplate&&noticeOrMarking.equals(notice_type)) {
            Set<Object> keys = redisTemplate.opsForHash().keys(tryunProperties.getNotice_template_key());
            Iterator<Object> iterator = keys.iterator();
            while(iterator.hasNext()) {
            	String next = iterator.next().toString();
            	if(next.equals(templateNo)) {
            		content = redisTemplate.opsForHash().get(tryunProperties.getNotice_template_key(), next).toString();
            		content = getSendContent(argsArray, content);
            	}
            }
		} else if(useTemplate&&noticeOrMarking.equals(marking_type)) {
		     Set<Object> keys = redisTemplate.opsForHash().keys(tryunProperties.getMarketing_template_key());
	            Iterator<Object> iterator = keys.iterator();
	            while(iterator.hasNext()) {
	            	String next = iterator.next().toString();
	            	if(next.equals(templateNo)) {
	            		content = redisTemplate.opsForHash().get(tryunProperties.getMarketing_template_key(), next).toString();
	            		content = getSendContent(argsArray, content);
	            	}
	          }
		}
		return content;
	}
	@Override
	public JSONArray getReport(String type) throws Exception {
		return tryunSendSmsServiceImpl.getReport(type);
	}
	
	public static String getSendContent(String[] args,String content) {
		StringBuffer buffer = new StringBuffer();
		char[] array = content.toCharArray();
		int i =0;
		for (char c : array) {
			if(c == '{'|| c=='}') {
				continue;
			} else if (Character.isDigit(c)) {
				buffer.append(args[i]);
				i++;
			} else {
				buffer.append(c);
			}
		}
		return buffer.toString();	
	}
}
