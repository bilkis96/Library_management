package com.libraryManagement.library_management.controller;
import com.libraryManagement.library_management.entity.Book;
import com.libraryManagement.library_management.entity.Borrow;
import com.libraryManagement.library_management.exceptions.BookNotAvailableException;
import com.libraryManagement.library_management.repository.BookRepository;
import com.libraryManagement.library_management.repository.BorrowRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/borrows")
public class BorrowController {

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private BookRepository bookRepository;

    @PostMapping
    public ResponseEntity<Borrow> createBorrow(@RequestBody Borrow borrow) {
        Optional<Book> bookOptional = bookRepository.findById(borrow.getBookId());
        if (bookOptional.isPresent()) {
            Book book = bookOptional.get();
            if (book.getAvailableCopies() > 0) {
                book.setAvailableCopies(book.getAvailableCopies() - 1);
                bookRepository.save(book);
                borrow.setBorrowedDate(LocalDate.now());
                borrow.setDueDate(LocalDate.now().plusDays(14)); // Assuming a 2-week borrowing period
                Borrow savedBorrow = borrowRepository.save(borrow);
                return ResponseEntity.ok(savedBorrow);
            } else {
                throw new BookNotAvailableException("Book is not available for borrowing.");
            }
        } else {
            throw new BookNotAvailableException("Book is not found");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<Borrow> getBorrowById(@PathVariable Long id) {
        Optional<Borrow> borrow = borrowRepository.findById(id);
        return borrow.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Borrow>> getAllBorrows() {
        List<Borrow> borrows = borrowRepository.findAll();
        return ResponseEntity.ok(borrows);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Borrow> updateBorrow(@PathVariable Long id, @RequestBody Borrow borrowDetails) {
        Optional<Borrow> optionalBorrow = borrowRepository.findById(id);
        if (optionalBorrow.isPresent()) {
            Borrow borrow = optionalBorrow.get();
            borrow.setMemberId(borrowDetails.getMemberId());
            borrow.setBookId(borrowDetails.getBookId());
            borrow.setBorrowedDate(borrowDetails.getBorrowedDate());
            borrow.setDueDate(borrowDetails.getDueDate());
            borrowRepository.update(borrow);
            return ResponseEntity.ok(borrow);
        } else {
            throw new BookNotAvailableException("Book not available with id: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrow(@PathVariable Long id) {
        Optional<Borrow> borrow = borrowRepository.findById(id);
        if (borrow.isPresent()) {
            borrowRepository.delete(id);
            return ResponseEntity.noContent().build();
        } else {
            throw new BookNotAvailableException("Book is not available with id: " + id);
        }
    }

    @PutMapping("/return/{id}")
    public ResponseEntity<Borrow> returnBook(@PathVariable Long id) {
        Optional<Borrow> optionalBorrow = borrowRepository.findById(id);
        if (optionalBorrow.isPresent()) {
            Borrow borrow = optionalBorrow.get();
            Optional<Book> bookOptional = bookRepository.findById(borrow.getBookId());
            if (bookOptional.isPresent()) {
                Book book = bookOptional.get();
                book.setAvailableCopies(book.getAvailableCopies() + 1);
                bookRepository.save(book);
                borrow.setDueDate(LocalDate.now()); // Assuming setting the return date to today
                borrowRepository.update(borrow);
                return ResponseEntity.ok(borrow);
            } else {
                throw new BookNotAvailableException("Book is not available for borrowing with id: " + id);

            }
        } else {
            throw new BookNotAvailableException("Book not found: ");
        }
    }
}

