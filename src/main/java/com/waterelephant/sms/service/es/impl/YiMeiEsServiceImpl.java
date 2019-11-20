package com.waterelephant.sms.service.es.impl;

import com.waterelephant.sms.service.es.YiMeiEsService;
import com.waterelephant.sms.utils.DateUtil;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import org.elasticsearch.action.update.UpdateRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;
import org.springframework.stereotype.Service;

@Service
public class YiMeiEsServiceImpl implements YiMeiEsService {
	
  private Logger logger = LoggerFactory.getLogger(LianLuEsServiceImpl.class);
  
  @Autowired
  private ElasticsearchTemplate elasticsearchTemplate;
  
  public void saveInfo(String phone, String msg, String seqid, int chenal, int type) {
    UpdateQuery query = new UpdateQuery();
    query.setId(seqid);
    query.setIndexName("yimei_info");
    query.setType("es_message_info");
    Map<String, Object> map = new HashMap<>();
    map.put("phone", phone);
    map.put("msg", msg);
    map.put("state", Integer.valueOf(0));
    map.put("create_time", DateUtil.getDateString(new Date()));
    map.put("update_time", "");
    map.put("seqid", seqid);
    map.put("state_value", "");
    map.put("chenal", Integer.valueOf(chenal));
    map.put("type", Integer.valueOf(type));
    UpdateRequest updateRequest = new UpdateRequest().doc(map);
    query.setUpdateRequest(updateRequest);
    query.setDoUpsert(true);
    elasticsearchTemplate.update(query);
  }
  
  public void update(String seqid, int state, String stateValue, String phone) {
    UpdateQuery query = new UpdateQuery();
    query.setId(seqid);
    query.setIndexName("yimei_info");
    query.setType("es_message_info");
    Map<String, Object> updatemap = new HashMap<>();
    updatemap.put("update_time", DateUtil.getDateString(new Date()));
    updatemap.put("state", Integer.valueOf(state));
    updatemap.put("state_value", stateValue);
    UpdateRequest updateRequest = new UpdateRequest().doc(updatemap);
    query.setUpdateRequest(updateRequest);
    query.setDoUpsert(true);
    elasticsearchTemplate.update(query);
  }
  
  public void saveReport(String phone, String stateValue, String seqid, int chenal, int type)
  {
    UpdateQuery query = new UpdateQuery();
    query.setId(seqid);
    query.setIndexName("yimei_report");
    query.setType("es_message_report");
    Map<String, Object> updatemap = new HashMap();
    updatemap.put("create_time", DateUtil.getDateString(new Date()));
    updatemap.put("phone", phone);
    updatemap.put("chenal", Integer.valueOf(chenal));
    updatemap.put("seqid", seqid);
    updatemap.put("state_value", stateValue);
    updatemap.put("type", Integer.valueOf(type));
    updatemap.put("update_time", DateUtil.getDateString(new Date()));
    UpdateRequest updateRequest = new UpdateRequest().doc(updatemap);
    query.setUpdateRequest(updateRequest);
    query.setDoUpsert(true);
    elasticsearchTemplate.update(query);
  }
}
