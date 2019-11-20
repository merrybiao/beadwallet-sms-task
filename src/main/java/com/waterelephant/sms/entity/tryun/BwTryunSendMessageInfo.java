package com.waterelephant.sms.entity.tryun;

import org.springframework.data.elasticsearch.annotations.Document;

import lombok.Getter;
import lombok.Setter;

@Document(indexName="tryun_error", type="es_message_error")
public class BwTryunSendMessageInfo {
	
	@Getter
	@Setter
	private String id;
	@Getter
	@Setter
	private String mobile;
	@Getter
	@Setter
	private String sign;
	@Getter
	@Setter
	private String content;
	@Getter
	@Setter
	private Integer count;
	@Getter
	@Setter
	private Long sendTime;
	@Getter
	@Setter
	private String createTime;
	
	private boolean useTemplate;
	@Getter
	@Setter
	private String templateNo;
	@Getter
	@Setter
	private String noticeOrMarketing;
	
	public boolean getUseTemplate() {
		return useTemplate;
	}
	public void setUseTemplate(boolean useTemplate) {
		this.useTemplate = useTemplate;
	}
	
	

}
