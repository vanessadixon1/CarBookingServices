package com.amcsoftware.carbookingservices.service;

import com.amcsoftware.carbookingservices.exceptions.ResourceExist;
import com.amcsoftware.carbookingservices.exceptions.ResourceNotFound;
import com.amcsoftware.carbookingservices.jpaDataAccess.MemberJpaDataAccessService;
import com.amcsoftware.carbookingservices.model.Member;
import com.amcsoftware.carbookingservices.repository.MemberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class MemberService extends MemberJpaDataAccessService {

    public MemberService(MemberRepository memberRepository) {
        super(memberRepository);
    }

    public void saveMember(Member member) {
        if(!existsByEmail(member.getEmail())) {
            member.setFirstName(member.getFirstName().substring(0,1).toUpperCase() +
                    member.getFirstName().substring(1));

            saveMember(member);
        } else {
            throw new ResourceExist("member [%s]".formatted(member.getEmail()) + " already exist");
        }
    }

    public List<Member> getMembers() {
        if(memberCount() == 0) {
            throw new NullPointerException("no members available");
        }else {
            return getAllMembers();
        }
    }

    public Member findLastNameAndEmail(String lastName, String email) {
        if(existsByLastNameAndEmail(lastName,email)) {
            return findMemberByLastNameAndEmail(lastName, email);
        } else {
            throw new ResourceNotFound("the lastName [%s]".formatted(lastName) + " and email: [%s] ".formatted(email) + " doesn't exist");
        }
    }

    public Member findMemberByEmail(String email) {
        if(existsByEmail(email)) {
            return findMemberByEmail(email);
        }else {
            throw new ResourceNotFound("the email [%s] ".formatted(email) + " doesn't exist");
        }
    }

    public Member findMemberById(UUID userId) {
        return findById(userId)
                .orElseThrow(() -> new ResourceNotFound("uer [%s] ".formatted(userId) + " was not found") );
    }

    public void deleteMember(String email) {
        Member locatedMember = findMemberByEmail(email);
        if(locatedMember.getReservations().size() == 0) {
            deleteMember(locatedMember);
        } else {
            throw new IllegalArgumentException("in order to delete the user you must remove the reservation(s) booked by the user");
        }

    }

}
