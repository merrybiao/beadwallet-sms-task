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
@Document(indexName="tryun_report", type="es_message_report")
public class BwTryEsReport {
	
	private String id;
	
	private String smUuid;
	
	private String deliverTime;
	
	private String deliverResult;
	
	private String mobile;
	
	private String batchId;

}
