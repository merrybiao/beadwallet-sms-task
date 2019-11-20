package com.waterelephant.sms.entity.yimei;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class SmsConfig {
  private int id;
  private int chenal;
  private String sign;
  private int state;
  private int type;
  
}
