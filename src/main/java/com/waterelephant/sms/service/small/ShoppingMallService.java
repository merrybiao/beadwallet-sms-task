package com.waterelephant.sms.service.small;

import com.waterelephant.sms.stateVo.HttpResult;

public abstract interface ShoppingMallService
{
  public abstract boolean sendShoppingMallMsg(String paramString1, String paramString2);
  
  public abstract HttpResult getReport();
}
