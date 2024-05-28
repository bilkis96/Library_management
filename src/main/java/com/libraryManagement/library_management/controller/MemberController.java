package com.libraryManagement.library_management.controller;
import com.libraryManagement.library_management.entity.Member;
import com.libraryManagement.library_management.exceptions.MemberNotAvailableException;
import com.libraryManagement.library_management.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/members")
public class MemberController {

    @Autowired
    private MemberRepository memberRepository;

    @PostMapping
    public ResponseEntity<Member> createMember(@RequestBody Member member) {
        Member savedMember = memberRepository.save(member);
        return ResponseEntity.ok(savedMember);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Member> getMemberById(@PathVariable Long id) {
        Optional<Member> member = memberRepository.findById(id);
        if(member.isPresent()){
            return ResponseEntity.ok(member.get());
        }
        else {
            throw new MemberNotAvailableException("Member not found with id: " + id);
        }
    }

    @GetMapping
    public ResponseEntity<List<Member>> getAllMembers() {
        List<Member> members = memberRepository.findAll();
        return ResponseEntity.ok(members);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Member> updateMember(@PathVariable Long id, @RequestBody Member memberDetails) {
        Optional<Member> optionalMember = memberRepository.findById(id);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            member.setName(memberDetails.getName());
            member.setPhone(memberDetails.getPhone());
            member.setRegisteredDate(memberDetails.getRegisteredDate());
            Member updatedMember = memberRepository.save(member);
            return ResponseEntity.ok(updatedMember);
        } else {
            throw new MemberNotAvailableException("Member not found with id: " + id);
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        Optional<Member> member = memberRepository.findById(id);
        if (member.isPresent()) {
            memberRepository.delete(member.get());
            return ResponseEntity.noContent().build();
        } else {
           throw new MemberNotAvailableException("Member not found with id: " + id);
        }
    }

    @GetMapping("/search")
    public ResponseEntity<List<Member>> searchMembers(@RequestParam(required = false) String name,
                                                      @RequestParam(required = false) String phone) {
        List<Member> members = memberRepository.searchMembers(name, phone);
        return ResponseEntity.ok(members);
    }
}

