package com.example.library.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.example.library.model.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

  List<Book> findAllByApprovedTrue();

  @Query("SELECT b FROM Book b WHERE b.approved = true AND (:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%')))")
  List<Book> findApprovedByAuthor(@Param("author") String author);

  @Query("SELECT b FROM Book b WHERE b.approved = true AND (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))")
  List<Book> findApprovedByTitle(@Param("title") String title);

  @Query("SELECT b FROM Book b WHERE b.approved = true " +
      "AND (:author IS NULL OR LOWER(b.author) LIKE LOWER(CONCAT('%', :author, '%'))) " +
      "AND (:title IS NULL OR LOWER(b.title) LIKE LOWER(CONCAT('%', :title, '%')))")
  List<Book> findApprovedByAuthorAndTitle(@Param("author") String author, @Param("title") String title);

  boolean existsByIsbn(String isbn);

  boolean existsByIsbnAndIdNot(String isbn, Long id);
}