package com.waterelephant.sms;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling 
@MapperScan("com.waterelephant.sms.mapper")
@SpringBootApplication
public class SmsTaskApplication {

	
	public static void main(String[] args) {
		System.setProperty("es.set.netty.runtime.available.processors", "false");
		SpringApplication.run(SmsTaskApplication.class, args);
	}
}
