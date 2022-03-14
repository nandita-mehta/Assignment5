package com.example.demo.service;

import java.util.List;
import com.example.demo.model.Book;

public interface BookService {
	
	public Book addBook(Book book);
	
	public void deleteBook(long id);
	
	public List<Book> getAllBooks();
	
	public Book getBookById(long id);
	
	public Book updateBook(Book book, long id);
}
