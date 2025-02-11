package com.zerobase.mentalgrowhdiary.dto;


import com.zerobase.mentalgrowhdiary.type.ReservationStatus;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DecisionRequest {
    private ReservationStatus reservationStatus;
}
