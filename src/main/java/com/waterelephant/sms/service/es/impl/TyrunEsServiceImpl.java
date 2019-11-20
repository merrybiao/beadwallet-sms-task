package com.waterelephant.sms.service.es.impl;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.elasticsearch.action.update.UpdateRequest;
import org.elasticsearch.action.update.UpdateResponse;
import org.elasticsearch.rest.RestStatus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;

import com.waterelephant.sms.elasticRepos.BwTryEsReportRepos;
import com.waterelephant.sms.elasticRepos.BwTryunSendMessageInfoRepos;
import com.waterelephant.sms.elasticRepos.BwTryunTransMessageInfoRepos;
import com.waterelephant.sms.entity.tryun.BwTryEsReport;
import com.waterelephant.sms.entity.tryun.BwTryunSendMessageInfo;
import com.waterelephant.sms.entity.tryun.BwTryunTransEsMessageInfo;
import com.waterelephant.sms.service.es.TyrunEsService;
import com.waterelephant.sms.utils.CommUtils;
import com.waterelephant.sms.utils.DateUtil;

@Service
public class TyrunEsServiceImpl implements TyrunEsService {
	
	private Logger logger = LoggerFactory.getLogger(TyrunEsServiceImpl.class);
	
	@Autowired
	private BwTryunTransMessageInfoRepos bwTryunTransMessageInfoRepos;
	
	@Autowired
	private BwTryEsReportRepos bwTryEsReportRepos;
	
	@Autowired
	private BwTryunSendMessageInfoRepos bwTryunSendMessageInfoRepos;
	
	@Autowired
	private ElasticsearchTemplate elasticsearchTemplate;
	
	private final String es_index_key = "tryun_info";
	
	private final String es_type_key = "es_message_info";
	
	/**
	 *redis转存到ES
	 */
	@Override
	public void saveTryunSmsInfo(String mobile, String sign, String content,String batchId,Long sendTime,String createTime,Integer count,boolean useTemplate,String templateNo,String noticeOrMarking) {
		try {
			BwTryunTransEsMessageInfo trans = new BwTryunTransEsMessageInfo();
			trans.setId(batchId);
			trans.setMobile(mobile);
			trans.setSign(sign);
			trans.setContent(content);
			trans.setBatchId(batchId);
			trans.setCreateTime(DateUtil.getDateString(new Date()));
			trans.setSendTime(sendTime);
			trans.setCreateTime(createTime);
			trans.setCount(count);
			trans.setUseTemplate(useTemplate);
			trans.setTemplateNo(templateNo);
			trans.setNoticeOrMarketing(noticeOrMarking);
			BwTryunTransEsMessageInfo save = bwTryunTransMessageInfoRepos.save(trans);
			if(!CommUtils.isNull(save)) logger.info("手机号码:{}，信息内容:{}，存储ES成功，存储时间为:{}，批次号batchId为{}",mobile,content,createTime,batchId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	
	@Override
	public void saveTryunSmsReport(String smUuid, String deliverTime, String deliverResult, String mobile,
			String batchId) {
		try {
			BwTryEsReport report = new BwTryEsReport();
			report.setId(batchId);
			report.setBatchId(batchId);
			report.setDeliverResult(deliverResult);
			report.setDeliverTime(deliverTime);
			report.setMobile(mobile);
			report.setSmUuid(smUuid);
			BwTryEsReport save = bwTryEsReportRepos.save(report);
			if(!CommUtils.isNull(save)) logger.info("手机号码{}，批次号batchId为{}的报告存储成功",mobile,batchId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}


	@Override
	public void updateTryunSmsInfo(String batchId,String deliverTime, String deliverResult) {
		try {
			 UpdateQuery query = new UpdateQuery();
		      query.setId(batchId);
		      query.setIndexName(es_index_key);
		      query.setType(es_type_key);
		      Map<String, Object> updatemap = new HashMap<>();
		      updatemap.put("deliverTime", deliverTime);
		      updatemap.put("deliverResult", deliverResult);
		      UpdateRequest updateRequest = new UpdateRequest().doc(updatemap);
		      query.setUpdateRequest(updateRequest);
		      query.setDoUpsert(true);
		      UpdateResponse update = elasticsearchTemplate.update(query);
		       int status = update.status().getStatus();
		       if(200 == status) logger.info("批次号batchId为{}的发送信息更新成功",batchId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public void saveTryunSmsErrorInfo(BwTryunSendMessageInfo errorInfo) {
		try {
			BwTryunSendMessageInfo save = bwTryunSendMessageInfoRepos.save(errorInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
