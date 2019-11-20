package com.waterelephant.sms.utils;

import com.waterelephant.sms.stateVo.YimeiReportEntity;
import java.io.IOException;
import java.io.PrintStream;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import org.dom4j.DocumentException;
import org.dom4j.io.SAXReader;
import org.jdom.JDOMException;
import org.jdom.input.SAXBuilder;
import org.json.JSONObject;
import org.json.XML;
import org.xml.sax.InputSource;

public class YiMeiXmlTransEntity
{
  public static List<YimeiReportEntity> getXMLEntity(String text)
    throws DocumentException
  {
    SAXReader reader = new SAXReader();
    InputSource ins = new InputSource(new StringReader(text));
    ins.setEncoding("UTF-8");
    org.dom4j.Document document = reader.read(ins);
    org.dom4j.Element root = document.getRootElement();
    Iterator it2 = root.elementIterator("message");
    List<YimeiReportEntity> listData = new ArrayList();
    while (it2.hasNext())
    {
      org.dom4j.Element item = (org.dom4j.Element)it2.next();
      String srctermid = item.elementTextTrim("srctermid");
      String submitDate = item.elementTextTrim("submitDate");
      String receiveDate = item.elementTextTrim("receiveDate");
      String addSerial = item.elementTextTrim("addSerial");
      String addSerialRev = item.elementTextTrim("addSerialRev");
      String state = item.elementTextTrim("state");
      String seqid = item.elementTextTrim("seqid");
      YimeiReportEntity reportEntity = new YimeiReportEntity();
      reportEntity.setSrctermid(srctermid);
      reportEntity.setSubmitDate(submitDate);
      reportEntity.setReceiveDate(receiveDate);
      reportEntity.setAddSerial(addSerial);
      reportEntity.setAddSerialRev(addSerialRev);
      reportEntity.setState(state);
      reportEntity.setSeqid(seqid);
      listData.add(reportEntity);
    }
    return listData;
  }
  
  public static String xmlToJson(String xml)
  {
    JSONObject xmlJSONObject = XML.toJSONObject(xml);
    return xmlJSONObject.toString();
  }
  
  public static void main(String[] args)
    throws DocumentException, JDOMException, IOException
  {
    StringReader reader = new StringReader("<response><error>0</error><message></message></response>");
    SAXBuilder builder = new SAXBuilder();
    InputSource source = new InputSource(reader);
    org.jdom.Document document = builder.build(source);
    org.jdom.Element rootElement = document.getRootElement();
    System.out.println(rootElement.getChild("error").getText());
  }
}
