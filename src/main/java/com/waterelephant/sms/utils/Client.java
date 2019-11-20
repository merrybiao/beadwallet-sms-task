package com.waterelephant.sms.utils;



import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.waterelephant.sms.sdk.impl.SDKServiceBindingStub;
import com.waterelephant.sms.sdk.impl.SDKServiceLocator;
import com.waterelephant.sms.stateVo.StatusReport;


public class Client {
	private Logger logger = LoggerFactory.getLogger(Client.class);
	private String softwareSerialNo;
	private String key;
	public Client(String sn,String key){
		this.softwareSerialNo=sn;
		this.key=key;
		init();
	}
	
	SDKServiceBindingStub binding;
	
	
	public void init(){
		 try {
            binding = (SDKServiceBindingStub)
                          new SDKServiceLocator().getSDKService();
		 }
        catch (javax.xml.rpc.ServiceException jre) {
            if(jre.getLinkedCause()!=null)
                jre.getLinkedCause().printStackTrace();
        }
	}
	
	public List<StatusReport> getReport( )
			throws RemoteException {
		StatusReport[] sr=binding.getReport(softwareSerialNo, key);
		if(null!=sr){
			return Arrays.asList(sr);
		}else{
			return null;
		}
	}

	public String sendVoice(String[] mobiles, String smsContent, String addSerial,String srcCharset, int smsPriority,long smsID)
			throws RemoteException {
		     String value=null;
		     logger.info(softwareSerialNo + "******" + key);
		      value=binding.sendVoice(softwareSerialNo, key,"", mobiles, smsContent,addSerial, srcCharset, smsPriority,smsID);
		      return value;
	}
}
