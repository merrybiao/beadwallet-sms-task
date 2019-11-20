package com.waterelephant.sms.entity.yimei;

import java.io.Serializable;
import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName="yimei_info", type="es_message_info")
public class YiMeiMessageInfo implements Serializable {
  private static final long serialVersionUID = 1L;
  private String id;
  private String phone;
  private String msg;
  private int state;
  private String create_time;
  private String update_time;
  private String seqid;
  private String state_value;
  private int chenal;
  private int type;
  
}
