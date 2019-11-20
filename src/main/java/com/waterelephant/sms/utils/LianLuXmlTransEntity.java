package com.waterelephant.sms.utils;

import com.waterelephant.sms.entity.lianlu.BwLianluMessageReport;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.xml.sax.InputSource;

public class LianLuXmlTransEntity
{
  public static List<BwLianluMessageReport> getXMLEntity(String text)
    throws DocumentException
  {
    SAXReader reader = new SAXReader();
    InputSource ins = new InputSource(new StringReader(text));
    ins.setEncoding("UTF-8");
    Document document = reader.read(ins);
    Element root = document.getRootElement();
    Iterator it2 = root.elementIterator("statusbox");
    List<BwLianluMessageReport> listData = new ArrayList();
    while (it2.hasNext())
    {
      Element item = (Element)it2.next();
      String mobile = item.elementTextTrim("mobile");
      String taskid = item.elementTextTrim("taskid");
      String status = item.elementTextTrim("status");
      String errorcode = item.elementTextTrim("errorcode");
      String receivetime = item.elementTextTrim("receivetime");
      BwLianluMessageReport reportEntity = new BwLianluMessageReport();
      reportEntity.setMobile(mobile);
      reportEntity.setTaskid(taskid);
      reportEntity.setStatus(status);
      reportEntity.setErrorcode(errorcode);
      reportEntity.setReceivetime(receivetime);
      reportEntity.setCreateTime(new Date());
      listData.add(reportEntity);
    }
    return listData;
  }
}
