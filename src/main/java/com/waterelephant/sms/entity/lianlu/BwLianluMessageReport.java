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
@Document(indexName="lianlu_report", type="es_message_report")
public class BwLianluMessageReport {
  private String id;
  private String mobile;
  private String taskid;
  private String status;
  private String errorcode;
  private String receivetime;
  private Date createTime;
  
}
