package com.digitalbooks.controllers;

import java.util.List;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

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
import com.digitalbooks.entities.Users;
import com.digitalbooks.security.JwtTokenUtil;
import com.digitalbooks.services.AuthorService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

@RestController
@Tag(description = "Author can create a new login and create,edit a book and block/unblock book", name = "Author Portal")
public class AuthorController {

	@Autowired
	private AuthorService service;
	
	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	private static final Logger logger = LogManager.getLogger(AuthorController.class);

	@Operation(summary = "Get Author List", description = "Provides all available Author list")

	@GetMapping(URLConstants.FetchAuthorList)
	public List<Users> getAuthorList() {
		return service.getAuthorList();
	}

	@Operation(summary = "Get Book Purchase List", description = "Provides all available Book Purchase list")
	@GetMapping("/api/v1/digitalbooks/BookPurchase/")
	public List<BookPurchase> getBookPurchaseList() {
		return service.getBookPurchaseList();
	}

	@Operation(summary = "Get Book List", description = "Provides all available Book list")
	@GetMapping(URLConstants.FetchBookList)
	public List<Books> getBookList() {
		return service.getBookList();
	}

	@Operation(summary = "Create Author", description = "Creating a new author")
	@PostMapping(URLConstants.CreateAuthor)
	public ResponseEntity<?> CreateAuthor(@RequestBody Users Userss) throws DigitalBookException {
		logger.info("Userss Creation - Started");
		logger.debug("Userss Creation - Started");
		Users u1 = service.CreateAuthor(Userss);
		System.out.println(u1.getUserName() + " " + u1.getPassword());
		if (u1 != null)
			logger.info("Userss Creation - Successfully Done");
		else
			logger.info("Fail to create Userss");
		logger.info("Userss Creation - End");
		logger.debug("Userss Creation - Started");
		return ResponseEntity.ok(u1);
	}

	@Operation(summary = "Create Book", description = "Creating a new Book")
	@PostMapping(URLConstants.createBook)
	public Books CreateBook(@RequestBody Books book, HttpServletRequest request) throws DigitalBookException
	{
		logger.info("CreateBook - Started");
		String authToken = request.getHeader("Authorization");
	    final String token = authToken.substring(7);
	    String username = jwtTokenUtil.getUsernameFromToken(token);
		return service.CreateBook(book, username);
	}

	@Operation(summary = "Edit Book", description = "Editing a Book")
	@PostMapping(URLConstants.EditBook)
	public Books EditBook( HttpServletRequest request, @PathVariable int bookId, @RequestBody Books book)
			throws DigitalBookException {
		logger.info("EditBook - Started for BookId:" + bookId);
		String authToken = request.getHeader("Authorization");
	    final String token = authToken.substring(7);
	    String username = jwtTokenUtil.getUsernameFromToken(token);
		return service.EditBook(book, username, bookId);

	}

	@Operation(summary = "Block/Unblock a Book", description = "Blocking / Unblocking a Book")
	@GetMapping(URLConstants.changeStatusBook)
	public Books ChangeBookStatus(HttpServletRequest request, @PathVariable int bookId, @PathVariable int status)
			throws DigitalBookException {
		logger.info("ChangeBookStatus - Started for BookId:" + bookId);
		String authToken = request.getHeader("Authorization");
	    final String token = authToken.substring(7);
	    String username = jwtTokenUtil.getUsernameFromToken(token);
		return service.ChangeBookStatus(username, bookId, status);

	}
	
	@Operation(summary = "Get Book List belongs to Author", description = "Provides all available Book list Belongs to Author")
	@GetMapping(URLConstants.FetchBookListBelongAuthor)
	public List<Books> getBookListBelongToAuthor(HttpServletRequest request) {
		String authToken = request.getHeader("Authorization");
	    final String token = authToken.substring(7);
	    String username = jwtTokenUtil.getUsernameFromToken(token);
		logger.info("Fetch Books Belong to Author " + username );
		return service.getBookListBelongToAuthor(username);
	}
	
	@Operation(summary = "Get Particular Book belongs to Author", 
			description = "Provides the Particular Book Belongs to Author")
	@GetMapping(URLConstants.FetchParticularBookListBelongAuthor)
	public Books getParticularBookListBelongToAuthor(
			HttpServletRequest request,@PathVariable int bookId)
	{
		String authToken = request.getHeader("Authorization");
	    final String token = authToken.substring(7);
	    String username = jwtTokenUtil.getUsernameFromToken(token);
		logger.info("Fetch Book Belong to Author " + username );
		return service.getParticularBookListBelongToAuthor(username,bookId);
	}

}
