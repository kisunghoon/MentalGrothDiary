package com.zerobase.mentalgrowhdiary.service;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

@Service
@Slf4j
@RequiredArgsConstructor
public class MailService {

    private final JavaMailSender mailSender;

    private static final String FROM_EMAIL = "kisunghoon29@gmail.com";

    public void sendMailTest() {
        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,false,"UTF-8");

            helper.setTo("kisunghoon22@naver.com");
            helper.setSubject("메일 테스트 기능입니다.");
            helper.setText("테스트 입니다.",false);
            helper.setFrom(FROM_EMAIL);


            mailSender.send(message);
            log.info("메일 전송 완료~~!");


        } catch(MessagingException e){
            log.error("메일 전송 실패" + e.getMessage());
        }
    }

    public boolean sendMail(String toMail, String subject, String body){

        try{
            MimeMessage message = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(message,false,"UTF-8");

            helper.setTo(toMail);
            helper.setSubject(subject);
            helper.setText(body,true);
            helper.setFrom(FROM_EMAIL);

            mailSender.send(message);

            return true;
        } catch(MessagingException e){
            log.error("메일 전송 실패 " + e.getMessage());
            return false;
        }
    }

    public String getFeedbackCompletedTemplete(String username){

        return "<h3>안녕하세요, " + username + "님!</h3>"
                + "<p>귀하의 일기에 대한 상담사의 피드백이 완료되었습니다.</p>"
                + "<p>상세한 피드백 내용을 확인하려면 웹사이트에 로그인하여 확인해 주세요.</p>"
                + "<p>항상 마음의 안정을 찾기를 바랍니다.</p>"
                + "<p>감사합니다.</p>";

    }

    public String getFeedbackReminderTemplate(String counselorName, String clientName, Long diaryId) {

        return "<h3>안녕하세요 ," + counselorName + "님!</h3>"
                + "<p>" + clientName + "의 작성한 일기 "+ " 의 대한 피드백이 아직 작성되지 않았습니다.</p>"
                + "<p> 빠른 시일 내의 피드백을 작성해주시길 부탁 드립니다.</p>"
                + "<p> 감사합니다.</p>";
    }

    /**
     * 예약 상태 변경 이메일 템플릿 (수락 또는 거절)
     */

    public String getReservationStatusTemplate(String username, String status) {
        String statusMessage = status.equalsIgnoreCase("APPROVED") ?
                "<p>귀하의 예약이 <strong style='color:green;'>수락</strong>되었습니다.</p>" :
                "<p>죄송합니다. 귀하의 예약이 <strong style='color:red;'>거절</strong>되었습니다.</p>";

        return "<h3>안녕하세요, " + username + "님!</h3>"
                + statusMessage
                + "<p>자세한 내용은 웹사이트에서 확인해주세요.</p>"
                + "<p>감사합니다.</p>";
    }

    /**
     * 당일 예약된 오프라인 상담 일정 알림 이메일 템플릿
     */
    public String getOfflineCounselingReminderTemplate(String clientName,String counselorName) {
        return "<h3>안녕하세요, " + counselorName + "님!</h3>"
                + "<p>오늘 예약된 "+ clientName + "님 내담자와의 " + " 오프라인 상담 일정이 예약 되어있는 점 상기시켜 드립니다. </p>"
                + "<p>감사합니다.</p>";
    }

}
