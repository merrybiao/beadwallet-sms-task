package com.waterelephant.sms.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import lombok.Data;

@Data
@ConfigurationProperties(prefix = TryunProperties.PREFIX)
public class TryunProperties {
	
	public static final String PREFIX = "sms.tryun";
	
	private String use_notice;
	
	private String notice_accesskey;
	
	private String notice_accessSecret;
	
	private String notice_template_key;
		
	private String marketing_accesskey;
	
	private String marketing_accessSecret;
	
	private String marketing_template_key;
	
	private String send_url;
	
	private String report_url;

}
