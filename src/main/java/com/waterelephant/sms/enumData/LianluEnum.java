package com.waterelephant.sms.enumData;

import com.waterelephant.sms.exception.LianluException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


public enum LianluEnum {
  CODE_6060("6060", "不存在的签名"),  CODE_4040("4040", "系统异常,http请求失败"),  CODE_5050("5050", "短信发送失败"),  CODE_3030("3030", "系统发生异常");
  
  @Getter
  @Setter
  private String code;
  
  @Getter
  @Setter
  private String msg;
  
  private LianluEnum(String code, String msg) {
    this.code = code;
    this.msg = msg;
  }
  
  public LianluException getErrorInstence() {
    return new LianluException(this.code, this.msg);
  }
  
}
