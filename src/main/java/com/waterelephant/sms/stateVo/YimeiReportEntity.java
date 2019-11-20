package com.waterelephant.sms.stateVo;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class YimeiReportEntity {
  private String srctermid;
  private String submitDate;
  private String receiveDate;
  private String addSerial;
  private String addSerialRev;
  private String state;
  private String seqid;
  
}
