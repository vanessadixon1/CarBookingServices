package com.amcsoftware.carbookingservices.dao;

import com.amcsoftware.carbookingservices.model.Member;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface MemberDao {
    List<Member> getAllMembers();
    boolean existsByLastNameAndEmail(String lastName, String email);
    boolean existsByEmail(String email);
    void saveMember(Member member);
    Long memberCount();
    Member findMemberByLastNameAndEmail(String lastName, String email);
    Member findMemberByEmail(String email);
    Optional<Member> findById(UUID id);
    void deleteMember(Member member);
    Member findMemberById(UUID id);
}
