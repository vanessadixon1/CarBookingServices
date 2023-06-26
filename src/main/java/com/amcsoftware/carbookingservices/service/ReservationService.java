package com.amcsoftware.carbookingservices.service;

import com.amcsoftware.carbookingservices.dao.ReservationDao;
import com.amcsoftware.carbookingservices.exceptions.BadRequestException;
import com.amcsoftware.carbookingservices.exceptions.DuplicateResourceException;
import com.amcsoftware.carbookingservices.exceptions.ResourceNotFoundException;
import com.amcsoftware.carbookingservices.model.Member;
import com.amcsoftware.carbookingservices.model.Reservation;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import java.util.List;
import java.util.UUID;

@Service
public class ReservationService {

    private final ReservationDao reservationDao;

    public ReservationService(@Qualifier("reservationJpa") ReservationDao reservationDao) {

        this.reservationDao = reservationDao;
    }

    public List<Reservation> findAllReservations() {
        if(reservationDao.reservationCount() != 0) {
            return reservationDao.getAllReservations();
        } else {
            throw new NullPointerException("no reservations available");
        }
    }

    public Member getMember(UUID userId) {
        return reservationDao.findMemberById(userId);
    }

    public void addReservation(Reservation reservation) {

        Member locateMember = reservationDao.findMemberById(reservation.getMember().getUserId());

        if(reservationDao.reservationContainCar(reservation.getCar())) {

            Reservation res = reservationDao.findReservationByCar(reservation.getCar());

            LocalDate currentBorrowerReturnDate = res.getReturnDate();

            if(currentBorrowerReturnDate.isBefore(reservation.getPickupDate()) &&
                    reservation.getPickupDate().isAfter(LocalDate.now().minus(1L, ChronoUnit.DAYS)) &&
                    reservation.getReturnDate().isAfter(LocalDate.now())) {

                reservation.setCreateAt(LocalDateTime.now());

                locateMember.getReservations().add(reservation);

                reservationDao.saveReservation(reservation);

            }
        }else if(!locateMember.getReservations().contains(reservation) && !reservationDao.reservationContainCar(reservation.getCar()) &&
                reservation.getPickupDate().isAfter(LocalDate.now().minus(1L, ChronoUnit.DAYS)) &&
                reservation.getReturnDate().isAfter(LocalDate.now())) {

            reservation.setCreateAt(LocalDateTime.now());

            locateMember.getReservations().add(reservation);

            reservationDao.saveReservation(reservation);

        } else {
            throw new DuplicateResourceException("reservation %s".formatted(reservation)+ " already exist during that time");
        }
    }

    public void removeReservation(UUID reservationId) {

        Reservation reservation = findReservation(reservationId);

        Member locateMember = reservationDao.findMemberById(reservation.getMember().getUserId());

        if(reservationDao.existsById(reservationId) &&
                locateMember.getReservations().stream().anyMatch(r -> r.getReservationId().equals(reservationId))) {
            locateMember.getReservations().remove(reservation);
            reservationDao.deleteReservation(reservation);
        } else {
            throw new ResourceNotFoundException("reservation id %s".formatted(reservationId) + " doesn't exist");
        }
    }

    public Reservation findReservation(UUID reservationId) {
         return reservationDao.findReservationById(reservationId).orElseThrow(() ->
                 new ResourceNotFoundException("reservation %s".formatted(reservationId) + " doesn't exist"));
    }

    public void updateReservation(UUID reservationId, Reservation reservation) {

        if(!reservationDao.existsByReservationId(reservationId)) {
            throw new BadRequestException("no reservation found for %s".formatted(reservationId));
        }

        boolean changed = false;

        Reservation locatedReservation = findReservation(reservationId);

        if(!reservation.getMember().equals(locatedReservation.getMember())) {
            throw new BadRequestException("unable to update user " +
                    locatedReservation.getMember().getUserId() + " to " + reservation.getMember().getUserId() +
                    " delete reservation and then try again");
        }

        if(reservation.getCar() != null && !reservation.getCar().equals(locatedReservation.getCar())) {
            locatedReservation.setCar(reservation.getCar());
            changed = true;
        }

        if(reservation.getPickupDate() != null && !reservation.getPickupDate().equals(locatedReservation.getPickupDate())) {
            locatedReservation.setPickupDate(reservation.getPickupDate());
            changed = true;
        }

        if(reservation.getReturnDate() != null && !reservation.getReturnDate().equals(locatedReservation.getReturnDate())) {
            locatedReservation.setReturnDate(reservation.getReturnDate());
            changed = true;
        }

        if(!changed) {
            throw new BadRequestException("no changes made");
        }

        locatedReservation.setCreateAt(LocalDateTime.now());

        reservationDao.saveReservation(locatedReservation);

    }

}
