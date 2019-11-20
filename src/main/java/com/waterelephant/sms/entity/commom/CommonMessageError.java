package com.waterelephant.sms.entity.commom;

import org.springframework.data.elasticsearch.annotations.Document;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Document(indexName="yimei_error", type="es_message_error")
public class CommonMessageError {
  private String id;
  private String phone;
  private String msg;
  private int chenal;
  private int type;
  
  
}
