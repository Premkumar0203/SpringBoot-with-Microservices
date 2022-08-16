package com.digitalbooks.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import com.digitalbooks.Exception.DigitalBookException;
import com.digitalbooks.Models.EmailDetails;
import com.digitalbooks.clients.ReaderClients;
import com.digitalbooks.controllers.AuthorController;
import com.digitalbooks.email.EmailService;
import com.digitalbooks.entities.BookPurchase;
import com.digitalbooks.entities.Books;
import com.digitalbooks.entities.Users;
import com.digitalbooks.repositories.BookRepository;
import com.digitalbooks.repositories.UsersRepository;
import com.lowagie.text.DocumentException;
import com.lowagie.text.Image;

@Service
public class AuthorService {
	
	private static final Logger logger = LogManager.getLogger(AuthorService.class);
	
	@Autowired
	private ReaderClients client;
	
	@Autowired
	BookRepository bookResp;
	@Autowired
    UsersRepository UserResp;
	
	public List<Users> getAuthorList() {
		return UserResp.findAll();
	}
	
	public List<Books> getBookList() {
		return bookResp.findAll();
	}
	
	public static int lineNumber() {
		return new Throwable().getStackTrace()[2].getLineNumber();
	}

	public Users CreateAuthor(Users user) throws DigitalBookException
	{
		Optional<Users> u = UserResp.fetchUserName(user.getUserName());
		if(u.isPresent())
		{
			logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
			+ " Context =This UserName already exits");
			throw new DigitalBookException("This UserName already exits");
		}
		else
		{
			Users us = new Users();
			us= UserResp.save(user);
			logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
			+ " Context =Author Created");
			return us;
		}
		
	}

	public String VerifyloginUser(Users users) throws DigitalBookException 
	{
		
		if(UserResp.findByName(users.getUserName(),users.getPassword()) == 0) {
			logger.error("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
			+ " Context =Could not find the Author :"+ users.getUserName());
			throw new DigitalBookException("Could not find the Author :"+ users.getUserName());
		} else {
				logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
				+ " Context =Login Successfully");
				return "Login Successfully";
		}
		
	}

	
	public Books CreateBook(Books book, String username) throws DigitalBookException
	{
		
			Optional<Books> u = bookResp.FindByTitle(book.getTitle());
			if(u.isPresent())
			{
				logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
				+ " Context =This title already exits");
				throw new DigitalBookException("This title already exits");
			}
			else
			{
				logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
				+ " Context =After validation,Book Creation Process Started ");
//				Image image;
//				try {
//					image = Image.getInstance(book.getLogo());
//				} catch (Exception e1) {
//					logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
//							+ " Context = Could not" + " find the logo of book in the path:"
//							+ book.getLogo());
//					throw new DigitalBookException("Could not"
//							+ " find the logo of book in the path:" + book.getLogo());
//				}
				
				book.setAuthor(username);
				return bookResp.save(book);
			}
		
	}

	
	public Books EditBook(Books book, String username, int bookId) throws DigitalBookException
	{

			Optional<Books> optional1 = bookResp.findById(bookId);
			if( !optional1.isPresent()) {
				logger.error("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
				+ " Context =Could not find the Book Id: "+ bookId);
				throw new DigitalBookException("Could not find the Book Id: "+ bookId);
			} else 
			{
				if(optional1.get().getAuthor().equals(username))
				{
					logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
					+ " Context =After validation ,Book Edit Process Started ");
					
//					if(book.getLogo() != null)
//					{
//						Image image;
//						try {
//							image = Image.getInstance(book.getLogo());
//						} catch (Exception e1) {
//							logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
//									+ " Context = Could not" + " find the logo of book in the path:"
//									+ book.getLogo());
//							throw new DigitalBookException("Could not"
//									+ " find the logo of book in the path:" + book.getLogo());
//						}
//						
//					}
					
					Books b = optional1.get();
					book.setAuthor(username);
					if(book.getLogo() != null)
						b.setLogo(book.getLogo());
					if(book.getTitle() != null)
						b.setTitle(book.getTitle());
					if(book.getContent() != null)
						b.setContent(book.getContent());
					if(book.getPublisher() != null)
						b.setPublisher(book.getPublisher());
					if(book.getCategory() != null)
						b.setCategory(book.getCategory());
					if(book.getPrice() > 0)
						b.setPrice(book.getPrice());
					if(book.getStatus() > 0)
						b.setStatus(book.getStatus());
					if(book.getPublishedDate() != null)
						b.setPublishedDate(book.getPublishedDate());
					return bookResp.save(b);
				}else
				{
					logger.error("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
					+ " Context = This bookId :"+ bookId +" is not belongs to this Author: "+username );
					throw new DigitalBookException(" This bookId :"+ bookId +" is not belongs to this Author: "+username );
				}
				
			}
	}
	
	@Autowired
    private KafkaTemplate<String, EmailDetails> kafkaTemplate;
	private static final String TOPIC = "Kafka-Email-topic";

	
	public Books ChangeBookStatus(String username, int bookId, int status) throws DigitalBookException {
	
			Optional<Books> optional1 = bookResp.findById(bookId);
			if( !optional1.isPresent()) {
				logger.error("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
				+ " Context =Could not find the Book Id: "+ bookId);
				throw new DigitalBookException("Could not find the Book Id: "+ bookId);
			} else 
			{
				if(optional1.get().getAuthor().equals(username))
				{
					Books b = optional1.get();
					logger.info("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
					+ " Context =After validation ,Book - change status Process Started ");
					if(status != b.getStatus())
					{
						b.setId(bookId);
						b.setStatus(status);
						if(bookResp.save(b) != null)
						{
							List<BookPurchase> list = client.getBookPurchaseList();
							if(!list.isEmpty())
							{
								list = list.stream().filter(BookPurchase -> 
								BookPurchase.getBookId() == bookId ).collect(Collectors.toList());
								
								for(BookPurchase det : list)
								{
									String BookStatus = "The "+optional1.get().getTitle() + " Book is"+
											(status == 1 ? " Available Now." : " not available here after.");
									
									EmailDetails details = new EmailDetails(det.getEmailId(),BookStatus
											,"Book Avaliability Status Update");
									//emailService.sendSimpleMail(details);
							        kafkaTemplate.send(TOPIC, details);

								}
								
								
								return b;
							}
							else
							{
								return b;

							}		
						}
						else
						{
							logger.error("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
							+ " Context =unable to update the status of Book Id: "+ bookId);
							throw new DigitalBookException("unable to update the status of Book Id: "+ bookId);
						}
					}
					else
					{
						logger.error("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
						+ " Context =Already Book is in same status only");
						throw new DigitalBookException("Already Book is in same status only");
					}
				}
				else
				{

					logger.error("ClassPath :" + this.getClass() + " Line No :" + lineNumber()
					+ " Context = This bookId :"+ bookId +" is not belongs to this Author: "+username );
					throw new DigitalBookException(" This bookId : "+ bookId +" is not belongs to this Author: "+username );
				
				}
				
				
			}
	}

	public List<BookPurchase> getBookPurchaseList()
	{
		return client.getBookPurchaseList();

	}

	public List<Books> getBookListBelongToAuthor(String username)
	{
		return bookResp.FetchBookBelongToAuthor(username);
	}

	public Books getParticularBookListBelongToAuthor(String username, int bookId) {
		return bookResp.getParticularBookListBelongToAuthor(username,bookId);
	}

	
}
