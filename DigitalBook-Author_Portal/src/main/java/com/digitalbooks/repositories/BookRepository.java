package com.digitalbooks.repositories;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.digitalbooks.entities.BookPurchase;
import com.digitalbooks.entities.Books;
import com.digitalbooks.entities.Users;


public interface BookRepository extends JpaRepository<Books, Integer> {

	@Query("SELECT b FROM Books b where category= ?1 or author  =?2 or price =?3 or publisher =?4")
	List<Books> FindBy(String category, String author, int price, String publisher);

	@Transactional 
    @Modifying
	@Query("UPDATE Books b set b.status = ?1 where b.id =?2 ")
	int updateStatus(int status, int bookId);

	@Query("SELECT b FROM Books b where b.title =?1")
	Optional<Books> FindByTitle(String title);

	@Query("SELECT b FROM Books b where b.author =?1")
	List<Books> FetchBookBelongToAuthor(String username);
	
	@Query("SELECT b FROM Books b where b.author =?1 and b.id=?2 ")
	Books getParticularBookListBelongToAuthor(String username, int bookId);


}
