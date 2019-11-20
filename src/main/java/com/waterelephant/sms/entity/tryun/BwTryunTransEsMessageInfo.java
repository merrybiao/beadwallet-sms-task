package com.waterelephant.sms.entity.tryun;

import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Document(indexName="tryun_info", type="es_message_info")
public class BwTryunTransEsMessageInfo {
	
	private String id;
	
	private String mobile;
	
	private String sign;
	
	private String content;
	
	private Integer count;
	
	private Long sendTime;
	
	private String createTime;
	
	private String batchId;
	
	/**
	 * 需要从回执的报告中获取
	 */
	private Long deliverTime;
	
	/**
	 * 需要从回执的报告中获取
	 */
	private String deliverResult;
	
	private boolean useTemplate;
	 
	private String templateNo;
	 
	private String noticeOrMarketing;

}
