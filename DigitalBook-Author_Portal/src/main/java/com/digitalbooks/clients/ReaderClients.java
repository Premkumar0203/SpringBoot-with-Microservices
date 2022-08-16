package com.digitalbooks.clients;

import java.util.List;
import java.util.Optional;

import com.digitalbooks.entities.BookPurchase;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


@FeignClient("Reader-Portal")
public interface ReaderClients
{

	@GetMapping("/purchaseList/")
	List<BookPurchase> getBookPurchaseList();


}
