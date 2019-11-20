package com.waterelephant.sms.service.small;

import com.waterelephant.sms.stateVo.HttpResult;

public abstract interface ShoppingMallSendService
{
  public abstract HttpResult getShoppingMallReport();
  
  public abstract boolean sendShoppingMallMsg(String paramString1, String paramString2, String paramString3);
}
