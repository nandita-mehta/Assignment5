package com.example.demo.controller;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.config.DbContextHolder;
import com.example.demo.model.Book;
import com.example.demo.service.BookServiceImpl;

@RestController
public class BookController {
	
	@Autowired
	private BookServiceImpl bookService;
	
	public BookController() {}
	
	@PostMapping
	public Book addBook(@RequestHeader(name = "tenantName") String tenantName, @RequestBody Book book) {
		String tenantStr = "persistence-" + tenantName;
        DbContextHolder.setCurrentDb(tenantStr);
		return bookService.addBook(book);
	}
	
	@GetMapping
	public List<Book> getAllBooks(@RequestHeader(name = "tenantName") String tenantName) {
		String tenantStr = "persistence-" + tenantName;
        DbContextHolder.setCurrentDb(tenantStr);
        return bookService.getAllBooks();
	}
	
	@GetMapping("{id}")
	public ResponseEntity<Book> getBookById(@RequestHeader(name = "tenantName") String tenantName, @PathVariable("id") long id) {
		String tenantStr = "persistence-" + tenantName;
        DbContextHolder.setCurrentDb(tenantStr);
		return new ResponseEntity<Book>(bookService.getBookById(id), HttpStatus.OK);
	}

	@PutMapping("{id}")
	public ResponseEntity<Book> updateBook(@RequestHeader(name = "tenantName") String tenantName, @PathVariable("id") long id, @RequestBody Book book) {
		String tenantStr = "persistence-" + tenantName;
        DbContextHolder.setCurrentDb(tenantStr);
		return new ResponseEntity<Book>(bookService.updateBook(book, id), HttpStatus.OK);
	}

	@DeleteMapping("{id}")
	public ResponseEntity<String> deleteBook(@RequestHeader(name = "tenantName") String tenantName, @PathVariable("id") long id) {
		String tenantStr = "persistence-" + tenantName;
        DbContextHolder.setCurrentDb(tenantStr);
		bookService.deleteBook(id);
		return new ResponseEntity<String>("Book deleted successfully!.", HttpStatus.OK);
	}
}