package com.waterelephant.sms.elasticRepos;

import com.waterelephant.sms.entity.lianlu.BwLianluMessageInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public abstract interface LianLuMessageInfoRepos
  extends ElasticsearchRepository<BwLianluMessageInfo, String>
{}
