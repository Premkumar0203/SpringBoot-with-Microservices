package com.digitalbooks.pdfWriter;
import java.awt.Color;
import java.io.IOException;
import java.util.List;
 
import javax.servlet.http.HttpServletResponse;

import com.digitalbooks.entities.BookPurchase;
import com.digitalbooks.entities.Books;
import com.lowagie.text.*;
import com.lowagie.text.pdf.*;

 
 
public class UserPDFExporter
{
    private Books book;
    String name = null;
    String emailId = null; 
    String PaymentId= null;
     
    public UserPDFExporter(Books listUsers, String name, String emailId, String PaymentId) {
        this.book = listUsers;
        this.name = name;
        this.emailId = emailId;
        this.PaymentId = PaymentId;    
    }
 
    private void writeTableHeader(PdfPTable table) {
        PdfPCell cell = new PdfPCell();
        cell.setBackgroundColor(Color.BLUE);
        cell.setPadding(5);
         
        Font font = FontFactory.getFont(FontFactory.HELVETICA);
        font.setColor(Color.WHITE);
         
        cell.setPhrase(new Phrase("Book Id", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Title", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Category", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Author", font));
        table.addCell(cell); 
        cell.setPhrase(new Phrase("Publisher", font));
        table.addCell(cell);
        cell.setPhrase(new Phrase("Publisher Date", font));
        table.addCell(cell); 
        cell.setPhrase(new Phrase("Price", font));
        table.addCell(cell); 
    }
        
   public void export(HttpServletResponse response) throws DocumentException, IOException {
        Document document = new Document(PageSize.A4);
        PdfWriter.getInstance(document, response.getOutputStream());
         
        document.open();
        Font font = FontFactory.getFont(FontFactory.COURIER_BOLD);
        font.setSize(12);
        font.setColor(Color.BLUE);
         
        Paragraph p = new Paragraph("Payment Id : " + PaymentId, font);
        p.setAlignment(Paragraph.ALIGN_LEFT);
        document.add(p);
        String text ="Name : " + name;
        Paragraph p1 = new Paragraph(text, font);
        p1.setAlignment(Paragraph.ALIGN_LEFT);  
        text ="EmailID : " + emailId;
        document.add(p1);
        Paragraph p2 = new Paragraph(text, font);
        p2.setAlignment(Paragraph.ALIGN_RIGHT);  
        document.add(p2);
         
        PdfPTable table = new PdfPTable(7);
        table.setWidthPercentage(100f);
        table.setWidths(new float[] {1.5f, 3.5f, 3.0f, 3.0f, 1.5f, 3.0f, 3.0f});
        table.setSpacingBefore(10);
         
        writeTableHeader(table);

	    table.addCell(String.valueOf(book.getId()));
	    table.addCell(book.getTitle());
	    table.addCell(book.getCategory());
        table.addCell(book.getAuthor());
        table.addCell(book.getPublisher());
        table.addCell(String.valueOf(book.getPublishedDate()));
        table.addCell(String.valueOf(book.getPrice()));        
		
         
        document.add(table);
         
        document.close();
         
    }
}