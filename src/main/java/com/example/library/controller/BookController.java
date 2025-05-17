package com.example.library.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.library.model.Book;
import com.example.library.service.BookService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/books")
public class BookController {

  @Autowired
  private BookService bookService;

  @GetMapping
  public ResponseEntity<List<Book>> getAllBooks(
      @RequestParam(value = "author", required = false) String author,
      @RequestParam(value = "title", required = false) String title) {
    List<Book> books = bookService.getAllApprovedBooks(author, title);
    return ResponseEntity.ok(books);
  }

  @GetMapping("/{id}")
  public ResponseEntity<Book> getBookById(@PathVariable Long id) {
    return bookService.getBookById(id)
        .filter(Book::isApproved)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @PostMapping
  public ResponseEntity<Book> createBook(@Valid @RequestBody Book book) {
    Book createdBook = bookService.createBook(book);
    return ResponseEntity.ok(createdBook);
  }

  @PutMapping("/{id}")
  public ResponseEntity<Book> updateBook(@PathVariable Long id, @Valid @RequestBody Book bookDetails) {
    return bookService.updateBook(id, bookDetails)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }

  @DeleteMapping("/{id}")
  public ResponseEntity<Void> deleteBook(@PathVariable Long id) {
    return bookService.deleteBook(id)
        ? ResponseEntity.noContent().build()
        : ResponseEntity.notFound().build();
  }

  @PostMapping("/{id}/approve")
  public ResponseEntity<Book> approveBook(@PathVariable Long id) {
    return bookService.approveBook(id)
        .map(ResponseEntity::ok)
        .orElse(ResponseEntity.notFound().build());
  }
}