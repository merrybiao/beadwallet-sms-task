package com.waterelephant.sms.mapper;

import com.waterelephant.sms.entity.yimei.SmsConfig;
import org.apache.ibatis.annotations.Options;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

public abstract interface SmsConifigMapper {
	
  @Options(useGeneratedKeys=true, keyProperty="id")
  @Select({"select * from sms_config where id = #{id}"})
  public abstract SmsConfig querySmsConfig(@Param("id") Integer paramInteger);
  
}
