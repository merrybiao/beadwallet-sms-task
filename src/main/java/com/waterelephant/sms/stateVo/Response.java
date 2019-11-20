package com.waterelephant.sms.stateVo;

public class Response<T> {
  private String requestCode;
  private String requestMsg;
  private T obj;
  
  public boolean equals(Object o) {
    if (o == this) {
      return true;
    }
    if (!(o instanceof Response)) {
      return false;
    }
    Response<?> other = (Response)o;
    if (!other.canEqual(this)) {
      return false;
    }
    Object this$requestCode = getRequestCode();Object other$requestCode = other.getRequestCode();
    if (this$requestCode == null ? other$requestCode != null : !this$requestCode.equals(other$requestCode)) {
      return false;
    }
    Object this$requestMsg = getRequestMsg();Object other$requestMsg = other.getRequestMsg();
    if (this$requestMsg == null ? other$requestMsg != null : !this$requestMsg.equals(other$requestMsg)) {
      return false;
    }
    Object this$obj = getObj();Object other$obj = other.getObj();return this$obj == null ? other$obj == null : this$obj.equals(other$obj);
  }
  
  protected boolean canEqual(Object other) {
    return other instanceof Response;
  }
  
  public String toString() {
    return "Response(requestCode=" + getRequestCode() + ", requestMsg=" + getRequestMsg() + ", obj=" + getObj() + ")";
  }
  
  public void setRequestCode(String requestCode) {
    this.requestCode = requestCode;
  }
  
  public void setRequestMsg(String requestMsg)
  {
    this.requestMsg = requestMsg;
  }
  
  public void setObj(T obj) {
    this.obj = obj;
  }
  
  public int hashCode() {
    int PRIME = 59;int result = 1;Object $requestCode = getRequestCode();result = result * 59 + ($requestCode == null ? 43 : $requestCode.hashCode());Object $requestMsg = getRequestMsg();result = result * 59 + ($requestMsg == null ? 43 : $requestMsg.hashCode());Object $obj = getObj();result = result * 59 + ($obj == null ? 43 : $obj.hashCode());return result;
  }
  
  public Response(String requestCode, String requestMsg, T obj) {
    this.requestCode = requestCode;this.requestMsg = requestMsg;this.obj = obj;
  }
  
  public String getRequestCode() {
    return this.requestCode;
  }
  
  public String getRequestMsg()
  {
    return this.requestMsg;
  }
  
  public T getObj() {
    return this.obj;
  }
  
  public Response() {}
}
