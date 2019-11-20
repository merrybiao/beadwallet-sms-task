package com.waterelephant.sms.exception;

import lombok.Getter;
import lombok.Setter;

public class LianluException extends RuntimeException {
  private static final long serialVersionUID = 1L;
  
  @Getter
  @Setter
  private String code;
  
  public LianluException(String code, String msg)
  {
    super(msg);
    this.code = code;
  }
}
