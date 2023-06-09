package com.amcsoftware.carbookingservices.service;

import com.amcsoftware.carbookingservices.exceptions.ResourceExist;
import com.amcsoftware.carbookingservices.exceptions.ResourceNotFound;
import com.amcsoftware.carbookingservices.jpaDataAccess.ReservationJpaDataAccessService;
import com.amcsoftware.carbookingservices.model.Member;
import com.amcsoftware.carbookingservices.model.Reservation;
import com.amcsoftware.carbookingservices.repository.MemberRepository;
import com.amcsoftware.carbookingservices.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import java.util.List;
import java.util.UUID;

@Service
public class ReservationService extends ReservationJpaDataAccessService {

    public ReservationService(MemberRepository memberRepository, ReservationRepository reservationRepository) {
        super(memberRepository, reservationRepository);
    }

    public List<Reservation> findAllReservations() {
        if(reservationCount() != 0) {
            return findAllReservations();
        } else {
            throw new NullPointerException("no reservations available");
        }
    }

    public Member getMember(UUID userId) {
        return findMemberById(userId);
    }

    public void addReservation(Reservation reservation) {
        Member locateMember = findMemberById(reservation.getMember().getUserId());

        if(!locateMember.getReservations().contains(reservation) &&
                reservation.getPickupDate().isAfter(LocalDate.now().minus(1L, ChronoUnit.DAYS)) &&
                reservation.getReturnDate().isAfter(LocalDate.now())) {
            reservation.setCreateAt(LocalDateTime.now());
            locateMember.getReservations().add(reservation);
            saveReservation(reservation);
        } else {
            throw new ResourceExist("reservation [%s] ".formatted(reservation)+ " already exist");
        }
    }

    public void removeReservation(UUID reservationId) {
        Reservation reservation = findReservation(reservationId);
        Member locateMember = findMemberById(reservation.getMember().getUserId());

        if(existsById(reservationId) &&
                locateMember.getReservations().stream().anyMatch(r -> r.getReservationId().equals(reservationId))) {
            locateMember.getReservations().remove(reservation);
            deleteReservation(reservation);
        } else {
            throw new ResourceNotFound("reservation id ".formatted(reservationId) + " doesn't exist ");
        }
    }

    public Reservation findReservation(UUID reservationId) {
         return findReservationById(reservationId).orElseThrow(() ->
                 new ResourceNotFound("reservation [%s]".formatted(reservationId) + " doesn't exist"));
    }

    public void updateReservation(UUID reservationId, Reservation reservation) {
        Reservation locatedReservation = findReservation(reservationId);
        if(!reservation.getCar().equals(locatedReservation.getCar())) {
            locatedReservation.setCar(reservation.getCar());
        }

        if(reservation.getMember().equals(locatedReservation.getMember())) {
            throw new IllegalArgumentException("unable to update user " +
                    locatedReservation.getMember().getUserId() + " to " + reservation.getMember().getUserId() +
                    " delete reservation and then try again");
        }

        if(!reservation.getPickupDate().equals(locatedReservation.getPickupDate())) {
            locatedReservation.setPickupDate(reservation.getPickupDate());
        }

        if(!reservation.getReturnDate().equals(locatedReservation.getReturnDate())) {
            locatedReservation.setReturnDate(reservation.getReturnDate());
        }

        locatedReservation.setCreateAt(LocalDateTime.now());

        saveReservation(locatedReservation);

    }

}
