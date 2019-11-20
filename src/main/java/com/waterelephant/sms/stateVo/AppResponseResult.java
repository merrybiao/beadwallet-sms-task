package com.waterelephant.sms.stateVo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class AppResponseResult
{
  private String code;
  private String msg;
  private Object result;
  
}
