package com.amcsoftware.carbookingservices.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
public class Reservation {

    @Id
    @GeneratedValue
    @Column(name = "reservation_id", updatable = false, nullable = false)
    private UUID reservationId;
    @ManyToOne( fetch = FetchType.EAGER)
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "member_id_fk"),columnDefinition = "UUID")
    private Member member;
    @OneToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "car_id", foreignKey = @ForeignKey(name = "car_id_fk"), columnDefinition = "UUID", unique = true)
    private Car car;
    @Column(name = "pickup_date", nullable = false, columnDefinition = "DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate pickupDate;
    @Column(name = "return_date", nullable = false, columnDefinition = "DATE")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate returnDate;
    @Column(name = "create_at", columnDefinition = "TIMESTAMP WITHOUT TIME ZONE")
    private LocalDateTime createAt;

    public Reservation() {
    }

    public Reservation(Member member, Car car, LocalDate pickupDate, LocalDate returnDate) {
        this.member = member;
        this.car = car;
        this.pickupDate = pickupDate;
        this.returnDate = returnDate;
        this.createAt = LocalDateTime.now();
    }

    @JsonBackReference
    public Member getMember() {
        return member;
    }

    @JsonManagedReference
    public Car getCar() {
        return car;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Reservation that = (Reservation) o;
        return reservationId != null && Objects.equals(reservationId, that.reservationId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
