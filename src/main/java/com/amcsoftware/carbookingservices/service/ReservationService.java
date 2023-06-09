package com.amcsoftware.carbookingservices.service;

import com.amcsoftware.carbookingservices.exceptions.ResourceExist;
import com.amcsoftware.carbookingservices.exceptions.ResourceNotFound;
import com.amcsoftware.carbookingservices.model.Car;
import com.amcsoftware.carbookingservices.model.Member;
import com.amcsoftware.carbookingservices.model.Reservation;
import com.amcsoftware.carbookingservices.repository.CarRepository;
import com.amcsoftware.carbookingservices.repository.MemberRepository;
import com.amcsoftware.carbookingservices.repository.ReservationRepository;
import org.springframework.stereotype.Repository;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAmount;
import java.time.temporal.TemporalUnit;
import java.util.List;
import java.util.UUID;

@Repository
public class ReservationService {
    private final ReservationRepository reservationRepository;
    private final MemberRepository memberRepository;
    private final CarRepository carRepository;
    private final MemberService memberService;

    public ReservationService(ReservationRepository reservationRepository,
                              MemberRepository memberRepository, CarRepository carRepository, MemberService memberService) {
        this.reservationRepository = reservationRepository;
        this.memberRepository = memberRepository;
        this.carRepository = carRepository;
        this.memberService = memberService;
    }

    public List<Reservation> findAllReservations() {
        if(reservationRepository.count() != 0) {
            return reservationRepository.findAll();
        } else {
            throw new NullPointerException("no reservations available");
        }
    }

    public Member getMember(UUID userId) {
        return memberService.findMemberById(userId);
    }

    public void addReservation(Reservation reservation) {
        Member locateMember = memberRepository.findByUserId(reservation.getMember().getUserId());

        if(!locateMember.getReservations().contains(reservation) &&
                reservation.getPickupDate().isAfter(LocalDate.now().minus(1L, ChronoUnit.DAYS)) &&
                reservation.getReturnDate().isAfter(LocalDate.now())) {
            reservation.setCreateAt(LocalDateTime.now());
            locateMember.getReservations().add(reservation);
            reservationRepository.save(reservation);
        } else {
            throw new ResourceExist("reservation [%s] ".formatted(reservation)+ " already exist");
        }
    }

    public void removeReservation(UUID reservationId) {
        Reservation reservation = findReservation(reservationId);
        Member locateMember = memberRepository.findByUserId(reservation.getMember().getUserId());

        if(reservationRepository.existsById(reservationId) &&
                locateMember.getReservations().stream().anyMatch(r -> r.getReservationId().equals(reservationId))) {
            locateMember.getReservations().remove(reservation);
            reservationRepository.delete(reservation);
        } else {
            throw new ResourceNotFound("reservation id ".formatted(reservationId) + " doesn't exist ");
        }
    }

    public Reservation findReservation(UUID reservationId) {
         return reservationRepository.findById(reservationId).orElseThrow(() ->
                 new ResourceNotFound("reservation [%s]".formatted(reservationId) + " doesn't exist"));
    }


    public void updateReservation(UUID reservationId, Reservation reservation) {
        Reservation locatedReservation = findReservation(reservationId);
        if(!reservation.getCar().equals(locatedReservation.getCar())) {
            locatedReservation.setCar(reservation.getCar());
        }

        if(reservation.getMember().equals(locatedReservation.getMember())) {
            throw new IllegalArgumentException("unable to update user " +
                    locatedReservation.getMember().getUserId() + " to " + reservation.getMember().getUserId() + " delete reservation and then try again");
        }

        if(!reservation.getPickupDate().equals(locatedReservation.getPickupDate())) {
            locatedReservation.setPickupDate(reservation.getPickupDate());
        }

        if(!reservation.getReturnDate().equals(locatedReservation.getReturnDate())) {
            locatedReservation.setReturnDate(reservation.getReturnDate());
        }

        locatedReservation.setCreateAt(LocalDateTime.now());

        reservationRepository.save(locatedReservation);

    }

}
