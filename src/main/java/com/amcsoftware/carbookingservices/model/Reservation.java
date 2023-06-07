package com.amcsoftware.carbookingservices.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.annotations.Type;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@ToString
@EqualsAndHashCode
@Entity
public class Reservation {
//    @EmbeddedId
    @Id
    @GeneratedValue
    @Column(name = "reservation_id", updatable = false, nullable = false)
    private UUID reservationId;
    @ManyToOne( fetch = FetchType.EAGER)
//    @MapsId("userId")
    @JoinColumn(name = "member_id", foreignKey = @ForeignKey(name = "member_id_fk"),columnDefinition = "UUID")
    private Member member;
//    @ManyToOne
//    @MapsId("carId")
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


//    @ManyToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "car_id", referencedColumnName = "car_id", foreignKey = @ForeignKey(name = "car_id_fk"))
//    private Car car;
//    @OneToOne(cascade = CascadeType.ALL)
//    @JoinColumn(name = "user_id", referencedColumnName = "user_id", foreignKey = @ForeignKey(name = "user_id_fk"))
//    private Member member;

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
}
