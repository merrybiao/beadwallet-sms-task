package com.waterelephant.sms.elasticRepos;

import com.waterelephant.sms.entity.commom.CommonMessageError;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public abstract interface CommonMessageErrorRepos extends ElasticsearchRepository<CommonMessageError, String>{
	
}
