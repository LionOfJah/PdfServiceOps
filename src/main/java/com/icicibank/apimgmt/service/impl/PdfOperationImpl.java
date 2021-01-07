package com.icicibank.apimgmt.service.impl;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.StringReader;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Unmarshaller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.icicibank.apimgmt.eventHandlers.FooterEventHandler;
import com.icicibank.apimgmt.eventHandlers.ImageEventHandler;
import com.icicibank.apimgmt.eventHandlers.TableEventHandler;
import com.icicibank.apimgmt.model.AcctDetail;
import com.icicibank.apimgmt.model.accDetailRequest;
import com.icicibank.apimgmt.service.PdfOperations;
import com.itextpdf.io.font.FontConstants;
import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.Style;
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Tab;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.AreaBreakType;
import com.itextpdf.layout.property.HorizontalAlignment;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.property.VerticalAlignment;
import com.itextpdf.layout.renderer.DocumentRenderer;
import com.itextpdf.layout.renderer.TableRenderer;

@Service
public class PdfOperationImpl implements PdfOperations {

	private static Logger logger = LoggerFactory.getLogger(PdfOperationImpl.class);

	@Autowired
	accDetailRequest AccountDetailrequest;

	@Value("${pdfFilePath}")
	private String FILE_NAME;

	private Properties props = new Properties();

	@Value("${logo}")
	private String logo;

	@Value("${fontFilePath}")
	private String fontFilePath;
	
	double totalInterestPaidSb = 0;
	double totalTaxPaidSb = 0;
	int serialNoSb=1;
	
	double totalInterestPaidTd = 0;
	double totalTaxPaidTd = 0;
	int serialNoTd=1;
	Style regularStyle = new Style();
	Style boldStyle = new Style();
	@Override
	public String generatePdf(String Payload) {
		JAXBContext jaxbContext;
		// accDetailRequest accDetailRequest = null;

		try {

			FontProgram fontPg = FontProgramFactory.createFont(fontFilePath);
			
			PdfFont regularFont = PdfFontFactory.createFont(FontConstants.HELVETICA);
			regularStyle.setFont(regularFont).setFontSize(10);

			
			PdfFont boldFont = PdfFontFactory.createFont(FontConstants.HELVETICA);
			boldStyle.setFont(boldFont).setBold().setFontSize(10);

			jaxbContext = JAXBContext.newInstance(accDetailRequest.class);
			Unmarshaller jaxbUnmarshaller = jaxbContext.createUnmarshaller();
			AccountDetailrequest = (accDetailRequest) jaxbUnmarshaller.unmarshal(new StringReader(Payload));
			logger.info("AccountDetailrequest " + AccountDetailrequest.toString());

			Map<String, ArrayList<AcctDetail>> acctDetails = sortAccountDetails(AccountDetailrequest);

			logger.info("Step-1 ");
			File file = new File(FILE_NAME);
			logger.info("Step-2");

			FileOutputStream fileOut = new FileOutputStream(file);
			PdfWriter pdfWriter = new PdfWriter(fileOut);
			PdfDocument pdfDocument = new PdfDocument(pdfWriter);
			pdfDocument.setDefaultPageSize(PageSize.A4);
			Document document = new Document(pdfDocument);

			TableEventHandler tableHandler = new TableEventHandler(document);

			pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, tableHandler);

			ImageData imageData = null;
			Image image = null;

			props.load(TableEventHandler.class.getClassLoader().getResourceAsStream("application.properties"));
			logger.info("Logo " + props.getProperty("logo"));
			imageData = ImageDataFactory.create(props.getProperty("logo"));

			image = new Image(imageData);

			ImageEventHandler imageHanlder = new ImageEventHandler(image, document);

			pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, imageHanlder);

			// logger.info("imageHanlder.getImageHeight() "+imageHanlder.getImageHeight());
			// document.setMargins(60+imageHanlder.getImageHeight(),document.getRightMargin(),
			// document.getLeftMargin(), document.getBottomMargin());

			// document.add(new AreaBreak(AreaBreakType.NEXT_AREA));

			Paragraph namePg = new Paragraph();
			
			namePg.add(new Text(AccountDetailrequest.getAcctDetails().get(0).getCustomer_name()).addStyle(boldStyle));
			
			Paragraph namePg1=new Paragraph();
			
			namePg1.add(new Text("Your Base Branch :").addStyle(boldStyle));
			
			/*
			 * if(AccountDetailrequest.getAcctDetails().get(0).getCustomer_name().contains(
			 * "&")) {
			 * 
			 * namePg.add(new
			 * Text(AccountDetailrequest.getAcctDetails().get(0).getCustomer_name()).
			 * addStyle(boldStyle)) .add(new Tab()).add(new Tab()) .add(new
			 * Text("Your Base Branch :").addStyle(boldStyle)); }else {
			 * 
			 * namePg.add(new
			 * Text(AccountDetailrequest.getAcctDetails().get(0).getCustomer_name()).
			 * addStyle(boldStyle)) .add(new Tab()).add(new Tab()).add(new Tab()).add(new
			 * Tab()) .add(new Text("Your Base Branch :").addStyle(boldStyle)); }
			 */
			
			
			
