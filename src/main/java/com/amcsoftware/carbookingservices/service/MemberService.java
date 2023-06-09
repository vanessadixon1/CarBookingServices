package com.amcsoftware.carbookingservices.service;

import com.amcsoftware.carbookingservices.exceptions.ResourceExist;
import com.amcsoftware.carbookingservices.exceptions.ResourceNotFound;
import com.amcsoftware.carbookingservices.model.Member;
import com.amcsoftware.carbookingservices.repository.MemberRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }
//
    public void saveMember(Member member) {
        if(!memberRepository.existsByEmail(member.getEmail())) {
            member.setFirstName(member.getFirstName().substring(0,1).toUpperCase() +
                    member.getFirstName().substring(1));

            memberRepository.save(member);
        } else {
            throw new ResourceExist("member [%s]".formatted(member.getEmail()) + " already exist");
        }
    }

    public List<Member> getMembers() {
        if(memberRepository.count() == 0) {
            throw new NullPointerException("no members available");
        }else {
            return memberRepository.findAll();
        }
    }

    public Member findLastNameAndEmail(String lastName, String email) {
        if(memberRepository.existsByLastNameAndEmail(lastName, email)) {
            return memberRepository.findMemberByLastNameAndEmail(lastName, email);
        } else {
            throw new ResourceNotFound("the lastName [%s]".formatted(lastName) + " and email: [%s] ".formatted(email) + " doesn't exist");
        }
    }

    public Member findMemberByEmail(String email) {
        if(memberRepository.existsByEmail(email)) {
            return memberRepository.findMemberByEmail(email);
        }else {
            throw new ResourceNotFound("the email [%s] ".formatted(email) + " doesn't exist");
        }
    }

    public Member findMemberById(UUID userId) {
        return memberRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFound("uer [%s] ".formatted(userId) + " was not found") );
    }

    public void deleteMember(String email) {
        Member locatedMember = memberRepository.findMemberByEmail(email);
        if(locatedMember.getReservations().size() == 0) {
            memberRepository.delete(locatedMember);
        } else {
            throw new IllegalArgumentException("in order to delete the user you must remove the reservation(s) booked by the user");
        }

    }

}
