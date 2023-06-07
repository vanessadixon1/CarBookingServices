package com.amcsoftware.carbookingservices.controller;

import com.amcsoftware.carbookingservices.customResponse.CustomResponse;
import com.amcsoftware.carbookingservices.model.Member;
import com.amcsoftware.carbookingservices.model.Reservation;
import com.amcsoftware.carbookingservices.service.ReservationService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/reservation")
public class reservationController {

    private final ReservationService reservationService;
    Member member = new Member();

    public reservationController(ReservationService reservationService) {
        this.reservationService = reservationService;
    }

    @ResponseBody
    @GetMapping()
    public List<Reservation> getReservations() {
        member.getReservations().forEach(System.out::println);
        return reservationService.findAllReservations();
    }

    @ResponseBody
    @PostMapping()
    public ResponseEntity<CustomResponse> addReservations(@RequestBody Reservation reservation) {
         reservationService.addReservation(reservation);
         CustomResponse response = new CustomResponse();
         response.setMessage("reservation for " + reservationService.getMember(reservation.getMember().getUserId()).getFirstName() + " "
                + " created successful" );

         return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @DeleteMapping("/{reservationId}")
    public ResponseEntity<CustomResponse> deleteReservation(@PathVariable("reservationId") UUID reservationId) {
        reservationService.removeReservation(reservationId);

        CustomResponse response = new CustomResponse();
        response.setMessage("reservation id " + reservationId + " has been removed successfully");

        return ResponseEntity.status(HttpStatus.CREATED).body(response);

    }


}
