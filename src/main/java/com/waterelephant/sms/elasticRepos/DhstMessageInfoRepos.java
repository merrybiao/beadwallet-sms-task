package com.waterelephant.sms.elasticRepos;

import com.waterelephant.sms.entity.dhst.BwDhstMessageInfo;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public abstract interface DhstMessageInfoRepos
  extends ElasticsearchRepository<BwDhstMessageInfo, String>
{}
