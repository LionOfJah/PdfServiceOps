package com.icicibank.apimgmt.eventHandlers;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.io.font.FontProgram;
import com.itextpdf.io.font.FontProgramFactory;
import com.itextpdf.kernel.events.Event;
import com.itextpdf.kernel.events.IEventHandler;
import com.itextpdf.kernel.events.PdfDocumentEvent;
import com.itextpdf.kernel.font.PdfFont;
import com.itextpdf.kernel.font.PdfFontFactory;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.geom.Rectangle;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfPage;
import com.itextpdf.kernel.pdf.canvas.PdfCanvas;
import com.itextpdf.layout.Canvas;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.TextAlignment;
import com.itextpdf.layout.renderer.DocumentRenderer;
import com.itextpdf.layout.renderer.ImageRenderer;

public class ImageEventHandler implements IEventHandler {

	 protected Image img;
	 
	 private static float imageHeight;
	 
	 private Logger logger = LoggerFactory.getLogger(ImageEventHandler.class);
	 
	 private Document doc;
	 
	 
	    public ImageEventHandler(Image img,Document Doc) {
	        this.img = img;
	        this.doc=Doc;
	        img.setWidth(330);
	        img.setHeight(50);
	        ImageRenderer renderer = (ImageRenderer) img.createRendererSubTree();
	         renderer.setParent(new DocumentRenderer(doc));

	         
	         // Simulate the positioning of the renderer to find out how much space the header table will occupy.
	         LayoutResult result = renderer.layout(new LayoutContext(new LayoutArea(0, PageSize.A4)));
	         imageHeight = result.getOccupiedArea().getBBox().getHeight();
	         logger.info("imageHeight "+imageHeight);
	         logger.info("Top "+result.getOccupiedArea().getBBox().getTop()+" y "+result.getOccupiedArea().getBBox().getY());
	    }
	    @Override
	    public void handleEvent(Event event) {
	        PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
	        PdfDocument pdfDoc = docEvent.getDocument();
	        PdfPage page = docEvent.getPage();
	        PdfCanvas aboveCanvas = new PdfCanvas(page.newContentStreamBefore(),
	                page.getResources(), pdfDoc);
	        PageSize pageSize= pdfDoc.getDefaultPageSize();
	        float x = pageSize.getX()+doc.getLeftMargin()-5;
	        logger.info("Top "+pageSize.getTop()+" Top Margin "+doc.getTopMargin()+" table height "+TableEventHandler.getTableHeight()+" imageHeight "+imageHeight+" in image hanler");
	        float y = pageSize.getTop() - doc.getTopMargin()-TableEventHandler.getTableHeight()-imageHeight+32;
	        float textX = pageSize.getRight()-doc.getLeftMargin()-doc.getRightMargin()+10;
	        float textY=pageSize.getTop() - doc.getTopMargin()-TableEventHandler.getTableHeight()-5;
	        logger.info("coord y of image "+y);
	        logger.info("coord y of text "+textY);
	        float width = pageSize.getWidth() - doc.getRightMargin()-doc.getLeftMargin()+5;
	       
	        
	        logger.info("height of image "+imageHeight);
	        DateFormat df = new SimpleDateFormat("MMMM dd, yyyy");
			Date dateobj = new Date();
			
			
	        Rectangle area = new Rectangle(x, y, width, imageHeight);
	        new Canvas(aboveCanvas, pdfDoc, area)
	                .add(img)
	                .setFontSize(10)
	                .showTextAligned("Page "+pdfDoc.getPageNumber(page), textX, textY+20, TextAlignment.RIGHT)
	                .showTextAligned(df.format(dateobj), textX, textY, TextAlignment.RIGHT)
	                .close();
	    }
		public Image getImg() {
			return img;
		}
		public void setImg(Image img) {
			this.img = img;
		}
		public static float getImageHeight() {
			return imageHeight;
		}
		public static void setImageHeight(float imageHeight) {
			ImageEventHandler.imageHeight = imageHeight;
		}
		public Document getDoc() {
			return doc;
		}
		public void setDoc(Document doc) {
			this.doc = doc;
		}
}