			PageSize pageSize = pdfDocument.getDefaultPageSize();
			float paraX = pdfDocument.getDefaultPageSize().getLeft() + document.getLeftMargin();
			float paraY = pageSize.getTop() - document.getTopMargin() - TableEventHandler.getTableHeight()
					- ImageEventHandler.getImageHeight() + 5;
			namePg.setFixedPosition(paraX, paraY, pageSize.getWidth());
			namePg1.setFixedPosition(paraX+310, paraY, pageSize.getWidth());
			logger.info("Para Y val " + paraY);
			paraY -= 20;
			document.add(namePg);
			document.add(namePg1);

			Paragraph p2 = new Paragraph(new Text(AccountDetailrequest.getAcctDetails().get(0).getCustomer_address1())
					.addStyle(regularStyle));
			/*
			 * if(AccountDetailrequest.getAcctDetails().get(0).getBranch_name().contains(" "
			 * )){ p2.add(new Tab()).add(new Tab()); }else { p2.add(new Tab()).add(new
			 * Tab()).add(new Tab()); }
			 */
			
			Paragraph p21=new Paragraph();
			//p2.add(new Tab()).add(new Tab());
			p21.add(new Text(AccountDetailrequest.getAcctDetails().get(0).getBranch_name()).addStyle(regularStyle));
			p2.setFixedPosition(paraX, paraY, pageSize.getWidth());
			p21.setFixedPosition(paraX+310, paraY, pageSize.getWidth());
			logger.info("Para Y val " + paraY);
			paraY -= 12;
			document.add(p2);
			document.add(p21);

			Paragraph p3 = new Paragraph(new Text(AccountDetailrequest.getAcctDetails().get(0).getCustomer_address2())
					.addStyle(regularStyle));
			//p3.add(new Tab()).add(new Tab()).add(new Tab());
			
			Paragraph p31=new Paragraph();
			
			p31.add(new Text(AccountDetailrequest.getAcctDetails().get(0).getBranch_address1()).addStyle(regularStyle));
			p3.setFixedPosition(paraX, paraY, pageSize.getWidth());
			p31.setFixedPosition(paraX+310, paraY, pageSize.getWidth());
			logger.info("Para Y val " + paraY);
			paraY -= 12;
			document.add(p3);
			document.add(p31);
			Paragraph p4 = new Paragraph(
					new Text(AccountDetailrequest.getAcctDetails().get(0).getCustomer_City()).addStyle(regularStyle));
			//p4.add(new Tab()).add(new Tab()).add(new Tab()).add(new Tab()).add(new Tab());
			
			Paragraph p41=new Paragraph();
			
			p41.add(new Text(AccountDetailrequest.getAcctDetails().get(0).getBranch_address2()).addStyle(regularStyle));
			p4.setFixedPosition(paraX, paraY, pageSize.getWidth());
			p41.setFixedPosition(paraX+310, paraY, pageSize.getWidth());
			logger.info("Para Y val " + paraY);
			paraY -= 12;
			document.add(p4);
			document.add(p41);
			Paragraph p5 = new Paragraph(
					new Text(AccountDetailrequest.getAcctDetails().get(0).getCustomer_State()).addStyle(regularStyle));
			//p5.add(new Tab()).add(new Tab()).add(new Tab()).add(new Tab()).add(new Tab());
			
			Paragraph p51 = new Paragraph();
			p51.add(new Text(AccountDetailrequest.getAcctDetails().get(0).getBranch_city() + ", "
					+ AccountDetailrequest.getAcctDetails().get(0).getBranch_state()).addStyle(regularStyle));
			p5.setFixedPosition(paraX, paraY, pageSize.getWidth());
			p51.setFixedPosition(paraX+310, paraY, pageSize.getWidth());
			logger.info("Para Y val " + paraY);
			paraY -= 12;
			document.add(p5);
			document.add(p51);

			Paragraph p6 = new Paragraph(
					new Text(AccountDetailrequest.getAcctDetails().get(0).getCustomer_Zip()).addStyle(regularStyle));
			//p6.add(new Tab()).add(new Tab()).add(new Tab()).add(new Tab()).add(new Tab()).add(new Tab());
			
			Paragraph p61=new Paragraph();
			p61.add(new Text(AccountDetailrequest.getAcctDetails().get(0).getBranch_Pin_code()).addStyle(regularStyle));
			p6.setFixedPosition(paraX, paraY, pageSize.getWidth());
			p61.setFixedPosition(paraX+310, paraY, pageSize.getWidth());
			
			logger.info("Para Y val " + paraY);
			paraY -= 30;
			document.add(p6);
			document.add(p61);
			
			Text label = new Text("Interest Certificate");
			label.addStyle(boldStyle);
			label.setUnderline(.8f, -5f);

