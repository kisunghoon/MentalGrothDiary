package com.zerobase.mentalgrowhdiary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.mentalgrowhdiary.domain.Counselor;
import com.zerobase.mentalgrowhdiary.domain.Reservation;
import com.zerobase.mentalgrowhdiary.domain.User;
import com.zerobase.mentalgrowhdiary.dto.ReservationRequest;
import com.zerobase.mentalgrowhdiary.exception.MentalGrowthException;
import com.zerobase.mentalgrowhdiary.repository.CounselorRepository;
import com.zerobase.mentalgrowhdiary.repository.ReservationRepository;
import com.zerobase.mentalgrowhdiary.repository.UserRepository;
import com.zerobase.mentalgrowhdiary.type.ErrorCode;
import com.zerobase.mentalgrowhdiary.type.ReservationStatus;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;

@Service
@Slf4j
@RequiredArgsConstructor
public class ReservationService {

    private final UserRepository userRepository;
    private final CounselorRepository counselorRepository;
    private final ReservationRepository reservationRepository;
    private final ObjectMapper objectMapper;

    @Transactional(isolation = Isolation.SERIALIZABLE)
    public void registerReservation(ReservationRequest request, String client) throws JsonProcessingException {

        User user = userRepository.findByUsername(client)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.USER_NOT_FOUND));

        Counselor counselor = counselorRepository.findById(request.getCounselorId())
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.COUNSELOR_NOT_FOUND));

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime reservationTime = LocalDateTime.parse(request.getReservationTime(), formatter);

        validationReservationTime(reservationTime,counselor);

        Reservation reservation = Reservation.builder()
                .user(user)
                .counselor(counselor)
                .reservationDateTime(reservationTime)
                .status(ReservationStatus.PENDING)
                .build();

        reservationRepository.save(reservation);
    }

    private void validationReservationTime(LocalDateTime reservationTime , Counselor counselor) throws JsonProcessingException {

        String dayOfWeek = reservationTime.getDayOfWeek().toString();

        int hour = reservationTime.getHour();

        List<Map<String,String>> availableSlots = parseAvailableSlots(counselor.getAvailableSlots());

        boolean isAvailable = availableSlots.stream().anyMatch(slot -> {
           String day = slot.get("day");
           String time = slot.get("time");

           return day.equalsIgnoreCase(dayOfWeek) && timeInRange(hour,time);
        });

        if(!isAvailable) {
            throw new MentalGrowthException(ErrorCode.NO_AVAILABLE_SLOTS);
        }
    }

    private List<Map<String,String>> parseAvailableSlots(String availableSlotsJson) throws JsonProcessingException {

        return objectMapper.readValue(availableSlotsJson, new TypeReference<List<Map<String,String>>>() {});
    }

    //상담시간은 무조건 1시간
    private boolean timeInRange(int hour , String timeRange) {
        String[] times = timeRange.split("-");
        int startHour = Integer.parseInt(times[0].split(":")[0]);
        int endHour = Integer.parseInt(times[1].split(":")[0]);

        return hour >= startHour && hour <= endHour;
    }
}
