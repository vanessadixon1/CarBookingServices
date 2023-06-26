package com.amcsoftware.carbookingservices.jpaDataAccess;

import com.amcsoftware.carbookingservices.dao.MemberDao;
import com.amcsoftware.carbookingservices.model.Member;
import com.amcsoftware.carbookingservices.repository.MemberRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("memberJpa")
public class MemberJpaDataAccessService implements MemberDao {

    private final MemberRepository memberRepository;

    public MemberJpaDataAccessService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Override
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    @Override
    public boolean existsByLastNameAndEmail(String lastName, String email) {
        return memberRepository.existsByLastNameAndEmail(lastName, email);
    }

    @Override
    public boolean existsByEmail(String email) {
        return memberRepository.existsByEmail(email);
    }

    @Override
    public void saveMember(Member member) {
        memberRepository.save(member);
    }

    @Override
    public Long memberCount() {
        return memberRepository.count();
    }

    @Override
    public Member findMemberByLastNameAndEmail(String lastName, String email) {
        return memberRepository.findMemberByLastNameAndEmail(lastName, email);
    }

    @Override
    public Member findMemberByEmail(String email) {
        return memberRepository.findMemberByEmail(email);
    }

    @Override
    public Optional<Member> findById(UUID id) {
        return memberRepository.findById(id);
    }

    @Override
    public void deleteMember(Member member) {
        memberRepository.delete(member);
    }

    @Override
    public Member findMemberById(UUID id) {
        return memberRepository.findByUserId(id);
    }
}
