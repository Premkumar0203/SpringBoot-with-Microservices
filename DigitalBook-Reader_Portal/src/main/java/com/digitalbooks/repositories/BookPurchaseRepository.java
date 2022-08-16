package com.digitalbooks.repositories;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.digitalbooks.entities.BookPurchase;
import com.digitalbooks.entities.Books;

public interface BookPurchaseRepository extends JpaRepository<BookPurchase, Integer> {

	List<BookPurchase> findByEmailId(String emailId);

	@Query("Select bp from BookPurchase bp where bp.emailId = ?1 and bp.bookId = ?2 ")
	Optional<BookPurchase> ReadBookDetails(String emailId, int bookId);

	@Query("Select bp from BookPurchase bp where bp.emailId = ?1 and bp.PaymentId = ?2 ")
	Optional<BookPurchase> FetchPurchasedBookByPaymentId(String emailId, String PaymentId);

	@Transactional 
    @Modifying
	@Query("delete from BookPurchase bp where bp.emailId = ?1 and bp.bookId = ?2 ")
	int updateRefundProcess(String emailId, int bookId);


}
