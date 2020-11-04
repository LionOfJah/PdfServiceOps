package com.icicibank.apimgmt.model;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.stereotype.Component;

@Component
@XmlRootElement(name = "AcctDetails")
//@XmlAccessorType(XmlAccessType.PROPERTY)
public class AcctDetail implements Serializable {
    
   private static final long serialVersionUID = 1L;
	private String SerialNo;
	private String Account_number;
	private String DateOfBirth;
	private String Customer_address1;
	private String Customer_address2;
	private String Customer_City;
	private String Customer_State;
	private String Customer_name;
	
	private String Customer_Zip;
	private String TDS_AMT;
	private String INT_AMT;
	private String Cust_id;
	private String Start_date;
	private String Country;
	private String End_date;
	private String Schm_type;
	private String Branch_name;
	private String Branch_address1;
	private String Branch_address2;
	private String Branch_city;
	private String Branch_state;
	private String Branch_Pin_code;
	private String Currency_code;
	private String Schm_code;
	public String getSerialNo() {
		return SerialNo;
	}
	@XmlElement(name="SerialNo")
	public void setSerialNo(String serialNo) {
		SerialNo = serialNo;
	}
	public String getAccountnumber() {
		return Account_number;
	}
	@XmlElement(name="Account_number")
	public void setAccountnumber(String Account_number) {
		this.Account_number = Account_number;
	}
	public String getDateOfBirth() {
		return DateOfBirth;
	}
	@XmlElement(name="Date_Of_Birth")
	public void setDateOfBirth(String dateOfBirth) {
		DateOfBirth = dateOfBirth;
	}
	public String getCustomer_address1() {
		return Customer_address1;
	}
	@XmlElement(name="Customer_address1")
	public void setCustomer_address1(String customer_address1) {
		Customer_address1 = customer_address1;
	}
	public String getCustomer_address2() {
		return Customer_address2;
	}
	@XmlElement(name="Customer_address2")
	public void setCustomer_address2(String customer_address2) {
		Customer_address2 = customer_address2;
	}
	public String getCustomer_City() {
		return Customer_City;
	}
	@XmlElement(name="Customer_City")
	public void setCustomer_City(String customer_City) {
		Customer_City = customer_City;
	}
	public String getCustomer_State() {
		return Customer_State;
	}
	@XmlElement(name="Customer_State")
	public void setCustomer_State(String customer_State) {
		Customer_State = customer_State;
	}
	public String getCustomer_name() {
		return Customer_name;
	}
	@XmlElement(name="Customer_name")
	public void setCustomer_name(String customer_name) {
		Customer_name = customer_name;
	}
	public String getCustomer_Zip() {
		return Customer_Zip;
	}
	@XmlElement(name="Customer_Zip")
	public void setCustomer_Zip(String customer_Zip) {
		Customer_Zip = customer_Zip;
	}
	public String getTDS_AMT() {
		return TDS_AMT;
	}
	@XmlElement(name="TDS_AMT")
	public void setTDS_AMT(String tDS_AMT) {
		TDS_AMT = tDS_AMT;
	}
	public String getINT_AMT() {
		return INT_AMT;
	}
	@XmlElement(name="INT_AMT")
	public void setINT_AMT(String iNT_AMT) {
		INT_AMT = iNT_AMT;
	}
	public String getCust_id() {
		return Cust_id;
	}
	@XmlElement(name="Cust_id")
	public void setCust_id(String cust_id) {
		Cust_id = cust_id;
	}
	public String getStart_date() {
		return Start_date;
	}
	@XmlElement(name="Start_date")
	public void setStart_date(String start_date) {
		Start_date = start_date;
	}
	public String getCountry() {
		return Country;
	}
	@XmlElement(name="Country")
	public void setCountry(String country) {
		Country = country;
	}
	public String getEnd_date() {
		return End_date;
	}
	@XmlElement(name="End_date")
	public void setEnd_date(String end_date) {
		End_date = end_date;
	}
	public String getSchm_type() {
		return Schm_type;
	}
	@XmlElement(name="Schm_type")
	public void setSchm_type(String schm_type) {
		Schm_type = schm_type;
	}
	public String getBranch_name() {
		return Branch_name;
	}
	@XmlElement(name="Branch_name")
	public void setBranch_name(String branch_name) {
		Branch_name = branch_name;
	}
	public String getBranch_address1() {
		return Branch_address1;
	}
	@XmlElement(name="Branch_address1")
	public void setBranch_address1(String branch_address1) {
		Branch_address1 = branch_address1;
	}
	public String getBranch_address2() {
		return Branch_address2;
	}
	@XmlElement(name="Branch_address2")
	public void setBranch_address2(String branch_address2) {
		Branch_address2 = branch_address2;
	}
	public String getBranch_city() {
		return Branch_city;
	}
	@XmlElement(name="Branch_city")
	public void setBranch_city(String branch_city) {
		Branch_city = branch_city;
	}
	public String getBranch_state() {
		return Branch_state;
	}
	@XmlElement(name="Branch_state")
	public void setBranch_state(String branch_state) {
		Branch_state = branch_state;
	}
	public String getBranch_Pin_code() {
		return Branch_Pin_code;
	}
	@XmlElement(name="Branch_Pin_code")
	public void setBranch_Pin_code(String branch_Pin_code) {
		Branch_Pin_code = branch_Pin_code;
	}
	public String getCurrency_code() {
		return Currency_code;
	}
	@XmlElement(name="Currency_code")
	public void setCurrency_code(String currency_code) {
		Currency_code = currency_code;
	}
	public String getSchm_code() {
		return Schm_code;
	}
	@XmlElement(name="Schm_code")
	public void setSchm_code(String schm_code) {
		Schm_code = schm_code;
	}
}
