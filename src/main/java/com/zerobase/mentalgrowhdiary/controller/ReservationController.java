package com.zerobase.mentalgrowhdiary.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zerobase.mentalgrowhdiary.dto.DecisionRequest;
import com.zerobase.mentalgrowhdiary.dto.ReservationRequest;
import com.zerobase.mentalgrowhdiary.service.ReservationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

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

    @PreAuthorize("hasRole('COUNSELOR')")
    @PutMapping("/{reservationId}/decision")
    public ResponseEntity<?> decisionReservation(@PathVariable Long reservationId, @RequestBody DecisionRequest request,
                                                 Authentication authentication){

        String counselor = authentication.getName();

        reservationService.decisionReservation(reservationId,counselor,request);

        return ResponseEntity.ok("상담 요청에 응답을 하셨습니다.");
    }




}
