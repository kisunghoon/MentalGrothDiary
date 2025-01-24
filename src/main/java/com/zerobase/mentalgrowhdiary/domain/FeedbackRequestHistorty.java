package com.zerobase.mentalgrowhdiary.domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FeedbackRequestHistorty {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackRequestHistoryId;

    private Long diaryId;
    private Long userId;
    private Long counselorId;

    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime updateDate;
}
