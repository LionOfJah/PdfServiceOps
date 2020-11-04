package com.icicibank.apimgmt.eventHandlers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.property.TextAlignment;


public class FooterEventHandler implements IEventHandler{

	
	private Document doc;
	
	private Logger logger =LoggerFactory.getLogger(FooterEventHandler.class);
	public FooterEventHandler(Document doc) {
		super();
		this.doc = doc;
	}
	
	@Override
	public void handleEvent(Event event) {
		PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
        PdfDocument pdfDoc = docEvent.getDocument();
        PdfPage page = docEvent.getPage();
        PdfCanvas aboveCanvas = new PdfCanvas(page.newContentStreamBefore(),
                page.getResources(), pdfDoc);
        PageSize pageSize= pdfDoc.getDefaultPageSize();
        
        float x = pageSize.getX()+doc.getLeftMargin();
        //logger.info("Top "+pageSize.getTop()+" Top Margin "+doc.getTopMargin()+" table height "+TableEventHandler.getTableHeight()+" imageHeight "+imageHeight+" in image hanler");
        float y = pageSize.getBottom()+ doc.getBottomMargin()+80;
        float textX = x+10;
        float textY=y-20;
        logger.info("coord y of image "+y);
        logger.info("coord y of text "+textY);
        float width = pageSize.getWidth() -doc.getLeftMargin()+5;
        
        Rectangle area = new Rectangle(x, y, width, y);
        
        aboveCanvas.moveTo(x, y);
        aboveCanvas.lineTo(pageSize.getRight(), y);
        aboveCanvas.stroke();
        
		
		  new Canvas(aboveCanvas, pdfDoc, area).setFontSize(9)
		  .showTextAligned(new Paragraph("Corporate Office: ").setBold(), textX, textY, TextAlignment.LEFT)
		  .showTextAligned(new Paragraph("ICICI Bank Ltd., ICICI Bank Towers, Bandra-Kurla complex, Mumbai - 400051, India"), textX+90, textY, TextAlignment.LEFT).
		  showTextAligned(new Paragraph("Registered Office: ").setBold(), textX, textY-20, TextAlignment.LEFT)
		  .showTextAligned(new Paragraph("ICICI Bank Tower, Near Chakli Circle, Old Padra Road, Vadodara, Gujarat. Pin – 390 007"), textX+90, textY-20, TextAlignment.LEFT)
		  .showTextAligned(new Paragraph("This is an authenticated intimation/statement."), textX, textY-50, TextAlignment.LEFT)
		  .showTextAligned(new Paragraph("Customers are requested to immediately notify the Bank of any discrepancy in the statement."), textX, textY-70, TextAlignment.LEFT)
		  .close();
		 
		
	}

}
