package com.icicibank.apimgmt.model;

import java.io.Serializable;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;

@Component
@XmlRootElement(name="accDetailRequest")
public class accDetailRequest implements Serializable {
 
//	@XmlElementWrapper(name = "accDetailRequest")
    @XmlElement(name = "AcctDetails")
	private List<AcctDetail> AcctDetails;
	
	public List<AcctDetail> getAcctDetails() {
		return AcctDetails;
	}

	public void setAcctDetails(List<AcctDetail> acctDetails) {
		this.AcctDetails = acctDetails;
	}
	

}
