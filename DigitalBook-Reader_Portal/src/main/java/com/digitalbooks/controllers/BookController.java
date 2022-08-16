package com.digitalbooks.controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.digitalbooks.Exception.DigitalBookException;
import com.digitalbooks.constants.URLConstants;
import com.digitalbooks.entities.BookPurchase;
import com.digitalbooks.entities.Books;
import com.digitalbooks.services.BookService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@Tag(description = "Reader can search a book and purchase , view and download invoice, read book ", name = "Reader Portal")
public class BookController {

	private static final Logger logger = LogManager.getLogger(BookController.class);

	@Autowired
	private BookService service;

	@Operation(summary = "Get Purchase List", description = "Provides all available Book Purchase list")
	@GetMapping("/purchaseList/")
	List<BookPurchase> getBookPurchaseList() {
		return service.getBookPurchaseList();
	}

	@Operation(summary = "Search Book", description = "Search Book based on category,price, author or publisher")
	@GetMapping(URLConstants.SearchBook)
	public List<Books> SearchBook(@RequestParam(value = "category", required = false) String category,
			@RequestParam(value = "author", required = false) String author,
			@RequestParam(value = "price", required = false) int price,
			@RequestParam(value = "publisher", required = false) String publisher) {
		return service.SearchBook(category, author, price, publisher);

	}

	@Operation(summary = "Purchase Book", description = "Reader can purchase the Book")
	@PostMapping(URLConstants.PurchaseBook)
	public ResponseEntity<?> savePurchase(@RequestBody BookPurchase book) throws DigitalBookException {
		return service.SavePurchase(book);
	}

	@Operation(summary = "Fetch Purchase Book By Reader", description = "Reader can fetch the Book purchase by using mailId")
	@GetMapping(URLConstants.FetchPurchasedBook)
	public List<BookPurchase> FetchPurchaseBookList(@PathVariable String emailId) {
		return service.FetchPurchaseBookList(emailId);
	}

	@Operation(summary = "Read Book", description = "Reader can read the book by using mailId and bookID")
	@GetMapping(URLConstants.ReadBook)
	public List<Books> ReadBook(@PathVariable String emailId, @PathVariable int bookId) throws DigitalBookException {
		return service.ReadBook(emailId, bookId);
	}

	@Operation(summary = "Fetch Book using PaymentId", description = "Reader can read the book by using PaymentId")
	@GetMapping(URLConstants.FetchBookUsingPaymentId)
	public List<Books> FetchPurchasedBookByPaymentId(@PathVariable String emailId, @RequestParam String PaymentId)
			throws DigitalBookException {
		return service.FetchPurchasedBookByPaymentId(emailId, PaymentId);
	}

	@Operation(summary = "Book refund", description = "Reader can refund the purchase book within 24 hours ")
	@GetMapping(URLConstants.RefundBook)
	public ResponseEntity<?> AskRefundBook(@PathVariable String emailId, @PathVariable int bookId)
			throws DigitalBookException {
		return service.AskRefundBook(emailId, bookId);
	}

	@Operation(summary = "View Payment History", description = "Reader can view the Payment History ")

	@GetMapping(URLConstants.ViewHistoryPayment)
	public ResponseEntity<?> ViewPaymentHistory(@RequestParam String emailId) throws DigitalBookException {
		return service.ViewPaymentHistory(emailId);
	}

	@Operation(summary = "View Invoice", description = "Reader can view the Invoice using PaymentID ")
	@GetMapping(URLConstants.viewInvoiceUsingPaymentId)
	public ResponseEntity<?> ViewInvoiceUsingPaymentId(@PathVariable String emailId, @RequestParam String PaymentId)
			throws DigitalBookException {
		return service.ViewInvoiceUsingPaymentId(emailId, PaymentId);
	}

	@Operation(summary = "Download Invoice", description = "Reader can view the Download using PaymentID ")
	@GetMapping(URLConstants.DocumentInvoiceUsingPaymentId)
	public void DocumentInvoiceUsingPaymentId(HttpServletResponse response, @PathVariable String emailId,
			@RequestParam String PaymentId) throws DigitalBookException {
		service.DocumentInvoiceUsingPaymentId(response, emailId, PaymentId);
	}

}
