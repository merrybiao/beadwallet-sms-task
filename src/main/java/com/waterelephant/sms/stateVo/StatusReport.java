package com.waterelephant.sms.stateVo;

import java.io.Serializable;

import javax.xml.namespace.QName;

import org.apache.axis.description.ElementDesc;
import org.apache.axis.description.TypeDesc;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class StatusReport implements Serializable {
  /**
	 * 
	 */
  private static final long serialVersionUID = 1L;
  private String errorCode;
  private String memo;
  private String mobile;
  private String receiveDate;
  private int reportStatus;
  private long seqID;
  private String serviceCodeAdd;
  private String submitDate;
  
  
  private static TypeDesc typeDesc = new TypeDesc(StatusReport.class, true);
  
  static {
    typeDesc.setXmlType(new QName("http://sdkhttp.eucp.b2m.cn/", "statusReport"));
    ElementDesc elemField = new ElementDesc();
    elemField.setFieldName("errorCode");
    elemField.setXmlName(new QName("", "errorCode"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new ElementDesc();
    elemField.setFieldName("memo");
    elemField.setXmlName(new QName("", "memo"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new ElementDesc();
    elemField.setFieldName("mobile");
    elemField.setXmlName(new QName("", "mobile"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new ElementDesc();
    elemField.setFieldName("receiveDate");
    elemField.setXmlName(new QName("", "receiveDate"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new ElementDesc();
    elemField.setFieldName("reportStatus");
    elemField.setXmlName(new QName("", "reportStatus"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "int"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new ElementDesc();
    elemField.setFieldName("seqID");
    elemField.setXmlName(new QName("", "seqID"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "long"));
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new ElementDesc();
    elemField.setFieldName("serviceCodeAdd");
    elemField.setXmlName(new QName("", "serviceCodeAdd"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
    elemField = new ElementDesc();
    elemField.setFieldName("submitDate");
    elemField.setXmlName(new QName("", "submitDate"));
    elemField.setXmlType(new QName("http://www.w3.org/2001/XMLSchema", "string"));
    elemField.setMinOccurs(0);
    elemField.setNillable(false);
    typeDesc.addFieldDesc(elemField);
  }
  
}
