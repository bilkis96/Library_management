package com.libraryManagement.library_management.controller;
import com.libraryManagement.library_management.entity.Book;
import com.libraryManagement.library_management.entity.Borrow;
import com.libraryManagement.library_management.exceptions.BookNotAvailableException;
import com.libraryManagement.library_management.repository.BookRepository;
import com.libraryManagement.library_management.repository.BorrowRepository;
import com.libraryManagement.library_management.service.BorrowService;
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
    private BorrowService borrowService;

    @PostMapping
    public ResponseEntity<Borrow> createBorrow(@RequestBody Borrow borrow) {
        Borrow savedBorrow = borrowService.createBorrow(borrow);
        return ResponseEntity.ok(savedBorrow);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Borrow> getBorrowById(@PathVariable Long id) {
        Borrow borrow = borrowService.getBorrowById(id);
        return ResponseEntity.ok(borrow);
    }

    @GetMapping
    public ResponseEntity<List<Borrow>> getAllBorrows() {
        List<Borrow> borrows = borrowService.getAllBorrows();
        return ResponseEntity.ok(borrows);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Borrow> updateBorrow(@PathVariable Long id, @RequestBody Borrow borrowDetails) {
        Borrow updatedBorrow = borrowService.updateBorrow(id, borrowDetails);
        return ResponseEntity.ok(updatedBorrow);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteBorrow(@PathVariable Long id) {
        borrowService.deleteBorrow(id);
        return ResponseEntity.noContent().build();
    }


    @PutMapping("/return/{id}")
    public ResponseEntity<Borrow> returnBook(@PathVariable Long id) {
        Borrow returnedBook = borrowService.returnBook(id);
        return ResponseEntity.ok(returnedBook);
    }
}