			Paragraph labelPg = new Paragraph(label);
			labelPg.setHorizontalAlignment(HorizontalAlignment.CENTER);
			float labelPgPosX = (pageSize.getWidth() - document.getLeftMargin()) / 2 - document.getRightMargin();
			labelPg.setFixedPosition(labelPgPosX, paraY, pageSize.getWidth());
			document.add(labelPg);
			paraY -= 25;
			document.add(new Paragraph("Dear Customer,").addStyle(regularStyle).setFixedPosition(paraX, paraY,
					pageSize.getWidth()));
			paraY -= 10;
			document.add(new Paragraph(" ").addStyle(regularStyle).setFixedPosition(paraX, paraY, pageSize.getWidth()));
			paraY -= 25;
			document.add(new Paragraph(
					"Please find below confirmation of the Interest paid and Tax withheld/Tax Deducted at Source/Interest Collected towards "
							+ "various Deposit/Loan accounts held under Cust ID : "
							+ AccountDetailrequest.getAcctDetails().get(0).getCust_id() + " for the period "
							+ dateConvert(AccountDetailrequest.getAcctDetails().get(0).getStart_date()) + " to "
							+ dateConvert(AccountDetailrequest.getAcctDetails().get(0).getEnd_date())).addStyle(regularStyle)
									.setFixedPosition(paraX, paraY, pageSize.getWidth() - document.getRightMargin()));
			paraY -= 30;
			float[] columnWidth = { 2f };
			Table savingTableHeader = new Table(columnWidth);
			savingTableHeader.setWidthPercent(90);

			Cell headerCell = new Cell();
			headerCell.add("Account Type : Savings Account").addStyle(boldStyle);
			headerCell.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
			headerCell.setTextAlignment(TextAlignment.LEFT);
			headerCell.setBorder(Border.NO_BORDER);

			savingTableHeader.addCell(headerCell);
			savingTableHeader.setFixedPosition(paraX + 20, paraY,
					pageSize.getWidth() - document.getRightMargin() - document.getLeftMargin() - 30);
			document.add(savingTableHeader);

			float[] widths = { 0.10f, 0.20f, 0.20f, 0.20f };
			Table savingTable = new Table(widths);

			Cell headerRowCell1 = new Cell();
			headerRowCell1.add("Sr. No ").addStyle(regularStyle);
			headerRowCell1.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
			headerRowCell1.setTextAlignment(TextAlignment.CENTER);
			headerRowCell1.setWidth(35f);
			Border border = new SolidBorder(Color.LIGHT_GRAY, 1);

			headerRowCell1.setBorder(border);
			savingTable.addHeaderCell(headerRowCell1);

			Cell headerRowCell2 = new Cell();
			headerRowCell2.add("Account Number").addStyle(regularStyle);
			headerRowCell2.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
			headerRowCell2.setTextAlignment(TextAlignment.CENTER);
			headerRowCell2.setWidth(180);

			headerRowCell2.setBorder(border);
			savingTable.addHeaderCell(headerRowCell2);

			Cell headerRowCell3 = new Cell();
			headerRowCell3.add("Interest Paid (Amt. in INR)").addStyle(regularStyle);
			headerRowCell3.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
			headerRowCell3.setTextAlignment(TextAlignment.CENTER);
			headerRowCell3.setWidth(180);
			

			headerRowCell3.setBorder(border);
			savingTable.addHeaderCell(headerRowCell3);

