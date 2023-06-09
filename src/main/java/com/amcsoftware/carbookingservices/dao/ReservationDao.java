package com.amcsoftware.carbookingservices.dao;

import com.amcsoftware.carbookingservices.model.Reservation;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface ReservationDao extends MemberDao {
    boolean existsByReservationId(UUID id);
    Long reservationCount();
    List<Reservation> getAllReservations();
    void saveReservation(Reservation reservation);
    boolean existsById(UUID id);
    void deleteReservation(Reservation reservation);
    Optional<Reservation> findReservationById(UUID id);
}
