package com.amcsoftware.carbookingservices.service;

import com.amcsoftware.carbookingservices.dao.MemberDao;
import com.amcsoftware.carbookingservices.exceptions.BadRequestException;
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

        validateValuesWerePassed(member);

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

        if(memberDao.existsByEmail(member.getEmail())) {
            throw new DuplicateResourceException("a member with the email %s".formatted(member.getEmail()) + " already exist");
        }

        member.setFirstName(member.getFirstName().substring(0,1).toUpperCase() +
                member.getFirstName().substring(1));

        memberDao.saveMember(member);
    }

    public List<Member> getMembers() {

        if(memberDao.memberCount() == 0) {
            throw new ResourceNotFoundException("no data available");
        }

        return memberDao.getAllMembers();

    }

    public Member findLastNameAndEmail(String lastName, String email) {

        if(lastName.length() == 0 || email.length() == 0) {
            throw new BadRequestException("the last name or email is not valid");
        }

        boolean isEmailValid = Pattern.compile("[a-zA-Z0-9]{1,}[@]{1}[a-z]{5,}[.]{1}+[a-z]{3}").matcher(email).matches();

        String lastname = lastName.substring(0,1).toUpperCase() + lastName.substring(1);

        if(!isEmailValid) {
            throw new IllegalArgumentException("email %s".formatted(email) + " is invalid" );
        }

        if(!memberDao.existsByLastNameAndEmail(lastname,email)) {
            throw new ResourceNotFoundException("the lastName %s".formatted(lastname) + " and email: %s ".formatted(email) + "doesn't exist");
        }

        return memberDao.findMemberByLastNameAndEmail(lastname, email);
    }

    public Member findMemberWithEmail(String email) {

        if(email.length() == 0) {
            throw new BadRequestException("the last name or email is not valid");
        }

        boolean isEmailValid = Pattern.compile("[a-zA-Z0-9]{1,}[@]{1}[a-z]{5,}[.]{1}+[a-z]{3}").matcher(email).matches();

        if(!isEmailValid) {
            throw new IllegalArgumentException("email %s".formatted(email) + " is invalid");
        }

        if(!memberDao.existsByEmail(email)) {
            throw new ResourceNotFoundException("the email %s".formatted(email) + " doesn't exist");
        }

        return memberDao.findMemberByEmail(email);
    }

    public Member findMemberById(UUID userId) {

        return memberDao.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("uer %s".formatted(userId) + " was not found") );
    }

    public void deleteMember(String email) {

        if(!memberDao.existsByEmail(email)) {
            throw new ResourceNotFoundException("the email %s".formatted(email) + " doesn't exist");
        }

        boolean isEmailValid = Pattern.compile("[a-zA-Z0-9]{1,}[@]{1}[a-z]{5,}[.]{1}+[a-z]{3}").matcher(email).matches();

        if(!isEmailValid) {
            throw new BadRequestException("email %s".formatted(email) + " is invalid");
        }

        Member locatedMember = memberDao.findMemberByEmail(email);

        if(locatedMember.getReservations().size() != 0) {
            throw new BadRequestException("unsuccessful deletion - remove the current reservation(s) and then remove user." );
        }

        memberDao.deleteMember(locatedMember);
    }

    public void updateMember(String email, Member member) {

        if(!memberDao.existsByEmail(email)) {
            throw new BadRequestException("unable to update - no user with the email %s".formatted(email) + " was found");
        }

        boolean isUpdatingEmailValid = Pattern.compile("[a-zA-Z0-9]{1,}[@]{1}[a-z]{5,}[.]{1}+[a-z]{3}").matcher(member.getEmail()).matches();

        boolean isEmailValid = Pattern.compile("[a-zA-Z0-9]{1,}[@]{1}[a-z]{5,}[.]{1}+[a-z]{3}").matcher(email).matches();

        boolean changed = false;

        if(!isEmailValid || !isUpdatingEmailValid) {
            throw new BadRequestException("email is invalid" );
        }

        if(!memberDao.existsByEmail(email)) {
            throw new ResourceNotFoundException("no user with email %s".formatted(email) + " found");
        }

        Member locatedMember = memberDao.findMemberByEmail(email);

        if(!member.getFirstName().equals("") && !locatedMember.getFirstName().equals(member.getEmail())) {
            locatedMember.setFirstName(member.getFirstName());
            changed = true;
        }

        if(!member.getLastName().equals("") && !locatedMember.getLastName().equals(member.getLastName())) {
            locatedMember.setLastName(member.getLastName());
            changed = true;
        }

        if(member.getMiddleInitial() == null || !locatedMember.getMiddleInitial().equals(member.getMiddleInitial())) {
            locatedMember.setMiddleInitial(member.getMiddleInitial());
            changed = true;
        }

        if(!member.getPhoneNumber().equals("") && !locatedMember.getPhoneNumber().equals(member.getPhoneNumber())) {
            locatedMember.setPhoneNumber(member.getPhoneNumber());
            changed = true;
        }

        if(!member.getEmail().equals("") && !locatedMember.getEmail().equals(member.getEmail())) {
            locatedMember.setEmail(member.getEmail());
            changed = true;
        }

        if(member.getDateOfBirth() != null && !locatedMember.getDateOfBirth().equals(member.getDateOfBirth())) {
            locatedMember.setDateOfBirth(member.getDateOfBirth());
            changed = true;
        }

        if(member.getStreetNo() != 0 && locatedMember.getStreetNo() != member.getStreetNo()) {
            locatedMember.setStreetNo(member.getStreetNo());
            changed = true;
        }

        if(!member.getStreetAddress().equals("") && !locatedMember.getStreetAddress().equals(member.getStreetAddress())) {
            locatedMember.setStreetAddress(member.getStreetAddress());
            changed = true;
        }

        if(!member.getCity().equals("") && !locatedMember.getCity().equals(member.getCity())) {
            locatedMember.setCity(member.getCity());
            changed = true;
        }

        if(member.getZipCode() != 0 && locatedMember.getZipCode() != member.getZipCode()) {
            locatedMember.setZipCode(member.getZipCode());
            changed = true;
        }

        if(!member.getState().equals("") && !locatedMember.getState().equals(member.getState())) {
            locatedMember.setState(member.getState());
            changed = true;
        }

        if(!changed) {
            throw new BadRequestException("no changes were made");
        }

        memberDao.saveMember(locatedMember);
    }

    private void validateValuesWerePassed(Member member) {
        if(member.getFirstName().equals("") || member.getLastName().equals("") || member.getPhoneNumber().equals("") ||
                member.getEmail().equals("") || member.getDateOfBirth().equals("") || member.getStreetNo() != 0 ||
                member.getStreetAddress().equals("") || member.getCity().equals("") || member.getState().equals("")) {

            throw new BadRequestException("invalid values sent through request");
        }
    }

}
