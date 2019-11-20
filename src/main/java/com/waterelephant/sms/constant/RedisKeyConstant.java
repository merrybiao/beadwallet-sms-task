package com.waterelephant.sms.constant;
/**
 * <p> redis Key 定义
 * @author Luyuan
 * @since 1.8
 * @version 1.0
 * @date 2019-01-25 17:29:38
 */
public interface RedisKeyConstant {
	//冒号节点
	public static final String COLON_NODE = ":";
    //API登录令牌
	public static final String API_TOKEN = "API:TOKEN:%s";
	//api的appId
	public static final String API_ID = "API:APPID:%s";
	
	public static final String SMS_QUEUE = "SMS:%s";
	
	//缓存30分钟
	public static final Integer CACHE_30M_TIME = 30 * 60 * 60;
}
