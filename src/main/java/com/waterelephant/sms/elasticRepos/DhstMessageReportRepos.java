package com.waterelephant.sms.elasticRepos;

import com.waterelephant.sms.entity.dhst.BwDhstMessageReport;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public abstract interface DhstMessageReportRepos
  extends ElasticsearchRepository<BwDhstMessageReport, String>
{}
