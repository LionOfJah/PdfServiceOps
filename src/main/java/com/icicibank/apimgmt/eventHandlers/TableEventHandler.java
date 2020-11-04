package com.icicibank.apimgmt.eventHandlers;

import java.io.IOException;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.itextpdf.io.image.ImageData;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.color.Color;
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
import com.itextpdf.layout.border.Border;
import com.itextpdf.layout.border.SolidBorder;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.layout.LayoutArea;
import com.itextpdf.layout.layout.LayoutContext;
import com.itextpdf.layout.layout.LayoutResult;
import com.itextpdf.layout.property.UnitValue;
import com.itextpdf.layout.renderer.DocumentRenderer;
import com.itextpdf.layout.renderer.TableRenderer;

public class TableEventHandler implements IEventHandler {

	private Table table;
	
	private static float tableHeight;
	
	private Document doc;
	
	
	
	private Logger logger = LoggerFactory.getLogger(TableEventHandler.class);
	public  TableEventHandler(Document doc) {
		super();
		this.doc = doc;
		initTable();
		
		 TableRenderer renderer = (TableRenderer) table.createRendererSubTree();
         renderer.setParent(new DocumentRenderer(doc));

         
         // Simulate the positioning of the renderer to find out how much space the header table will occupy.
         LayoutResult result = renderer.layout(new LayoutContext(new LayoutArea(0, PageSize.A4)));
         tableHeight = result.getOccupiedArea().getBBox().getHeight();
         
        
	}


	@Override
	public void handleEvent(Event event) {
		
		 PdfDocumentEvent docEvent = (PdfDocumentEvent) event;
		 
         PdfDocument pdfDoc = docEvent.getDocument();
         PdfPage page = docEvent.getPage();
         PdfCanvas canvas = new PdfCanvas(page.newContentStreamBefore(), page.getResources(), pdfDoc);
         PageSize pageSize = pdfDoc.getDefaultPageSize();
         float coordX = pageSize.getX()+doc.getLeftMargin()-10 ;
         float coordY = pageSize.getTop() - doc.getTopMargin()+10 ;
         logger.info("coord y of table "+coordY);
         float width = pageSize.getWidth() - doc.getRightMargin()-doc.getLeftMargin()+15;
         float height = getTableHeight();
         Rectangle rect = new Rectangle(coordX, coordY, width, height);
         
         new Canvas(canvas,pdfDoc, rect)
         .add(table)
         .close();
         
        
         
         
	}


	public Table getTable() {
		return table;
	}


	public void setTable(Table table) {
		this.table = table;
	}


	public static float getTableHeight() {
		return tableHeight;
	}


	public static void setTableHeight(float tablHeight) {
		tableHeight = tablHeight;
	}


	public Document getDoc() {
		return doc;
	}


	public void setDoc(Document doc) {
		this.doc = doc;
	}

    private void initTable()
    {
    	float[] percArray= {100};
    	UnitValue[] unitVal= UnitValue.createPercentArray(percArray);
        table = new Table(unitVal);
        table.useAllAvailableWidth();
        Border border = new SolidBorder(Color.WHITE, 1);
        table.setBorder(border);
        table.addCell(" ").setHeight(20).setBackgroundColor(Color.LIGHT_GRAY, .5f);
        
       
    }
}
