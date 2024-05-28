package com.libraryManagement.library_management.service;

import com.libraryManagement.library_management.entity.Member;
import com.libraryManagement.library_management.exceptions.MemberNotAvailableException;
import com.libraryManagement.library_management.repository.MemberRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public Member creatMember(Member member){
        return memberRepository.save(member);
    }

    public Member getMemberById(Long id){
        Optional<Member> optionalMember = memberRepository.findById(id);
        if(optionalMember.isPresent()){
            return optionalMember.get();
        }
        else {
            throw new MemberNotAvailableException("Member with id not found: " + id);
        }
    }

    public List<Member> getAllMember(){
        return memberRepository.findAll();
    }

    public Member updateMember(Long id, Member memberDetails){
        Member member = getMemberById(id);
        member.setName(memberDetails.getName());
        member.setPhone(memberDetails.getPhone());
        member.setRegisteredDate(memberDetails.getRegisteredDate());
        return memberRepository.save(member);
    }

    public void deleteMemeber(Long id){
        Optional<Member> optionalMember = memberRepository.findById(id);
        if(optionalMember.isPresent()){
            memberRepository.deleteById(optionalMember.get().getId());
        }
        else {
            throw new MemberNotAvailableException("Memeber with id not found: " + id);
        }
    }

}
