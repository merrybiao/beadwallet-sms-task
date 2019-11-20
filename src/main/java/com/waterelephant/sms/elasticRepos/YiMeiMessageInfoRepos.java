package com.waterelephant.sms.elasticRepos;

import com.waterelephant.sms.entity.yimei.YiMeiMessageInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public abstract interface YiMeiMessageInfoRepos extends ElasticsearchRepository<YiMeiMessageInfo, String>{
	
}
