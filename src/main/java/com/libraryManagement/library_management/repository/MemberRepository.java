package com.libraryManagement.library_management.repository;

import com.libraryManagement.library_management.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {
    @Query("SELECT m FROM Member m WHERE m.name LIKE %:name% OR m.phone LIKE %:phone%")
    List<Member> searchMembers(@Param("name") String name, @Param("phone") String phone);
}
