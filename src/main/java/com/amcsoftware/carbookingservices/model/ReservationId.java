package com.amcsoftware.carbookingservices.model;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.UUID;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Embeddable
public class ReservationId implements Serializable {
    @Column(name = "user_id")
    private UUID userId;
    @Column(name = "car_id")
    private UUID carId;

    public ReservationId(UUID userId, UUID carId) {
        this.userId = userId;
        this.carId = carId;
    }
}
