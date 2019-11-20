package com.waterelephant.sms.service.es.impl;

import com.waterelephant.sms.elasticRepos.BwLianluMessageErrorRepos;
import com.waterelephant.sms.entity.lianlu.BwLianluMessageError;
import com.waterelephant.sms.service.es.LianLuEsService;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import org.elasticsearch.action.update.UpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;

@Service
public class LianLuEsServiceImpl implements LianLuEsService {
  private Logger logger = LoggerFactory.getLogger(LianLuEsServiceImpl.class);
  @Autowired
  private ElasticsearchTemplate elasticsearchTemplate;
  @Autowired
  private BwLianluMessageErrorRepos bwLianluMessageErrorRepos;
  
  public void saveInfo(String mobile, String content, String sign, Integer count, String taskid) {
    try {
      UpdateQuery query = new UpdateQuery();
      query.setId(taskid);
      query.setIndexName("lianlu_info");
      query.setType("es_message_info");
      Map<String, Object> map = new HashMap<>();
      map.put("content", content);
      map.put("createTime", Long.valueOf(System.currentTimeMillis()));
      map.put("mobile", mobile);
      map.put("count", count);
      map.put("msgid", UUID.randomUUID().toString().replace("-", ""));
      map.put("receivetime", "");
      map.put("status", "");
      map.put("errorcode", "");
      map.put("sign", sign);
      UpdateRequest updateRequest = new UpdateRequest().doc(map);
      query.setUpdateRequest(updateRequest);
      query.setDoUpsert(true);
      elasticsearchTemplate.update(query);
    }
    catch (Exception e)
    {
      logger.error(mobile + "联麓保存数据异常：{}", e.getMessage());
      e.printStackTrace();
    }
  }
  
  public void update(String taskid, String status, String receivetime, String errorcode) {
    try {
      UpdateQuery query = new UpdateQuery();
      query.setId(taskid);
      query.setIndexName("lianlu_info");
      query.setType("es_message_info");
      Map<String, Object> updatemap = new HashMap();
      updatemap.put("status", status);
      updatemap.put("receivetime", receivetime);
      updatemap.put("errorcode", errorcode);
      UpdateRequest updateRequest = new UpdateRequest().doc(updatemap);
      query.setUpdateRequest(updateRequest);
      query.setDoUpsert(true);
      elasticsearchTemplate.update(query);
    }
    catch (Exception e)
    {
      logger.error(taskid + "联麓更新数据异常：{}", e.getMessage());
      e.printStackTrace();
    }
  }
  
  public boolean saveReport(String mobile, String taskid, String status, String errorcode, String receivetime)
  {
    UpdateQuery query = new UpdateQuery();
    query.setId(taskid);
    query.setIndexName("lianlu_report");
    query.setType("es_message_report");
    Map<String, Object> updatemap = new HashMap();
    updatemap.put("createTime", Long.valueOf(System.currentTimeMillis()));
    updatemap.put("errorcode", errorcode);
    updatemap.put("mobile", mobile);
    updatemap.put("receivetime", receivetime);
    updatemap.put("status", status);
    updatemap.put("taskid", taskid);
    UpdateRequest updateRequest = new UpdateRequest().doc(updatemap);
    query.setUpdateRequest(updateRequest);
    query.setDoUpsert(true);
    elasticsearchTemplate.update(query);
    return true;
  }
  
  public boolean saveErrorInfo(BwLianluMessageError error) {
    bwLianluMessageErrorRepos.save(error);
    return true;
  }
}
