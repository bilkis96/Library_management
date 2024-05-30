package com.libraryManagement.library_management.service;

import com.libraryManagement.library_management.entity.Book;
import com.libraryManagement.library_management.entity.Borrow;
import com.libraryManagement.library_management.exceptions.BookNotAvailableException;
import com.libraryManagement.library_management.repository.BookRepository;
import com.libraryManagement.library_management.repository.BorrowRepository;
import com.libraryManagement.library_management.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class BorrowService {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private MemberRepository memberRepository;
    @Autowired
    private BorrowRepository borrowRepository;

    public Borrow createBorrow(Borrow borrow) {
        Optional<Book> optionalBook = bookRepository.findById(borrow.getBookId());
        if (optionalBook.isPresent()) {
            Book book = optionalBook.get();
            if (book.getAvailableCopies() > 0) {
                book.setAvailableCopies(book.getAvailableCopies() - 1);
                bookRepository.save(book);
                borrow.setBorrowedDate(LocalDate.now());
                borrow.setDueDate(LocalDate.now().plusDays(14)); // Assuming a 2-week borrowing period
                return borrowRepository.save(borrow);
            } else {
                throw new BookNotAvailableException("Book is not available for borrowing: ");
            }
        } else {
            throw new BookNotAvailableException("Book is not found");
        }
    }

    public Borrow getBorrowById(Long id) {
        Optional<Borrow> optionalBorrow = borrowRepository.findById(id);
        if(optionalBorrow.isPresent())
        {
            return optionalBorrow.get();
        }
        else {
            throw new BookNotAvailableException("Book not found for borrow id : " + id);
        }
    }
    public List<Borrow> getAllBorrows(){
        return borrowRepository.findAll();
    }

    public Borrow updateBorrow(Long id, Borrow borrowDetails){
        Borrow borrow = getBorrowById(id);
        borrow.setMemberId(borrowDetails.getMemberId());
        borrow.setBookId(borrowDetails.getBookId());
        borrow.setBorrowedDate(borrowDetails.getBorrowedDate());
        borrow.setDueDate(borrowDetails.getDueDate());
        borrowRepository.save(borrow);
        return borrow;
    }

    public void deleteBorrow(Long id){
        Borrow borrow = getBorrowById(id);
        borrowRepository.deleteById(borrow.getId());
    }

    public Borrow returnBook(Long id){
        Borrow borrow = getBorrowById(id);
        Optional<Book> optionalBook = bookRepository.findById(borrow.getBookId());
        if(optionalBook.isPresent()){
            Book book = optionalBook.get();
            book.setAvailableCopies((book.getAvailableCopies()+1));
            bookRepository.save(book);
            borrow.setDueDate(LocalDate.now()); // Assuming setting the return date to today
            borrowRepository.save(borrow);
            return borrow;
        }
        else{
            throw new BookNotAvailableException("Book is not available for borrowing with id: " + id);
        }
    }
}
