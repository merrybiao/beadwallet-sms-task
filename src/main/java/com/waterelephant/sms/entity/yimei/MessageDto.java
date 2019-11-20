package com.waterelephant.sms.entity.yimei;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class MessageDto {
  private String phone;
  private String msg;
  private String type;
  private String businessScenario;
  private Integer inviteCount;
  private String delay;
 
}
