package com.waterelephant.sms.service.es.impl;

import com.waterelephant.sms.elasticRepos.DhstMessageInfoRepos;
import com.waterelephant.sms.elasticRepos.DhstMessageReportRepos;
import com.waterelephant.sms.entity.dhst.BwDhstMessageInfo;
import com.waterelephant.sms.entity.dhst.BwDhstMessageReport;
import com.waterelephant.sms.service.es.DhstEsService;
import com.waterelephant.sms.utils.CommUtils;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.elasticsearch.action.update.UpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;

@Service
public class DhstEsServiceImpl implements DhstEsService {
  private Logger logger = LoggerFactory.getLogger(DhstEsServiceImpl.class);
  @Autowired
  private DhstMessageInfoRepos dhstMessageInfoRepos;
  @Autowired
  private DhstMessageReportRepos dhstMessageReportRepos;
  @Autowired
  private ElasticsearchTemplate elasticsearchTemplate;
  
  public boolean saveInfo(String msgid, String phones, String content, String sign, String sendtime) {
    BwDhstMessageInfo messageInfo = new BwDhstMessageInfo();
    messageInfo.setMsgid(msgid);
    messageInfo.setPhone(phones);
    messageInfo.setContent(content);
    messageInfo.setSign(sign);
    messageInfo.setSendTime(CommUtils.isNull(sendtime) ? "" : sendtime);
    messageInfo.setCreateTime(new Date());
    this.dhstMessageInfoRepos.save(messageInfo);
    return true;
  }
  
  public boolean saveReport(List<BwDhstMessageReport> list) {
    for (BwDhstMessageReport report : list) {
      this.dhstMessageReportRepos.save(report);
    }
    return true;
  }
  
  public boolean updatetInfo(List<BwDhstMessageReport> list) {
    for (BwDhstMessageReport report : list)  {
      UpdateQuery query = new UpdateQuery();
      query.setId(report.getWgcode());
      query.setIndexName("dhst_info");
      query.setType("es_message_info");
      Map<String, Object> updatemap = new HashMap();
      updatemap.put("desc", report.getDesc());
      UpdateRequest updateRequest = new UpdateRequest().doc(updatemap);
      query.setUpdateRequest(updateRequest);
      query.setDoUpsert(true);
    }
    return true;
  }
}
