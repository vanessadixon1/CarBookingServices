package com.amcsoftware.carbookingservices.repository;

import com.amcsoftware.carbookingservices.model.Car;
import com.amcsoftware.carbookingservices.model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface ReservationRepository extends JpaRepository<Reservation, UUID> {
    boolean existsByReservationId(UUID reservationId);
    boolean existsReservationByCar(Car car);
    Reservation findReservationByCar(Car car);
}
