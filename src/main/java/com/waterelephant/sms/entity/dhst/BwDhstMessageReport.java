package com.waterelephant.sms.entity.dhst;

import java.io.Serializable;
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
@Document(indexName="dhst_report", type="es_message_report")
public class BwDhstMessageReport implements Serializable {
  private static final long serialVersionUID = 101212151454114L;
  private String id;
  private String msgid;
  private String phone;
  private String status;
  private String desc;
  private String wgcode;
  private Date createTime;
  private String smsCount;
  private String smsIndex;
  
}
