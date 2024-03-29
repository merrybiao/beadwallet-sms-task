package com.waterelephant.sms.config;


import javax.sql.DataSource;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.alibaba.druid.pool.DruidDataSource;

@Configuration
public class DruidConfig {
	
  @Bean
  @ConfigurationProperties(prefix="spring.datasource")
  public DataSource druid()  {
	  
	  return new DruidDataSource();
	    
	}
}
