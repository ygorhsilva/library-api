package com.example.library.service;

import com.example.library.exception.DuplicateIsbnException;
import com.example.library.model.Book;
import com.example.library.repository.BookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class BookService {

  @Autowired
  private BookRepository bookRepository;

  public List<Book> getAllApprovedBooks(String author, String title) {
    if (author != null && title != null) {
      return bookRepository.findApprovedByAuthorAndTitle(author, title);
    } else if (author != null) {
      return bookRepository.findApprovedByAuthor(author);
    } else if (title != null) {
      return bookRepository.findApprovedByTitle(title);
    } else {
      return bookRepository.findAllByApprovedTrue();
    }
  }

  public Optional<Book> getBookById(Long id) {
    return bookRepository.findById(id);
  }

  public Book createBook(Book book) {
    if (bookRepository.existsByIsbn(book.getIsbn())) {
      throw new DuplicateIsbnException("ISBN já cadastrado: " + book.getIsbn());
    }
    return bookRepository.save(book);
  }

  public Optional<Book> updateBook(Long id, Book bookDetails) {
    if (bookRepository.existsByIsbnAndIdNot(bookDetails.getIsbn(), id)) {
      throw new DuplicateIsbnException("ISBN já cadastrado: " + bookDetails.getIsbn());
    }
    return bookRepository.findById(id).map(book -> {
      book.setTitle(bookDetails.getTitle());
      book.setAuthor(bookDetails.getAuthor());
      book.setIsbn(bookDetails.getIsbn());
      book.setApproved(bookDetails.isApproved());
      return bookRepository.save(book);
    });
  }

  public boolean deleteBook(Long id) {
    return bookRepository.findById(id).map(book -> {
      bookRepository.delete(book);
      return true;
    }).orElse(false);
  }

  public Optional<Book> approveBook(Long id) {
    return bookRepository.findById(id).map(book -> {
      book.setApproved(true);
      return bookRepository.save(book);
    });
  }
}