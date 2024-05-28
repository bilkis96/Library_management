package com.libraryManagement.library_management.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "books")

public class Book {
   // public Byte getId;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title_book")
    private String title;
    @Column(name = "author_book")
    private String author;
    @Column(name = "isbn_book")
    private String isbn;
    @Column(name = "publisheddate_book")
    private LocalDate publishedDate;
    @Column(name = "availablecopies_book")
    private int availableCopies;

    public Book(long id, String testBook, String testAuthor, String number, String date, int availableCopies) {
        
    }

    public Book(Long o, String title, String testAuthor, String number, String date, int availableCopies) {
    }
}
