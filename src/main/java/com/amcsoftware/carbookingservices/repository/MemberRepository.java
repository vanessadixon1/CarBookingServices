package com.amcsoftware.carbookingservices.repository;

import com.amcsoftware.carbookingservices.model.Member;
import org.springframework.data.repository.ListCrudRepository;

import java.util.UUID;

public interface MemberRepository extends ListCrudRepository<Member, UUID> {
    boolean existsByEmail(String email);
    Member findMemberByLastNameAndEmail(String lastName, String email);
    Member findMemberByEmail(String email);
    boolean existsByLastNameAndEmail(String lastName, String email);
    boolean existsByUserId(UUID id);
    Member findByUserId(UUID id);
}
