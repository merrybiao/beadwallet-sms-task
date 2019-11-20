package com.waterelephant.sms.elasticRepos;

import com.waterelephant.sms.entity.yimei.YiMeiMessageReport;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public abstract interface YiMeiMessageReportRepos extends ElasticsearchRepository<YiMeiMessageReport, String>{
	
}
