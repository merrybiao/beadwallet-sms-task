package com.waterelephant.sms.constant;

public enum ErrorCode {
  SUCCESS("200", "请求接口成功"),  LOGIN_FAIL("201", "登录失败，请稍后再试~"),  UN_LOGIN("202", "还未登录"),  TOKEN_ERROR("203", "token无效或不存在~"),  MEMBER_EXISTS("204", "该会员已存在或已注册~"),  PARAM_ERROR("300", "参数错误"),  PARAM_MISSING("301", "缺少参数"),  PARAM_IS_EMPTY("302", "参数为空"),  PARAM_TYPE_ERROR("303", "参数类型错误"),  CAPTCHA_FAIL("401", "获取验证码失败"),  CAPTCHA_ERROR("402", "验证码错误"),  BUSSIN_EXCEPTION_ERROR("600", "业务流程异常"),  PAY_FAIL("700", "支付失败"),  FAIL("900", "请求接口失败");
  
  private String code;
  private String desc;
  
  private ErrorCode(String code, String desc) {
    this.code = code;
    this.desc = desc;
  }
  
  public String getCode() {
    return this.code;
  }
  
  public void setCode(String code) {
    this.code = code;
  }
  
  public String getDesc() {
    return this.desc;
  }
  
  public void setDesc(String desc) {
    this.desc = desc;
  }
}
