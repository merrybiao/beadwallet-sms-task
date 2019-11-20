package com.waterelephant.sms.entity.lianlu;

import java.util.Date;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName="lianlu_info", type="es_message_info")
public class BwLianluMessageInfo {
	
  private String id;
  private String msgid;
  private String mobile;
  private String content;
  private String sign;
  private Integer count;
  private Date createTime;
  private String taskid;
  private String status;
  private String errorcode;
  private String receivetime;
 
}
