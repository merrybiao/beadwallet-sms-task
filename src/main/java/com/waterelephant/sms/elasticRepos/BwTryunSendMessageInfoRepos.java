package com.waterelephant.sms.elasticRepos;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.waterelephant.sms.entity.tryun.BwTryunSendMessageInfo;

public interface BwTryunSendMessageInfoRepos extends ElasticsearchRepository<BwTryunSendMessageInfo, String>{

}
