package com.waterelephant.sms.entity.yimei;

import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName="yimei_report", type="es_message_report")
public class YiMeiMessageReport {
  private Long id;
  private String phone;
  private String msg;
  private String create_time;
  private String update_time;
  private String seqid;
  private String state_value;
  private int chenal;
  private int type;
  
}
