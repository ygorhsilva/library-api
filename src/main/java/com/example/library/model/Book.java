package com.example.library.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.UniqueConstraint;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

@Entity
@Table(name = "books", uniqueConstraints = @UniqueConstraint(columnNames = "isbn"))
@Data
public class Book {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O título é obrigatório")
    private String title;

    @NotBlank(message = "O autor é obrigatório")
    private String author;

    @NotBlank(message = "O ISBN é obrigatório")
    @Pattern(regexp = "^(?:ISBN(?:-1[03])?:? )?(?:[0-9]{10}|[0-9X]{10}|[0-9]{1,5}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9X]+|[0-9]{1,7}[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9]+[- ]?[0-9])$", message = "ISBN inválido. Use formato ISBN-10 ou ISBN-13 válido.")
    private String isbn;

    private boolean approved;
}