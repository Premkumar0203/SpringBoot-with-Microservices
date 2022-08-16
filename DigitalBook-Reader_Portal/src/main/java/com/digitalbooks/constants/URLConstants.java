package com.digitalbooks.constants;

public interface URLConstants {
	
	String BOOK_BASE_URL= "/api/v1/digitalbooks/";
	
    String SearchBook =BOOK_BASE_URL+ "books/search";

    String PurchaseBook = BOOK_BASE_URL + "books/buy";
    
    String FetchPurchasedBook = BOOK_BASE_URL + "readers/{emailId}/books";
    
    String ReadBook = FetchPurchasedBook + "/{bookId}";
    
    String FetchBookUsingPaymentId = BOOK_BASE_URL + "readers/{emailId}/book";
    
    String RefundBook = ReadBook + "/refund";
    
    String ViewHistoryPayment  = BOOK_BASE_URL + "readers";
    
    String viewInvoiceUsingPaymentId = BOOK_BASE_URL + "readers/{emailId}/invoice";
    
    String DocumentInvoiceUsingPaymentId = BOOK_BASE_URL + "readers/{emailId}/DocumentInvoice";


    
    

    
    

}