			Cell headerRowCell4 = new Cell();
			headerRowCell4.add("Tax Withheld/ Tax deducted at source/ Interest Collected (Amt. in INR)")
					.addStyle(regularStyle);
			headerRowCell4.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
			headerRowCell4.setTextAlignment(TextAlignment.CENTER);
			headerRowCell4.setBorder(border);
			headerRowCell4.setWidth(180);
			savingTable.addHeaderCell(headerRowCell4);

		
			if (!acctDetails.isEmpty()) {
				List<AcctDetail> savingAcc = null;
				if (acctDetails.containsKey("SB")) {
					savingAcc = acctDetails.get("SB");
					float slice = 25;

					List<AcctDetail> subAccntList1 = null;
					List<AcctDetail> subTempAccntList = null;
					List<AcctDetail> tempAccntList = null;
					ArrayList<AcctDetail>[] arrayOfList = null;
					if (savingAcc.size() > 16) {

						subAccntList1 = savingAcc.subList(0, 16);
						subTempAccntList = savingAcc.subList(16, savingAcc.size());
						logger.info("subTempAccntList size"+subTempAccntList.size());
						float templistSize = Float.valueOf(subTempAccntList.size());
						float arraysize = templistSize / slice;
						if (arraysize > (int) arraysize && arraysize < (int) arraysize + .5) {
							arraysize += 1;
						}
						arraysize = Math.round(arraysize);
						arrayOfList = new ArrayList[(int) arraysize];
					} else if (savingAcc.size() <= 16) {
						subAccntList1 = savingAcc;
					}

					for (int i = 0; i < subAccntList1.size(); i++) {
						AcctDetail obj = subAccntList1.get(i);

						Cell snoCell = new Cell();
						snoCell.add(serialNoSb++ + "").addStyle(regularStyle);
						snoCell.setTextAlignment(TextAlignment.CENTER);
						snoCell.setWidth(35f);
						snoCell.setBorder(border);

						savingTable.addCell(snoCell);
						Cell acNoCell = new Cell();
						acNoCell.add(new Paragraph(obj.getAccountnumber()).setTextAlignment(TextAlignment.LEFT).setMarginLeft(12)).addStyle(regularStyle);
						acNoCell.setWidth(180);
						acNoCell.setBorder(border);
						savingTable.addCell(acNoCell);

						Cell IntAmt = new Cell();
						IntAmt.add(new Paragraph(Double.parseDouble(obj.getINT_AMT().toString()) + "").setTextAlignment(TextAlignment.RIGHT).setMarginRight(12)).addStyle(regularStyle);
						IntAmt.setWidth(180);
						IntAmt.setBorder(border);
						savingTable.addCell(IntAmt);

						totalInterestPaidSb = totalInterestPaidSb + Double.parseDouble(obj.getINT_AMT());

						Cell tdsAmt = new Cell();
						tdsAmt.add(new Paragraph(Double.parseDouble(obj.getTDS_AMT().toString()) + "").setTextAlignment(TextAlignment.RIGHT).setMarginRight(12)).addStyle(regularStyle);
						tdsAmt.setWidth(180);
						tdsAmt.setBorder(border);

						savingTable.addCell(tdsAmt);
						
						
						totalTaxPaidSb = totalTaxPaidSb + Double.parseDouble(obj.getTDS_AMT());
					}
					
					
					
					TableRenderer tableRenderer = (TableRenderer) savingTable.createRendererSubTree();
					tableRenderer.setParent(new DocumentRenderer(document));

					LayoutResult result = tableRenderer.layout(new LayoutContext(new LayoutArea(0, PageSize.A4)));
					float tableHeight = result.getOccupiedArea().getBBox().getHeight();
					float width=result.getOccupiedArea().getBBox().getWidth();
					logger.info(" width "+width);
					
					int methodCount=0;
					if (arrayOfList != null) {
						
						

						logger.info("inside footer event");
						paraY -=tableHeight+20;
						
						savingTable.setFixedPosition(paraX + 20, paraY,
								pageSize.getWidth() - document.getRightMargin() - document.getLeftMargin() - 30);
						document.add(savingTable);
						
						FooterEventHandler footerHandler = new FooterEventHandler(document);
						pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, footerHandler);
						double[] returnArr=null;
						for (int i = 0; i < arrayOfList.length; i++) {

							tempAccntList = new ArrayList<AcctDetail>();
							int counter = 0;
							
							if (subTempAccntList.size() <= 25) {
								tempAccntList = new ArrayList<AcctDetail>(subTempAccntList.subList(0, subTempAccntList.size()));
								arrayOfList[i] =  (ArrayList<AcctDetail>) tempAccntList;
								methodCount++;
								returnArr=writeTableToDocument(document, arrayOfList[i], methodCount,arrayOfList.length,serialNoSb,totalTaxPaidSb,totalInterestPaidSb);
								serialNoSb=(int) returnArr[0];
								totalTaxPaidSb=returnArr[1];
								totalInterestPaidSb=returnArr[2];
							} else {
								for (AcctDetail element : subTempAccntList) {
									if (counter >= 25) {

										subTempAccntList.removeAll(tempAccntList);
										arrayOfList[i]= (ArrayList<AcctDetail>) tempAccntList;
										methodCount++;
									
										returnArr=writeTableToDocument(document, arrayOfList[i],methodCount,arrayOfList.length,serialNoSb,totalTaxPaidSb,totalInterestPaidSb);
										serialNoSb=(int) returnArr[0];
										totalTaxPaidSb=returnArr[1];
										totalInterestPaidSb=returnArr[2];
										break;
									} else {
										
										logger.info("Counter "+counter);
										tempAccntList.add(element);
										counter++;
										
									}

								}

							}
						}
					}else {
						Cell emptyCell1 = new Cell();
						emptyCell1.add("").addStyle(regularStyle);
						emptyCell1.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
						emptyCell1.setBorder(border);
						emptyCell1.setWidth(35f);
						savingTable.addFooterCell(emptyCell1);

						Cell totalCell2 = new Cell();
						totalCell2.add(new Paragraph("Total").setTextAlignment(TextAlignment.LEFT).setMarginLeft(12)).addStyle(boldStyle);
						totalCell2.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
						totalCell2.setBorder(border);
						totalCell2.setWidth(180);
						savingTable.addFooterCell(totalCell2);

						Cell intPaid = new Cell();
						intPaid.add(new Paragraph(totalInterestPaidSb + "").setTextAlignment(TextAlignment.RIGHT).setMarginRight(12)).addStyle(boldStyle);
						intPaid.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
						intPaid.setBorder(border);
						intPaid.setWidth(180);

						savingTable.addFooterCell(intPaid);

						Cell tdsPaid = new Cell();
						tdsPaid.add(new Paragraph(totalTaxPaidSb + "").setTextAlignment(TextAlignment.RIGHT).setMarginRight(12)).addStyle(boldStyle);
						tdsPaid.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
						tdsPaid.setBorder(border);
						tdsPaid.setWidth(180);

						savingTable.addFooterCell(tdsPaid);

						

						LayoutResult result1 = tableRenderer.layout(new LayoutContext(new LayoutArea(0, PageSize.A4)));
						float tableHeight1 = result1.getOccupiedArea().getBBox().getHeight();

						paraY -=tableHeight1-3;
						
						savingTable.setFixedPosition(paraX + 20, paraY,
								pageSize.getWidth() - document.getRightMargin() - document.getLeftMargin() - 30);
						
						document.add(savingTable);
						
						FooterEventHandler footerHandler = new FooterEventHandler(document);
						pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, footerHandler);
						
						
						
						
					}

				
				}

			}
			serialNoSb=1;
			totalTaxPaidSb=0;
			totalInterestPaidSb=0;
			
			//Code to Add TD account Data
			/***********************************************************************************************************/
			document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
			

			pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, tableHandler);


			pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, imageHanlder);
			
			float paraXtd = document.getPdfDocument().getDefaultPageSize().getLeft() + document.getLeftMargin();
			float paraYtd = pageSize.getTop() - document.getTopMargin() - TableEventHandler.getTableHeight()
					- ImageEventHandler.getImageHeight() + 5;
			
			float[] columnWidthTd = { 2f };
			Table tdTableHeader = new Table(columnWidthTd);
			tdTableHeader.setWidthPercent(90);

			Cell headerCellTd = new Cell();
			headerCellTd.add("Account Type : Fixed Deposits / Recurring Deposits").addStyle(boldStyle);
			headerCellTd.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
			headerCellTd.setTextAlignment(TextAlignment.LEFT);
			headerCellTd.setBorder(Border.NO_BORDER);

			tdTableHeader.addCell(headerCellTd);
			tdTableHeader.setFixedPosition(paraXtd + 20, paraYtd,
					pageSize.getWidth() - document.getRightMargin() - document.getLeftMargin() - 30);
			document.add(tdTableHeader);

			float[] widthsTd = { 0.08f, 0.20f, 0.20f, 0.20f };
			Table tdTable = new Table(widthsTd);

			Cell headerRowCell1Td = new Cell();
			headerRowCell1Td.add("Sr. No ").addStyle(regularStyle);
			headerRowCell1Td.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
			headerRowCell1Td.setTextAlignment(TextAlignment.CENTER);
			headerRowCell1Td.setWidth(35f);
			//Border borderTd = new SolidBorder(Color.LIGHT_GRAY, 1);

			headerRowCell1Td.setBorder(border);
			tdTable.addHeaderCell(headerRowCell1Td);

			Cell headerRowCell2Td = new Cell();
			headerRowCell2Td.add("Account Number").addStyle(regularStyle);
			headerRowCell2Td.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
			headerRowCell2Td.setTextAlignment(TextAlignment.CENTER);
			headerRowCell2Td.setWidth(180);
			headerRowCell2Td.setBorder(border);
			tdTable.addHeaderCell(headerRowCell2Td);

			Cell headerRowCell3Td = new Cell();
			headerRowCell3Td.add("Interest Paid (Amt. in INR)").addStyle(regularStyle);
			headerRowCell3Td.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
			headerRowCell3Td.setTextAlignment(TextAlignment.CENTER);
			headerRowCell3Td.setWidth(180);
			headerRowCell3Td.setBorder(border);
			tdTable.addHeaderCell(headerRowCell3Td);

			Cell headerRowCell4Td = new Cell();
			headerRowCell4Td.add("Tax Withheld/ Tax deducted at source/ Interest Collected (Amt. in INR)")
					.addStyle(regularStyle);
			headerRowCell4Td.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
			headerRowCell4Td.setTextAlignment(TextAlignment.CENTER);
			headerRowCell4Td.setWidth(180);
			headerRowCell4Td.setBorder(border);
			tdTable.addHeaderCell(headerRowCell4Td);
			
			if (!acctDetails.isEmpty()) {
				List<AcctDetail> tdAcc = null;
				if (acctDetails.containsKey("TD")) {
					tdAcc = acctDetails.get("TD");
					float slice = 25;

					List<AcctDetail> subAccntList1 = null;
					List<AcctDetail> subTempAccntList = null;
					List<AcctDetail> tempAccntList = null;
					ArrayList<AcctDetail>[] arrayOfList = null;
					if (tdAcc.size() > slice) {

						subAccntList1 = tdAcc.subList(0, (int)slice);
						subTempAccntList = tdAcc.subList((int)slice, tdAcc.size());
						float templistSize = Float.valueOf(subTempAccntList.size());
						float arraysize = templistSize / slice;
						if (arraysize > (int) arraysize && arraysize < (int) arraysize + .5) {
							arraysize += 1;
						}
						arraysize = Math.round(arraysize);
						arrayOfList = new ArrayList[(int) arraysize];
					} else if (tdAcc.size() <= slice) {
						subAccntList1 = tdAcc;
					}

					for (int i = 0; i < subAccntList1.size(); i++) {
						AcctDetail obj = subAccntList1.get(i);

						Cell snoCell = new Cell();
						snoCell.add((int)serialNoTd++ + "").addStyle(regularStyle);
						snoCell.setTextAlignment(TextAlignment.CENTER);
						snoCell.setWidth(35f);
						snoCell.setBorder(border);

						tdTable.addCell(snoCell);
						Cell acNoCell = new Cell();
						acNoCell.add(new Paragraph(obj.getAccountnumber()).setTextAlignment(TextAlignment.LEFT).setMarginLeft(12)).addStyle(regularStyle);
						acNoCell.setWidth(180);
						acNoCell.setBorder(border);
						tdTable.addCell(acNoCell);

						Cell IntAmt = new Cell();
						IntAmt.addStyle(regularStyle);
						IntAmt.add(new Paragraph(Double.parseDouble(obj.getINT_AMT().toString()) + "").setTextAlignment(TextAlignment.RIGHT).setMarginRight(12));
						IntAmt.setTextAlignment(TextAlignment.RIGHT);
						IntAmt.setWidth(180);
						IntAmt.setBorder(border);
						tdTable.addCell(IntAmt);

						totalInterestPaidTd = totalInterestPaidTd + Double.parseDouble(obj.getINT_AMT());

						Cell tdsAmt = new Cell();
						tdsAmt.addStyle(regularStyle);
						tdsAmt.add(new Paragraph(Double.parseDouble(obj.getTDS_AMT().toString()) + "").setTextAlignment(TextAlignment.RIGHT).setMarginRight(12));
						tdsAmt.setWidth(180);
						tdsAmt.setBorder(border);

						tdTable.addCell(tdsAmt);
						
						
						totalTaxPaidTd = totalTaxPaidTd + Double.parseDouble(obj.getTDS_AMT());
						logger.info("totalInterestPaidTd "+totalInterestPaidTd+" totalTaxPaidTd "+totalTaxPaidTd);
					}
					
					
					TableRenderer tableRenderer = (TableRenderer) tdTable.createRendererSubTree();
					tableRenderer.setParent(new DocumentRenderer(document));

					LayoutResult result = tableRenderer.layout(new LayoutContext(new LayoutArea(0, PageSize.A4)));
					float tableHeight = result.getOccupiedArea().getBBox().getHeight();
					
					int methodCount=0;
					if (arrayOfList != null) {
						
						

						paraYtd -=tableHeight+20;
						
						tdTable.setFixedPosition(paraX + 20, paraYtd,
								pageSize.getWidth() - document.getRightMargin() - document.getLeftMargin() - 30);
						document.add(tdTable);
						
						FooterEventHandler footerHandler = new FooterEventHandler(document);
						pdfDocument.addEventHandler(PdfDocumentEvent.END_PAGE, footerHandler);
						double[] returnArray=null;
						for (int i = 0; i < arrayOfList.length; i++) {

							tempAccntList = new ArrayList<AcctDetail>();
							int counter = 1;
							
							if (subTempAccntList.size() < 25) {
								tempAccntList = new ArrayList<AcctDetail>(subTempAccntList.subList(0, subTempAccntList.size()));
								arrayOfList[i] =  (ArrayList<AcctDetail>) tempAccntList;
								methodCount++;
								returnArray=writeTableToDocument(document, arrayOfList[i], methodCount,arrayOfList.length,serialNoTd,totalTaxPaidTd,totalInterestPaidTd);
								serialNoTd=(int) returnArray[0];
								totalTaxPaidTd=returnArray[1];
								totalInterestPaidTd=returnArray[2];
							} else {
								for (AcctDetail element : subTempAccntList) {
									if (counter >= 25) {

										subTempAccntList.removeAll(tempAccntList);
										arrayOfList[i]= (ArrayList<AcctDetail>) tempAccntList;
										methodCount++;
										returnArray=writeTableToDocument(document, arrayOfList[i],methodCount,arrayOfList.length,serialNoTd,totalTaxPaidTd,totalInterestPaidTd);
										serialNoTd=(int) returnArray[0];
										totalTaxPaidTd=returnArray[1];
										totalInterestPaidTd=returnArray[2];
										break;
									} else {
										tempAccntList.add(element);
										counter++;
									}

								}

							}
						}
					}else {
						Cell emptyCell1 = new Cell();
						emptyCell1.add("").addStyle(regularStyle);
						emptyCell1.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
						emptyCell1.setBorder(border);
						emptyCell1.setWidth(35f);
						tdTable.addFooterCell(emptyCell1);

						Cell totalCell2 = new Cell();
						totalCell2.add(new Paragraph("Total").setTextAlignment(TextAlignment.LEFT).setMarginLeft(12)).addStyle(boldStyle);
						totalCell2.setWidth(180);
						totalCell2.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
						totalCell2.setBorder(border);
						tdTable.addFooterCell(totalCell2);

						Cell intPaid = new Cell();
						intPaid.add(new Paragraph(totalInterestPaidTd + "").setTextAlignment(TextAlignment.RIGHT).setMarginRight(12)).addStyle(boldStyle);
						intPaid.setWidth(180);
						intPaid.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
						intPaid.setBorder(border);

						tdTable.addFooterCell(intPaid);

						Cell tdsPaid = new Cell();
						tdsPaid.add(new Paragraph(totalTaxPaidTd + "").setTextAlignment(TextAlignment.RIGHT).setMarginRight(12)).addStyle(boldStyle);
						tdsPaid.setWidth(180);
						tdsPaid.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
						tdsPaid.setBorder(border);

						tdTable.addFooterCell(tdsPaid);

						

						LayoutResult result1 = tableRenderer.layout(new LayoutContext(new LayoutArea(0, PageSize.A4)));
						float tableHeight1 = result1.getOccupiedArea().getBBox().getHeight();

						paraYtd -=tableHeight1-3;
						
						tdTable.setFixedPosition(paraX + 20, paraYtd,
								pageSize.getWidth() - document.getRightMargin() - document.getLeftMargin() - 30);
						
						document.add(tdTable);
						
						
						
						
					}

				
				}

			}


			serialNoTd=1;
			totalInterestPaidTd=0;
			totalTaxPaidTd=0;
			
			document.close();
			fileOut.close();
			
			byte[] bytesArray = new byte[(int) file.length()];
			FileInputStream fis = new FileInputStream(file);
			fis.read(bytesArray);
			fis.close();
			Base64.Encoder encoder = Base64.getEncoder();
			String encoded = encoder.encodeToString(bytesArray);
			logger.info("File Created:: " + encoded);
			return encoded;
		} catch (JAXBException e) {
			logger.info(e.getMessage() + " with error code " + e.getErrorCode());
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			logger.info(e.getMessage());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.info(e.getMessage());
		}

		return null;
	}

	public double[] writeTableToDocument(Document doc, List<AcctDetail> inputList,int methodCount,int arrayLength,double serialNo,double totalTaxPaid,double totalInterestPaid) {

		logger.info("serailNO "+serialNo+" totalTaxPaid "+totalTaxPaid+" totalInterestPaid "+totalInterestPaid);
		logger.info("input size list "+inputList.size());
		doc.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
		TableEventHandler tableHandler = new TableEventHandler(doc);

		doc.getPdfDocument().addEventHandler(PdfDocumentEvent.END_PAGE, tableHandler);
		PageSize pageSize=doc.getPdfDocument().getDefaultPageSize();
		float paraX = doc.getPdfDocument().getDefaultPageSize().getLeft() + doc.getLeftMargin();
		float paraY = pageSize.getTop() - doc.getTopMargin() - TableEventHandler.getTableHeight()
				- ImageEventHandler.getImageHeight() + 5;

		ImageData imageData = null;
		Image image = null;

		try {
			props.load(TableEventHandler.class.getClassLoader().getResourceAsStream("application.properties"));
			logger.info("Logo " + props.getProperty("logo"));
			imageData = ImageDataFactory.create(props.getProperty("logo"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	

		image = new Image(imageData);

		ImageEventHandler imageHanlder = new ImageEventHandler(image, doc);

		doc.getPdfDocument().addEventHandler(PdfDocumentEvent.END_PAGE, imageHanlder);
		
		float[] widths = { 0.08f, 0.20f, 0.20f, 0.20f };
		Table savingTable = new Table(widths);

		Cell headerRowCell1 = new Cell();
		headerRowCell1.add("Sr. No ").addStyle(regularStyle);
		headerRowCell1.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
		headerRowCell1.setTextAlignment(TextAlignment.CENTER);
		Border border = new SolidBorder(Color.LIGHT_GRAY, 1);
		headerRowCell1.setWidth(35f);

		headerRowCell1.setBorder(border);
		savingTable.addHeaderCell(headerRowCell1);

		Cell headerRowCell2 = new Cell();
		headerRowCell2.add("Account Number").addStyle(regularStyle);
		headerRowCell2.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
		headerRowCell2.setTextAlignment(TextAlignment.CENTER);
		headerRowCell2.setWidth(180);
		headerRowCell2.setBorder(border);
		savingTable.addHeaderCell(headerRowCell2);

		Cell headerRowCell3 = new Cell();
		headerRowCell3.add("Interest Paid (Amt. in INR)").addStyle(regularStyle);
		headerRowCell3.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
		headerRowCell3.setTextAlignment(TextAlignment.CENTER);
		headerRowCell3.setWidth(180);
		headerRowCell3.setBorder(border);
		savingTable.addHeaderCell(headerRowCell3);

		Cell headerRowCell4 = new Cell();
		headerRowCell4.add("Tax Withheld/ Tax deducted at source/ Interest Collected (Amt. in INR)")
				.addStyle(regularStyle);
		headerRowCell4.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
		headerRowCell4.setTextAlignment(TextAlignment.CENTER);
		headerRowCell4.setWidth(180);
		headerRowCell4.setBorder(border);
		savingTable.addHeaderCell(headerRowCell4);
		
		
		for (int i = 0; i < inputList.size(); i++) {
			AcctDetail obj = inputList.get(i);

			Cell snoCell = new Cell();
			snoCell.addStyle(regularStyle);
			snoCell.add((int)serialNo++ + "").addStyle(regularStyle).setTextAlignment(TextAlignment.CENTER);
			snoCell.setWidth(35f);
			snoCell.setBorder(border);
			savingTable.addCell(snoCell);
			
			Cell acNoCell = new Cell();
			acNoCell.addStyle(regularStyle);
			acNoCell.add(new Paragraph(obj.getAccountnumber()).setTextAlignment(TextAlignment.LEFT).setMarginLeft(12)).addStyle(regularStyle);
			acNoCell.setWidth(180);
			acNoCell.setBorder(border);
			savingTable.addCell(acNoCell);

			Cell IntAmt = new Cell();
			IntAmt.addStyle(regularStyle);
			IntAmt.add(new Paragraph(Double.parseDouble(obj.getINT_AMT().toString()) + "").setTextAlignment(TextAlignment.RIGHT).setMarginRight(12));
			IntAmt.setWidth(180);
			IntAmt.setBorder(border);
			savingTable.addCell(IntAmt);

			totalInterestPaid = totalInterestPaid + Double.parseDouble(obj.getINT_AMT());

			Cell tdsAmt = new Cell();
			tdsAmt.addStyle(regularStyle);
			tdsAmt.add(new Paragraph(Double.parseDouble(obj.getTDS_AMT().toString()) + "").setTextAlignment(TextAlignment.RIGHT).setMarginRight(12));
			tdsAmt.setHorizontalAlignment(HorizontalAlignment.RIGHT);
			tdsAmt.setVerticalAlignment(VerticalAlignment.BOTTOM);
			tdsAmt.setBorder(border);

			savingTable.addCell(tdsAmt);
			
			totalTaxPaid = totalTaxPaid + Double.parseDouble(obj.getTDS_AMT());
		}
		
		if(methodCount==arrayLength) {
			Cell emptyCell1 = new Cell();
			emptyCell1.add("").addStyle(regularStyle);
			emptyCell1.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
			emptyCell1.setBorder(border);
			emptyCell1.setWidth(135f);
			savingTable.addFooterCell(emptyCell1);

			Cell totalCell2 = new Cell();
			totalCell2.add(new Paragraph("Total").setTextAlignment(TextAlignment.LEFT).setMarginLeft(12)).addStyle(boldStyle);
			totalCell2.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
			totalCell2.setBorder(border);
			totalCell2.setWidth(180);
			savingTable.addFooterCell(totalCell2);

			Cell intPaid = new Cell();
			intPaid.add(new Paragraph(totalInterestPaid + "").setTextAlignment(TextAlignment.RIGHT).setMarginRight(12)).addStyle(boldStyle);
			intPaid.setWidth(180);
			intPaid.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
			intPaid.setBorder(border);

			savingTable.addFooterCell(intPaid);

			Cell tdsPaid = new Cell();
			tdsPaid.add(new Paragraph(totalTaxPaid + "").setTextAlignment(TextAlignment.RIGHT).setMarginRight(12)).addStyle(boldStyle);
			tdsPaid.setWidth(180);
			tdsPaid.setBackgroundColor(Color.LIGHT_GRAY, 0.5f);
			tdsPaid.setBorder(border);

			savingTable.addFooterCell(tdsPaid);
			
		}
		
		TableRenderer tableRenderer = (TableRenderer) savingTable.createRendererSubTree();
		tableRenderer.setParent(new DocumentRenderer(doc));

		LayoutResult result = tableRenderer.layout(new LayoutContext(new LayoutArea(0, PageSize.A4)));
		float tableHeight = result.getOccupiedArea().getBBox().getHeight();
		paraY-=tableHeight-5;
		savingTable.setFixedPosition(paraX+20, paraY, pageSize.getWidth() - doc.getRightMargin() - doc.getLeftMargin() - 30);

		
		doc.add(savingTable);

		double[] returnArr= {serialNo,totalTaxPaid,totalInterestPaid};
		return returnArr;
	}

	public Map sortAccountDetails(accDetailRequest accDetailRequestObj) {
		List<AcctDetail> savingAcc = new ArrayList<AcctDetail>();
		List<AcctDetail> otherAcc = new ArrayList<AcctDetail>();
		for (int i = 0; i < accDetailRequestObj.getAcctDetails().size(); i++) {
			if (accDetailRequestObj.getAcctDetails().get(i).getSchm_type().trim().equals("SB")) {
				savingAcc.add(accDetailRequestObj.getAcctDetails().get(i));
			} else {
				otherAcc.add(accDetailRequestObj.getAcctDetails().get(i));
			}
		}
		Map<String, List<AcctDetail>> acctDetails = new HashMap<String, List<AcctDetail>>();
		acctDetails.put("SB", savingAcc);
		acctDetails.put("TD", otherAcc);

		return acctDetails;
	}

	private String dateConvert(String dateToConvert) {
		
		
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd-MM-yyyy", Locale.ENGLISH);
		DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("MMM dd, yyyy", Locale.ENGLISH);
		LocalDate ld = LocalDate.parse(dateToConvert, dtf);
		String month_name = dtf2.format(ld);
		System.out.println(month_name);
		return month_name;
	}
}
