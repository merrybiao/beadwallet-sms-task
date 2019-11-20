package com.waterelephant.sms.controller;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.waterelephant.sms.elasticRepos.BwLianluMessageErrorRepos;
import com.waterelephant.sms.entity.lianlu.BwLianluMessageError;

@RestController
@RequestMapping("/task_sms")
public class RedisController {
	
	RedisController(){
		System.out.println("++++++++++++++++++++++++++++++++");
	}
	
	@Autowired
	RedisTemplate<String, Object> template;
	
	@Autowired
	private BwLianluMessageErrorRepos bwLianluMessageErrorRepos;
	
	@RequestMapping("/redis")
	@ResponseBody
	public String redis() {
		String val = template.boundListOps("system:sendMessage").toString();
		System.out.println(val);
		return val;
	}
	
	@RequestMapping("/saveEs")
	@ResponseBody
	public BwLianluMessageError saveEs() {
		BwLianluMessageError error = new BwLianluMessageError();
		error.setId("10012001");
		error.setCreateTime(new Date());
		error.setMobile("13094157324");
		BwLianluMessageError save = bwLianluMessageErrorRepos.save(error);
		return save;
	}
	
	

}
