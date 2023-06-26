package com.amcsoftware.carbookingservices.service;

import com.amcsoftware.carbookingservices.dao.MemberDao;
import com.amcsoftware.carbookingservices.exceptions.DuplicateResourceException;
import com.amcsoftware.carbookingservices.exceptions.ResourceNotFoundException;
import com.amcsoftware.carbookingservices.model.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;
import java.util.regex.Pattern;

import static java.time.temporal.ChronoUnit.YEARS;

@Service
public class MemberService {

    private final MemberDao memberDao;

    public MemberService(@Qualifier("memberJpa") MemberDao memberDao) {
        this.memberDao = memberDao;
    }

    public void userRegistrationInfo(Member member) {

        long ageValidated = YEARS.between(member.getDateOfBirth(), LocalDate.now());

        boolean isPhoneNumberValid = Pattern.compile("^(\\d{3}[-]){2}\\d{4}$").matcher(member.getPhoneNumber()).matches();

        boolean isEmailValid = Pattern.compile("[a-zA-Z0-9]{1,}[@]{1}[a-z]{5,}[.]{1}+[a-z]{3}").matcher(member.getEmail()).matches();

        if(!isEmailValid) {
            throw new IllegalArgumentException("email %s".formatted(member.getEmail()) + " is invalid" );
        }

        if(!isPhoneNumberValid) {
            throw new IllegalArgumentException("phone number %s".formatted(member.getPhoneNumber()) + " is invalid" );
        }

        if(ageValidated < 21) {
            throw new IllegalArgumentException("registration unsuccessful - must be 21 or older");
        }

        if(!memberDao.existsByEmail(member.getEmail())) {
            member.setFirstName(member.getFirstName().substring(0,1).toUpperCase() +
                    member.getFirstName().substring(1));

            memberDao.saveMember(member);

        } else {
            throw new DuplicateResourceException("member [%s]".formatted(member.getEmail()) + " already exist");
        }
    }

    public List<Member> getMembers() {

        if(memberDao.memberCount() == 0) {
            throw new NullPointerException("no members available");
        }else {
            return memberDao.getAllMembers();
        }
    }

    public Member findLastNameAndEmail(String lastName, String email) {
        boolean isEmailValid = Pattern.compile("[a-zA-Z0-9]{1,}[@]{1}[a-z]{5,}[.]{1}+[a-z]{3}").matcher(email).matches();

        String lastname = lastName.substring(0,1).toUpperCase() + lastName.substring(1);

        if(!isEmailValid) {
            throw new IllegalArgumentException("email [%s]".formatted(email) + " is invalid" );
        }
        if(memberDao.existsByLastNameAndEmail(lastname,email)) {
            return memberDao.findMemberByLastNameAndEmail(lastname, email);
        } else {
            throw new ResourceNotFoundException("the lastName %s".formatted(lastname) + " and email: %s ".formatted(email) + "doesn't exist");
        }

    }

    public Member findMemberWithEmail(String email) {

        boolean isEmailValid = Pattern.compile("[a-zA-Z0-9]{1,}[@]{1}[a-z]{5,}[.]{1}+[a-z]{3}").matcher(email).matches();

        if(!isEmailValid) {
            throw new IllegalArgumentException("email %s".formatted(email) + " is invalid" );
        }

        if(memberDao.existsByEmail(email)) {
            return memberDao.findMemberByEmail(email);
        }else {
            throw new ResourceNotFoundException("the email %s".formatted(email) + " doesn't exist");
        }
    }

    public Member findMemberById(UUID userId) {

        return memberDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("uer [%s] ".formatted(userId) + " was not found") );
    }

    public void deleteMember(String email) {

        boolean isEmailValid = Pattern.compile("[a-zA-Z0-9]{1,}[@]{1}[a-z]{5,}[.]{1}+[a-z]{3}").matcher(email).matches();

        if(!isEmailValid) {
            throw new IllegalArgumentException("email %s".formatted(email) + " is invalid" );
        }

        Member locatedMember = memberDao.findMemberByEmail(email);

        if(locatedMember.getReservations().size() != 0 ) {
            throw new IllegalArgumentException("unsuccessful deletion - user has " + locatedMember.getReservations().size() + " reservation booked" );
        }

        memberDao.deleteMember(locatedMember);
    }

    public void updateMember(String email, Member member) {
        boolean isUpdatingEmailValid = Pattern.compile("[a-zA-Z0-9]{1,}[@]{1}[a-z]{5,}[.]{1}+[a-z]{3}").matcher(member.getEmail()).matches();

        boolean isEmailValid = Pattern.compile("[a-zA-Z0-9]{1,}[@]{1}[a-z]{5,}[.]{1}+[a-z]{3}").matcher(email).matches();

        if(!isEmailValid || !isUpdatingEmailValid) {
            throw new IllegalArgumentException("email %s" + " is invalid" );
        }

        if(!memberDao.existsByEmail(email)) {
            throw new ResourceNotFoundException("no user with email %s".formatted(email) + " found");
        }
        Member locatedMember = memberDao.findMemberByEmail(email);
        System.out.println(locatedMember.getFirstName());

        if(!locatedMember.getFirstName().equals(member.getEmail())) {
            locatedMember.setFirstName(member.getFirstName());
        }

        if(!locatedMember.getLastName().equals(member.getLastName())) {
            locatedMember.setLastName(member.getLastName());
        }

        if(locatedMember.getMiddleInitial() == null || !locatedMember.getMiddleInitial().equals(member.getMiddleInitial())) {
            locatedMember.setMiddleInitial(member.getMiddleInitial());
        }

        if(!locatedMember.getPhoneNumber().equals(member.getPhoneNumber())) {
            locatedMember.setPhoneNumber(member.getPhoneNumber());
        }

        if(!locatedMember.getEmail().equals(member.getEmail())) {
            locatedMember.setEmail(member.getEmail());
        }

        if(!locatedMember.getDateOfBirth().equals(member.getDateOfBirth())) {
            locatedMember.setDateOfBirth(member.getDateOfBirth());
        }

        if(locatedMember.getStreetNo() != member.getStreetNo()) {
            locatedMember.setStreetNo(member.getStreetNo());
        }

        if(!locatedMember.getStreetAddress().equals(member.getStreetAddress())) {
            locatedMember.setStreetAddress(member.getStreetAddress());
        }

        if(!locatedMember.getCity().equals(member.getCity())) {
            locatedMember.setCity(member.getCity());
        }

        if(locatedMember.getZipCode() != member.getZipCode()) {
            locatedMember.setZipCode(member.getZipCode());
        }

        if(!locatedMember.getState().equals(member.getState())) {
            locatedMember.setState(member.getState());
        }

        memberDao.saveMember(locatedMember);
    }

}
