package com.zerobase.mentalgrowhdiary.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.mentalgrowhdiary.domain.Counselor;
import com.zerobase.mentalgrowhdiary.domain.Reservation;
import com.zerobase.mentalgrowhdiary.domain.User;
import com.zerobase.mentalgrowhdiary.dto.DecisionRequest;
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

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
    private final MailService mailService;

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

    @Transactional
    public void decisionReservation(Long reservationId,String counselorName , DecisionRequest request){

        Counselor counselor = counselorRepository.findByUser_Username(counselorName)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.COUNSELOR_NOT_FOUND));


        Reservation reservation = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new MentalGrowthException(ErrorCode.RESERVATION_NOT_FOUND));

        reservation.setStatus(request.getReservationStatus());

        reservationRepository.save(reservation);

        String subject = "예약 상태 변경 알림";
        String body = mailService.getReservationStatusTemplate(reservation.getUser().getEmail(), String.valueOf(request.getReservationStatus()));


        boolean isMailSent = mailService.sendMail(reservation.getUser().getEmail(), subject, body);

        if(!isMailSent) {
            throw new MentalGrowthException(ErrorCode.MAIL_SEND_FAIL);
        }
    }

    @Transactional
    public void sendDailyCounselingReminders(){

        LocalDate today = LocalDate.now();
        LocalDateTime startDate = today.atStartOfDay();
        LocalDateTime endDate = today.atTime(LocalTime.MAX);

        List<Reservation> reservations = reservationRepository.findByReservationDateTimeBetween(startDate,endDate);

        if(reservations.isEmpty()) {
            return;
        }

        for(Reservation reservation : reservations) {
            sendDailyCounselingReminder(reservation);
        }
    }

    private void sendDailyCounselingReminder(Reservation reservation) {
        String counselorEmail = reservation.getCounselor().getUser().getEmail();
        String counselorName = reservation.getCounselor().getUser().getUsername();
        String clientName = reservation.getUser().getUsername();

        String subject = "당일 상담 일정 알림 ";
        String body = mailService.getOfflineCounselingReminderTemplate(clientName,counselorName);


       boolean isMailSent = mailService.sendMail(counselorEmail, subject, body);

       if(!isMailSent) {
           throw new MentalGrowthException(ErrorCode.MAIL_SEND_FAIL);
       }
    }
}
