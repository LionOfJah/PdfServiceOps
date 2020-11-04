package com.icicibank.apimgmt.controller;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.icicibank.apimgmt.service.PdfOperations;

@RestController
@RequestMapping("/api/v0")
public class PdfApiController {

	
	@Autowired
	PdfOperations pdfOpsService;
	
	private Logger logger = LoggerFactory.getLogger(PdfApiController.class);
	
	@PostMapping(value = "/generatePdf",consumes = "application/xml" , produces = "text/plain")
	public ResponseEntity<String> generatePdf(InputStream input) {
		
		BufferedReader reader = new BufferedReader(new InputStreamReader(input));
		
		String xmlPayload = reader.lines().reduce("", (str1,str2)->str1+str2);
		logger.info("XmlPayLoad  "+xmlPayload);
		String response=pdfOpsService.generatePdf(xmlPayload);
		   
		
		
		return ResponseEntity.ok().body(response);
	}
}
