package com.libraryManagement.library_management.repository;

import com.libraryManagement.library_management.entity.Borrow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BorrowRepository extends JpaRepository<Borrow, Long> {
    List<Borrow> findByMember(Long memberId);

    List<Borrow> findByBookId(Long bookId);

    void delete(Long id);

    void update(Borrow borrow);
}



