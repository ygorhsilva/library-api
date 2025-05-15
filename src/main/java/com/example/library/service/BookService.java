package com.example.library.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.library.model.Book;
import com.example.library.repository.BookRepository;

@Service
public class BookService {

  @Autowired
  private BookRepository bookRepository;

  public Book createBook(Book book) {
    book.setApproved(false); // Novos livros precisam de aprovação
    return bookRepository.save(book);
  }

  public List<Book> getAllBooks() {
    return bookRepository.findByApprovedTrue(); // Retorna apenas livros aprovados
  }

  public Optional<Book> getBookById(Long id) {
    return bookRepository.findById(id);
  }

  public Book updateBook(Long id, Book updatedBook) {
    Optional<Book> existingBook = bookRepository.findById(id);
    if (existingBook.isPresent()) {
      Book book = existingBook.get();
      book.setTitle(updatedBook.getTitle());
      book.setAuthor(updatedBook.getAuthor());
      book.setIsbn(updatedBook.getIsbn());
      book.setApproved(updatedBook.isApproved());
      return bookRepository.save(book);
    }
    throw new RuntimeException("Livro não encontrado com ID: " + id);
  }

  public void deleteBook(Long id) {
    if (bookRepository.existsById(id)) {
      bookRepository.deleteById(id);
    } else {
      throw new RuntimeException("Livro não encontrado com ID: " + id);
    }
  }

  public Book approveBook(Long id) {
    Optional<Book> book = bookRepository.findById(id);
    if (book.isPresent()) {
      Book existingBook = book.get();
      existingBook.setApproved(true);
      return bookRepository.save(existingBook);
    }
    throw new RuntimeException("Livro não encontrado com ID: " + id);
  }
}