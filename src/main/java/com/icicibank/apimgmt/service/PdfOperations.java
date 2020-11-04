package com.icicibank.apimgmt.service;

import org.springframework.stereotype.Service;

@Service
public interface PdfOperations {

	public String generatePdf(String Payload);
}
