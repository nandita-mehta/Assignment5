package com.example.demo.service;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.exception.ResourceNotFoundException;
import com.example.demo.model.Book;
import com.example.demo.repository.BookRepository;

@Service
public class BookServiceImpl implements BookService {
	
	@Autowired
	private BookRepository bookRepository;
	
	@Override
	public Book addBook(Book book) {
		return bookRepository.save(book);
	}
	
	@Override
	public void deleteBook(long id) {
		bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "Id", id));
		bookRepository.deleteById(id);
	}
	
	@Override
	public List<Book> getAllBooks() {
		return bookRepository.findAll();
	}
	
	@Override
	public Book getBookById(long id) {
		return bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "Id", id));
	}
	
	@Override
	public Book updateBook(Book book, long id) {
		Book existingBook = bookRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Book", "Id", id)); 
		existingBook.setAuthor(book.getAuthor());
		existingBook.setTitle(book.getTitle());
		existingBook.setGenre(book.getGenre());
		bookRepository.save(existingBook);
		return existingBook;
	}
}