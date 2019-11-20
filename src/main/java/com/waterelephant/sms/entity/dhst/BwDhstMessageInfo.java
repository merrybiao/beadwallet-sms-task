package com.waterelephant.sms.entity.dhst;

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
@Document(indexName="dhst_info", type="es_message_info")
public class BwDhstMessageInfo {
  private String id;
  private String msgid;
  private String phone;
  private String content;
  private String sign;
  private String sendTime;
  private Integer count;
  private Date createTime;
  private String desc;
  private String wgcode;
  
}