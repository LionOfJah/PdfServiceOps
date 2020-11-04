package com.icicibank.apimgmt.util;

import java.io.StringReader;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import com.icicibank.apimgmt.model.accDetailRequest;

public class XMLToObjectTest {
	public static void main(String[] args) {  
		 String XmlPayload = "<accDetailRequest>"+ 
				"<AcctDetails>\r\n" + 
		 		"<SerialNo>1</SerialNo>\r\n" + 
		 		"<Account_number>004024017200</Account_number>\r\n" + 
		 		"<Customer_name>SWOPNARANI NAYAK</Customer_name>\r\n" + 
		 		"<Date_Of_Birth>02-10-1987</Date_Of_Birth>\r\n" + 
		 		"<Customer_address1>1,DHRUV RESIDENCY ,4TH FLOOR,J V COLONY ,</Customer_address1>\r\n" + 
		 		"<Customer_address2>INDIRA NAGAR,NEAR KENDRIYA VIHAR</Customer_address2>\r\n" + 
		 		"<Customer_City>HYDERABAD</Customer_City>\r\n" + 
		 		"<Customer_State>TELANGANA</Customer_State>\r\n" + 
		 		"<Customer_Zip>500032</Customer_Zip>\r\n" + 
		 		"<TDS_AMT>0</TDS_AMT>\r\n" + 
		 		"<INT_AMT>20</INT_AMT>\r\n" + 
		 		"<Cust_id>533513466</Cust_id>\r\n" + 
		 		"<Start_date>01-04-2017</Start_date>\r\n" + 
		 		"<Country>IN</Country>\r\n" + 
		 		"<End_date>31-03-2018</End_date>\r\n" + 
		 		"<Schm_type>SB</Schm_type>\r\n" + 
		 		"<Branch_name>HIRANANDANI ESTATE-PATLIPADA</Branch_name>\r\n" + 
		 		"<Branch_address1>SHOP NO. 8 TO 11, SHR,</Branch_address1>\r\n" + 
		 		"<Branch_address2>HALL, HIRANANDANI BEHI</Branch_address2>\r\n" + 
		 		"<Branch_city>THA</Branch_city>\r\n" + 
		 		"<Branch_state>MH</Branch_state>\r\n" + 
		 		"<Branch_Pin_code>400607</Branch_Pin_code>\r\n" + 
		 		"<Currency_code>INR</Currency_code>\r\n" + 
		 		"<Schm_code>ICIVR</Schm_code>\r\n" + 
		 		"</AcctDetails>\r\n" + 
		 		"<AcctDetails>\r\n" + 
		 		"<SerialNo>2</SerialNo>\r\n" + 
		 		"<Account_number>004024014599</Account_number>\r\n" + 
		 		"<Customer_name>SWOPNARANI NAYAK</Customer_name>\r\n" + 
		 		"<Date_Of_Birth>02-10-1987</Date_Of_Birth>\r\n" + 
		 		"<Customer_address1>1,DHRUV RESIDENCY ,4TH FLOOR,J V COLONY ,</Customer_address1>\r\n" + 
		 		"<Customer_address2>INDIRA NAGAR,NEAR KENDRIYA VIHAR</Customer_address2>\r\n" + 
		 		"<Customer_City>HYDERABAD</Customer_City>\r\n" + 
		 		"<Customer_State>TELANGANA</Customer_State>\r\n" + 
		 		"<Customer_Zip>500032</Customer_Zip>\r\n" + 
		 		"<TDS_AMT>0</TDS_AMT>\r\n" + 
		 		"<INT_AMT>0</INT_AMT>\r\n" + 
		 		"<Cust_id>533513466</Cust_id>\r\n" + 
		 		"<Start_date>01-04-2017</Start_date>\r\n" + 
		 		"<Country>IN</Country>\r\n" + 
		 		"<End_date>31-03-2018</End_date>\r\n" + 
		 		"<Schm_type>SB</Schm_type>\r\n" + 
		 		"<Branch_name>HIRANANDANI ESTATE-PATLIPADA</Branch_name>\r\n" + 
		 		"<Branch_address1>SHOP NO. 8 TO 11, SHR,</Branch_address1>\r\n" + 
		 		"<Branch_address2>HALL, HIRANANDANI BEHI</Branch_address2>\r\n" + 
		 		"<Branch_city>THA</Branch_city>\r\n" + 
		 		"<Branch_state>MH</Branch_state>\r\n" + 
		 		"<Branch_Pin_code>400607</Branch_Pin_code>\r\n" + 
		 		"<Currency_code>INR</Currency_code>\r\n" + 
		 		"<Schm_code>ICIVR</Schm_code>\r\n" + 
		 		"</AcctDetails>" +
		 		"</accDetailRequest>";  
	     try {  
	   
//	 		    AccDetailRequest accDetails = (AccDetailRequest) jaxbUnmarshaller.unmarshal(new StringReader(XmlPayload));
//	 		    System.out.println(accDetails);

	        JAXBContext jaxbContext = JAXBContext.newInstance(accDetailRequest.class);  
	   
	        Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();  
	        accDetailRequest que= (accDetailRequest) jaxbUnmarshaller.unmarshal(new StringReader(XmlPayload));  
	          
	        System.out.println(que.getAcctDetails().size());  
	   
	      } catch (JAXBException e) {  
	        e.printStackTrace();  
	      }  
	   
	    }  
}
