package com.waterelephant.sms.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
public class RedisController2 {
	
	RedisController2(){
		System.out.println("++++++++++++++++++++++++++++++++");
	}
	
	@Autowired
	RedisTemplate<String, Object> redisTemplate;
	
	@Autowired
	StringRedisTemplate stringRedisTemplate;
	
	@RequestMapping("/smsredis")
	@ResponseBody
	public String redis() {
		String val = redisTemplate.opsForList().leftPop("system:sendMessage").toString();
		System.out.println(val);
		return val;
	}
	
	@RequestMapping("/smsredis2")
	@ResponseBody
	public String redisString() {
		String val = stringRedisTemplate.opsForList().leftPop("system:sendMessage").toString();
		System.out.println(val);
		return val;
	}
	
	@RequestMapping("/rupuhsmsredis")
	@ResponseBody
	public Long rupuhsmsredis() {
		Long rightPush2 = redisTemplate.opsForList().rightPush("AAAAAAAAAAAAAA", "biaobiaodwfsc");
		Long rightPush = stringRedisTemplate.opsForList().rightPush("BBBBBBBBBBBBBBBB", "biaobiao");
		Object qqq = redisTemplate.opsForList().rightPop("AAAAAAAAAAAAAA");
		Object aaaa = stringRedisTemplate.opsForList().rightPop("BBBBBBBBBBBBBBBB");
		System.out.println(qqq+"-----"+aaaa);
		return rightPush+rightPush2;
	}
	
	@RequestMapping("/rpopsmsredis")
	@ResponseBody
	public Object rpopsmsredis() {
		Object rightPop = redisTemplate.opsForList().rightPop("system:sendMessa");
		System.out.println(rightPop);
		return rightPop;
	}

}
