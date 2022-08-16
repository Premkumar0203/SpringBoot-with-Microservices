package com.digitalbooks.services;

import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import com.digitalbooks.Author.client.AuthorClient;
import com.digitalbooks.Exception.DigitalBookException;
import com.digitalbooks.Models.EmailDetails;
import com.digitalbooks.entities.BookPurchase;
import com.digitalbooks.entities.BookRefund;
import com.digitalbooks.entities.Books;
import com.digitalbooks.pdfWriter.UserPDFExporter;
import com.digitalbooks.repositories.BookPurchaseRepository;
import com.digitalbooks.repositories.BookRefundRepository;
import com.lowagie.text.BadElementException;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.MediaType;

@Service
public class BookService {

	private static final Logger logger = LogManager.getLogger(BookService.class);

	@Autowired
	BookPurchaseRepository bookPurchaseResp;

	@Autowired
	BookRefundRepository bookRefundResp;

	@Autowired
	private AuthorClient client;
	
	@Autowired
    private KafkaTemplate<String, EmailDetails> kafkaTemplate;
	private static final String TOPIC = "Kafka-Email-topic";

	public static int lineNumber() {
		return new Throwable().getStackTrace()[2].getLineNumber();
	}

	public ResponseEntity<?> SavePurchase(BookPurchase book) throws DigitalBookException {
		Optional<BookPurchase> list = bookPurchaseResp.ReadBookDetails(book.getEmailId(), book.getBookId());
		if (list.isPresent()) {
			logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
					+ " Context = Already This Reader subscribed this Book");
			throw new DigitalBookException("Already This Reader subscribed this Book");

		} else {
			
			Date date = new Date();
			SimpleDateFormat Format = new SimpleDateFormat("ddMMyyyyHHmmss");
			book.setPaymentId(Format.format(date));
			book.setDate(LocalDateTime.now());
			logger.info("Book Purchase Process Started");
			BookPurchase b= bookPurchaseResp.save(book);
			if(b != null)
			{
				List<String> bookTopic = client.getBookList().stream().
						filter(s -> s.getId() == book.getBookId() )
						.map(s-> s.getTitle()).collect(Collectors.toList());
				String BookStatus = "The Book " + bookTopic.get(0) + " is subscribed successfully";
				EmailDetails details = new EmailDetails(book.getEmailId(),BookStatus
						,"Book Subscription");
		        kafkaTemplate.send(TOPIC, details);
				return ResponseEntity.ok(b);
			}
			else
			{
				return ResponseEntity.ok("Unable to subscribe the book");
			}
			
		}

	}

	public List<BookPurchase> getBookPurchaseList() {
		return bookPurchaseResp.findAll();

	}

	public List<Books> SearchBook(String category, String author, int price, String publisher) {
		String condition = null;
		Books book = new Books();
		Predicate<Books> pred1 = null, pred2 = null, pred3 = null, pred4 = null;

		if (category != "") {
			pred1 = s -> s.getCategory().equals(category);
		}
		if (author != "") {
			pred2 = s -> s.getAuthor().equals(author);
		}
		if (price > 0) {
			pred3 = s -> s.getPrice() == price;
		}
		if (publisher != "") {
			pred4 = s -> s.getPublisher().equals(publisher);
		}
		List<Books> list = client.getBookList().stream().filter(s -> s.getStatus() == 1).collect(Collectors.toList());
		if (list != null) {
			if (pred1 != null)
				list = list.stream().filter(pred1).collect(Collectors.toList());
			if (pred2 != null)
				list = list.stream().filter(pred2).collect(Collectors.toList());
			if (pred3 != null)
				list = list.stream().filter(pred3).collect(Collectors.toList());
			if (pred4 != null)
				list = list.stream().filter(pred4).collect(Collectors.toList());

		}

		return list;
	}

	public List<BookPurchase> FetchPurchaseBookList(String emailId) {
		return bookPurchaseResp.findByEmailId(emailId);
	}

	public List<Books> ReadBook(String emailId, int bookId) throws DigitalBookException {
		Optional<BookPurchase> list = bookPurchaseResp.ReadBookDetails(emailId, bookId);
		if (list.isPresent()) {
			List<Books> book = client.getBookList().stream().filter(s -> s.getId() == bookId)
					.collect(Collectors.toList());
			if (book.isEmpty()) {
				logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
						+ " Context =Could not find the book with the bookId:" + bookId);
				throw new DigitalBookException("Could not find the book with the bookId:" + bookId);
			} else
				return book;
		} else {
			logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
					+ " Context =Could not find the book with the emailId :" + emailId);
			throw new DigitalBookException("Could not find the book with the emailId :" + emailId);
		}

	}

	public List<Books> FetchPurchasedBookByPaymentId(String emailId, String PaymentId) throws DigitalBookException {
		Optional<BookPurchase> bookId = bookPurchaseResp.FetchPurchasedBookByPaymentId(emailId, PaymentId);
		if ( !bookId.isPresent()) {
			logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
					+ " Context =Could not find the book with the PaymentId :" + PaymentId);
			throw new DigitalBookException("Could not find the book with the PaymentId :" + PaymentId);
		} else {
			List<Books> book = client.getBookList().stream().filter(s -> s.getId() == bookId.get().getBookId())
					.collect(Collectors.toList());
			if (book.isEmpty()) {
				logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
						+ " Context =Could not find the book with the bookId:" + bookId);
				throw new DigitalBookException("Could not find the book with the bookId:" + bookId);
			} else
				return book;
		}
	}
	

	public ResponseEntity<?> AskRefundBook(String emailId, int bookId) throws DigitalBookException {
		Optional<BookPurchase> list = bookPurchaseResp.ReadBookDetails(emailId, bookId);
		if (list.isPresent()) {
			LocalDateTime dt1 = LocalDateTime.now();
			LocalDateTime dt2 = list.get().getDate();
			Duration diff = Duration.between(dt1, dt2);
			System.out.println("Hours: " + diff.toHours());
			if (diff.toHours() <= 24) {
				if (bookPurchaseResp.updateRefundProcess(emailId, bookId) > 0) {
					BookRefund bookrefund = new BookRefund();
					bookrefund.setBookId(bookId);
					bookrefund.setDate(LocalDateTime.now());
					bookrefund.setEmailId(emailId);
					bookrefund.setName(list.get().getName());
					bookrefund.setPaymentId(list.get().getPaymentId());
					bookRefundResp.save(bookrefund);
					String BookStatus = "Refund Process is in progress, "
							+ "The amount will credit back to your account with 3 or 5 days.";
					EmailDetails details = new EmailDetails(emailId,BookStatus
							,"Book Refund Update");
			        kafkaTemplate.send(TOPIC, details);
			        List<BookRefund> book = new ArrayList<BookRefund>();
			        book.add(bookrefund);
					return ResponseEntity.ok(book);
				} else {
					logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
							+ " Context =Unable to process refund process for the Book Id " + bookId);
					throw new DigitalBookException("Unable to process refund process for the Book Id " + bookId);
				}
			} else {
				logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
						+ " Context =The Refund Process is possible only with 24 hrs of purchase");
				throw new DigitalBookException("The Refund Process is possible only with 24 hrs of purchase");
			}
		} else {
			logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
					+ " Context =Could not find the book with the emailId :" + emailId);
			throw new DigitalBookException("Could not find the book with the emailId :" + emailId);
		}
	}

	public ResponseEntity<?> ViewPaymentHistory(String emailId) {
		return ResponseEntity.ok(bookPurchaseResp.findByEmailId(emailId));
	}

	public ResponseEntity<?> ViewInvoiceUsingPaymentId(String emailId, String paymentId) throws DigitalBookException {
		Optional<BookPurchase> bookPurchase = bookPurchaseResp.FetchPurchasedBookByPaymentId(emailId, paymentId);
		if (!bookPurchase.isPresent()) {
			logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
					+ " Context =Could not find the invoice with the PaymentId :" + paymentId);
			return ResponseEntity.ok(new DigitalBookException("Could not find the invoice with the PaymentId :" + paymentId));
		} else {
			List<BookPurchase> list= new ArrayList<BookPurchase>();
			list.add(bookPurchase.get());
			return ResponseEntity.ok(list);
		}
	}

	public void DocumentInvoiceUsingPaymentId(HttpServletResponse response, String emailId, String paymentId)
			throws DigitalBookException {
		Optional<BookPurchase> bookPurchase = bookPurchaseResp.FetchPurchasedBookByPaymentId(emailId, paymentId);
		if (!bookPurchase.isPresent()) {
			logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
					+ " Context =Could not find the invoice with the PaymentId :" + paymentId);
			throw new DigitalBookException("Could not find the invoice with the PaymentId :" + paymentId);
		} else {
			response.setContentType("application/pdf");
			DateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
			String currentDateTime = dateFormatter.format(new Date());

			String headerKey = "Content-Disposition";
			String headerValue = "attachment; filename=" + bookPurchase.get().getName() + "_" + currentDateTime
					+ ".pdf";
			response.setHeader(headerKey, headerValue);

			List<Books> book = client.getBookList().stream().filter(s -> s.getId() == bookPurchase.get().getBookId())
					.collect(Collectors.toList());
			if (book.isEmpty()) {
				logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
						+ " Context =Error in Download Invoice : Could not" + " find the book with the bookId:"
						+ bookPurchase.get().getBookId());
				throw new DigitalBookException("Error in Download Invoice : Could not"
						+ " find the book with the bookId:" + bookPurchase.get().getBookId());
			} else {
				Image image;
				try {
					image = Image.getInstance(book.get(0).getLogo());
				} catch (Exception e1) {
					logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
							+ " Context =Error in Download Invoice : Could not" + " find the logo of book in the path:"
							+ book.get(0).getLogo());
					throw new DigitalBookException("Error in Download Invoice : Could not"
							+ " find the logo of book in the path:" + book.get(0).getLogo());
				}

				UserPDFExporter exporter = new UserPDFExporter(book.get(0), bookPurchase.get().getName(),
						bookPurchase.get().getEmailId(), bookPurchase.get().getPaymentId());
				try {
					exporter.export(response);

				} catch (DocumentException | IOException e) {
					logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
							+ " Context =Error while downloading the invoice :" + e.getMessage());
					throw new DigitalBookException("Error while downloading the invoice :" + e.getMessage());

				}

			}

		}

	}

}
