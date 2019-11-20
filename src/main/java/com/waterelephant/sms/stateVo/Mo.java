package com.waterelephant.sms.stateVo;

import java.io.Serializable;
import javax.xml.namespace.QName;
import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;
import org.apache.axis.encoding.Deserializer;
import org.apache.axis.encoding.Serializer;
import org.apache.axis.encoding.ser.BeanDeserializer;
import org.apache.axis.encoding.ser.BeanSerializer;

public class Mo implements Serializable {
  private String addSerial;
  private String addSerialRev;
  private String channelnumber;
  private String mobileNumber;
  private String sentTime;
  private String smsContent;
  
  public Mo(String addSerial, String addSerialRev, String channelnumber, String mobileNumber, String sentTime, String smsContent) {
    this.addSerial = addSerial;
    this.addSerialRev = addSerialRev;
    this.channelnumber = channelnumber;
    this.mobileNumber = mobileNumber;
    this.sentTime = sentTime;
    this.smsContent = smsContent;
  }
  
  public String getAddSerial() {
    return this.addSerial;
  }
  
  public void setAddSerial(String addSerial) {
    this.addSerial = addSerial;
  }
  
  public String getAddSerialRev() {
    return this.addSerialRev;
  }
  
  public void setAddSerialRev(String addSerialRev) {
    this.addSerialRev = addSerialRev;
  }
  
  public String getChannelnumber() {
    return this.channelnumber;
  }
  
  public void setChannelnumber(String channelnumber) {
    this.channelnumber = channelnumber;
  }
  
  public String getMobileNumber() {
    return this.mobileNumber;
  }
  
  public void setMobileNumber(String mobileNumber) {
    this.mobileNumber = mobileNumber;
  }
  
  public String getSentTime() {
    return this.sentTime;
  }
  
  public void setSentTime(String sentTime) {
    this.sentTime = sentTime;
  }
  
  public String getSmsContent() {
    return this.smsContent;
  }
  
  public void setSmsContent(String smsContent) {
    this.smsContent = smsContent;
  }
  
  private Object __equalsCalc = null;
  
  public synchronized boolean equals(Object obj){
    if (!(obj instanceof Mo)) {
      return false;
    }
    Mo other = (Mo)obj;
    if (obj == null) {
      return false;
    }
    if (this == obj) {
      return true;
    }
    if (this.__equalsCalc != null) {
      return this.__equalsCalc == obj;
    }
    this.__equalsCalc = obj;
    if ((this.addSerial != null) || (other.getAddSerial() != null)) {
      if (this.addSerial != null) {
        if (!this.addSerial.equals(other.getAddSerial())) {}
      }
    }
    else if ((this.addSerialRev != null) ||  (other.getAddSerialRev() != null)) {
      if (this.addSerialRev != null) {
        if (!this.addSerialRev.equals(other.getAddSerialRev())) {}
      }
    }
    else if ((this.channelnumber != null) ||  (other.getChannelnumber() != null)) {
      if (this.channelnumber != null) {
        if (!this.channelnumber.equals(other.getChannelnumber())) {}
      }
    }
    else if ((this.mobileNumber != null) ||  (other.getMobileNumber() != null)) {
      if (this.mobileNumber != null) {
        if (!this.mobileNumber.equals(other.getMobileNumber())) {}
      }
    }
    else if ((this.sentTime != null) || (other.getSentTime() != null)) {
      if (this.sentTime != null) {
        if (!this.sentTime.equals(other.getSentTime())) {}
      }
    }
    else if ((this.smsContent != null) ||  (other.getSmsContent() != null)) {
      if (this.smsContent == null) {
      }
    }
    boolean _equals = this.smsContent.equals(other.getSmsContent());
    this.__equalsCalc = null;
    return _equals;
  }
  
  private boolean __hashCodeCalc = false;
  
  public synchronized int hashCode()
  {
    if (this.__hashCodeCalc) {
      return 0;
    }
    this.__hashCodeCalc = true;
    int _hashCode = 1;
    if (getAddSerial() != null) {
      _hashCode += getAddSerial().hashCode();
    }
    if (getAddSerialRev() != null) {
      _hashCode += getAddSerialRev().hashCode();
    }
    if (getChannelnumber() != null) {
      _hashCode += getChannelnumber().hashCode();
    }
    if (getMobileNumber() != null) {
      _hashCode += getMobileNumber().hashCode();
    }
    if (getSentTime() != null) {
      _hashCode += getSentTime().hashCode();
    }
    if (getSmsContent() != null) {
      _hashCode += getSmsContent().hashCode();
    }
    this.__hashCodeCalc = false;
    return _hashCode;
  }
  
  private static TypeDesc typeDesc = new TypeDesc(Mo.class, true);
  
  static
  {
    typeDesc.setXmlType(new QName("http://sdkhttp.eucp.b2m.cn/", "mo"));
    ElementDesc elemField = new ElementDesc();
    elemField.setFieldName("addSerial");
    elemField.setXmlName(new QName("", "addSerial"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new ElementDesc();
    elemField.setFieldName("addSerialRev");
    elemField.setXmlName(new QName("", "addSerialRev"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new ElementDesc();
    elemField.setFieldName("channelnumber");
    elemField.setXmlName(new QName("", "channelnumber"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new ElementDesc();
    elemField.setFieldName("mobileNumber");
    elemField.setXmlName(new QName("", "mobileNumber"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new ElementDesc();
    elemField.setFieldName("sentTime");
    elemField.setXmlName(new QName("", "sentTime"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new ElementDesc();
    elemField.setFieldName("smsContent");
    elemField.setXmlName(new QName("", "smsContent"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
  }
  
  public static TypeDesc getTypeDesc()
  {
    return typeDesc;
  }
  
  public static Serializer getSerializer(String mechType, Class _javaType, QName _xmlType)
  {
    return new BeanSerializer(_javaType, _xmlType, typeDesc);
  }
  
  public static Deserializer getDeserializer(String mechType, Class _javaType, QName _xmlType)
  {
    return new BeanDeserializer(_javaType, _xmlType, typeDesc);
  }
  
  public Mo() {}
}
