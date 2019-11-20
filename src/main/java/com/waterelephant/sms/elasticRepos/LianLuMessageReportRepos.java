package com.waterelephant.sms.elasticRepos;

import com.waterelephant.sms.entity.lianlu.BwLianluMessageReport;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public abstract interface LianLuMessageReportRepos extends ElasticsearchRepository<BwLianluMessageReport, String>{
	
}
