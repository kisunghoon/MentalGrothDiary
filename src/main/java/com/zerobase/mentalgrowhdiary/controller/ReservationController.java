package com.zerobase.mentalgrowhdiary.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zerobase.mentalgrowhdiary.dto.ReservationRequest;
import com.zerobase.mentalgrowhdiary.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/reservations")
public class ReservationController {

    private final ReservationService reservationService;

    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/application")
    public ResponseEntity<?> registerReservation(@RequestBody ReservationRequest request,
                                                 Authentication authentication) throws JsonProcessingException {

        String client = authentication.getName();

        reservationService.registerReservation(request,client);

        return ResponseEntity.ok("예약이 등록되었습니다.");
    }



}
