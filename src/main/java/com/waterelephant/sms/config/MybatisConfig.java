package com.waterelephant.sms.config;

import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;

@org.springframework.context.annotation.Configuration
public class MybatisConfig{
	
	  @Bean
	  public ConfigurationCustomizer configurationCustomizer(){
	  
	   return new ConfigurationCustomizer(){
		   
	      public void customize(org.apache.ibatis.session.Configuration configuration){
	    	  
	        configuration.setMapUnderscoreToCamelCase(true);
	      }
	    };	    
	  }
}
