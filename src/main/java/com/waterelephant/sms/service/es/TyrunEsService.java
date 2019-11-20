package com.waterelephant.sms.service.es;

import com.waterelephant.sms.entity.tryun.BwTryunSendMessageInfo;

public interface TyrunEsService {
	
	public void saveTryunSmsInfo(String mobile,String sign,String content,String batchId,Long sendTime,String createTime,Integer count,boolean useTemplate,String templateNo,String noticeOrMarketing);

	public void saveTryunSmsErrorInfo(BwTryunSendMessageInfo errorinfo);

	public void saveTryunSmsReport(String smUuid, String deliverTime, String deliverResult, String mobile, String batchId);
	
	public void updateTryunSmsInfo(String batchId,String deliverTime,String deliverResult);
}
