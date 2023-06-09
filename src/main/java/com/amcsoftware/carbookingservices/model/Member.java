package com.amcsoftware.carbookingservices.model;


import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//Mapping this class to a Table in my database
@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
@Table(name = "member", uniqueConstraints = {
        @UniqueConstraint(name = "member_email_unique", columnNames = "email")
})
public class Member {
    @Id
    @GeneratedValue
    @Column(name = "user_id", updatable = false)
    private UUID userId;
    @Column(name = "first_name", nullable = false, columnDefinition = "varchar(150)")
    private String firstName;
    @Column(name = "last_name", nullable = false, columnDefinition = "varchar(150)")
    private String lastName;
    @Column(name = "middle_initial", columnDefinition = "varchar(7)")
    private String middleInitial;
    @Column(name = "phone_number", nullable = false, columnDefinition = "TEXT")
    private String phoneNumber;
    @Column(nullable = false, columnDefinition = "TEXT")
    private String email;
    @Column(name = "date_of_birth", nullable = true, columnDefinition = "DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate dateOfBirth;
    @Column(name = "street_number", nullable = false, columnDefinition = "numeric")
    private int streetNo;
    @Column( name = "street_address", nullable = false, columnDefinition = "TEXT")
    private String streetAddress;
    @Column(name = "city", nullable = false, columnDefinition = "TEXT")
    private String city;
    @Column(name = "state", nullable = false, columnDefinition = "varchar(150)")
    private String state;
    @Column(name = "zip_code",nullable = false, columnDefinition = "numeric")
    private int zipCode;
    @OneToMany(cascade = {CascadeType.MERGE}, orphanRemoval = true, mappedBy = "member", fetch = FetchType.EAGER)
    private List<Reservation> reservations = new ArrayList<>();

    public Member() {
    }

    public Member(UUID userId) {
        this.userId = userId;
    }

    public Member(String firstName, String lastName, String middleInitial, String phoneNumber, String email,
                  LocalDate date_of_birth, int streetNo, String streetAddress, String city, String state, int zipCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.middleInitial = middleInitial;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dateOfBirth = date_of_birth;
        this.streetNo = streetNo;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public Member(String firstName, String lastName, String phoneNumber, String email,
                  LocalDate date_of_birth, int streetNo, String streetAddress, String city, String state, int zipCode,
                  UUID rental_id) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dateOfBirth = date_of_birth;
        this.streetNo = streetNo;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    public Member(String firstName, String lastName, String phoneNumber,
                  String email, LocalDate date_of_birth, int streetNo, String streetAddress, String city, String state,
                  int zipCode) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.dateOfBirth = date_of_birth;
        this.streetNo = streetNo;
        this.streetAddress = streetAddress;
        this.city = city;
        this.state = state;
        this.zipCode = zipCode;
    }

    @JsonManagedReference
    public List<Reservation> getReservations() {
        return reservations;
    }
}
