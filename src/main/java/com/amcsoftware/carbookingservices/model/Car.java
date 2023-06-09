package com.amcsoftware.carbookingservices.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.Hibernate;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.UUID;

@Getter
@Setter
@ToString
@Entity
public class Car {
    @Id
    @GeneratedValue
    @Column(name = "car_id", updatable = false)
    private UUID carId;
    @Column(name = "make",  columnDefinition = "TEXT")
    private String make;
    @Column(name = "model", columnDefinition = "TEXT")
    private String model;
    @Column(name = "year",  columnDefinition = "numeric")
    private int year;
    @Column(name = "price", columnDefinition = "numeric", precision = 5, scale = 2)
    private BigDecimal price;
    @OneToOne(mappedBy = "car",cascade = {CascadeType.MERGE}, orphanRemoval = true, fetch = FetchType.EAGER)
    private Reservation reservations;

    public Car() {
    }

    public Car(UUID carId) {
        this.carId = carId;
    }

    public Car(String make, String model, int year, BigDecimal price) {
        this.make = make;
        this.model = model;
        this.year = year;
        this.price = price;
    }

    @JsonBackReference
    public Reservation getReservations() {
        return reservations;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || Hibernate.getClass(this) != Hibernate.getClass(o)) return false;
        Car car = (Car) o;
        return carId != null && Objects.equals(carId, car.carId);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
