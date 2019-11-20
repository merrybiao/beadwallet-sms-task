package com.waterelephant.sms.elasticRepos;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.waterelephant.sms.entity.tryun.BwTryunTransEsMessageInfo;

public interface BwTryunTransMessageInfoRepos extends ElasticsearchRepository<BwTryunTransEsMessageInfo, String>{

}
