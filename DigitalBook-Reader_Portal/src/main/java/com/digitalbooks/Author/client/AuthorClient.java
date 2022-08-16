package com.digitalbooks.Author.client;

import java.util.List;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.digitalbooks.entities.BookPurchase;
import com.digitalbooks.entities.Books;

@FeignClient("Author-Portal")
public interface AuthorClient {

	@GetMapping("/api/v1/digitalbooks/author/books/")
	List<Books> getBookList();


}
