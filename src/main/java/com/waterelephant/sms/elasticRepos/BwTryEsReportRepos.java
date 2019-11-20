package com.waterelephant.sms.elasticRepos;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.waterelephant.sms.entity.tryun.BwTryEsReport;

public interface BwTryEsReportRepos extends ElasticsearchRepository<BwTryEsReport, String>{

}
