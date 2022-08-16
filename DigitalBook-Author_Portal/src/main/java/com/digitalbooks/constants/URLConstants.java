package com.digitalbooks.constants;

public interface URLConstants {
	
	String AUTHOR_BASE_URL= "/api/v1/digitalbooks/author";
	
	String FetchAuthorList = AUTHOR_BASE_URL + "/";
	
	String FetchBookList = AUTHOR_BASE_URL + "/books/";
	
	String CreateAuthor = AUTHOR_BASE_URL + "/signup";
	
	String LogoutAuthor = AUTHOR_BASE_URL + "/Logout";
	
	String LoginAuthor = AUTHOR_BASE_URL + "/authenicate";
	
	String createBook = AUTHOR_BASE_URL + "/books";
	
	String FetchBookListBelongAuthor = AUTHOR_BASE_URL + "/GetBooks";
	
	String FetchParticularBookListBelongAuthor = AUTHOR_BASE_URL + "/BookId/{bookId}";
	
	String EditBook = AUTHOR_BASE_URL + "/books/{bookId}";

	String changeStatusBook = AUTHOR_BASE_URL + "/books/{bookId}/status/{status}";




	
	

}
