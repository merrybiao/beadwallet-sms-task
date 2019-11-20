package com.waterelephant.sms.elasticRepos;

import com.waterelephant.sms.entity.lianlu.BwLianluMessageError;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public abstract interface BwLianluMessageErrorRepos extends ElasticsearchRepository<BwLianluMessageError, String>{
	
}
