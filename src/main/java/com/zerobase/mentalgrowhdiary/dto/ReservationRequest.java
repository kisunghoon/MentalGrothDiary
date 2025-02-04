package com.zerobase.mentalgrowhdiary.dto;

import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class ReservationRequest {
    private Long userId;
    private Long counselorId;
    private String reservationTime; // (예: "2025-02-03 10:00")
}
