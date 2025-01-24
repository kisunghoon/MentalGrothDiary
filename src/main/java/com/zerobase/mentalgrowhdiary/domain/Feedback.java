package com.zerobase.mentalgrowhdiary.domain;

import com.zerobase.mentalgrowhdiary.type.FeedbackStatus;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@EntityListeners(AuditingEntityListener.class)
@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feedbackId;

    @OneToOne
    @JoinColumn(name="diary_id",nullable = false,unique = true)
    private Diary diary;

    @ManyToOne
    @JoinColumn(name="counselor_id",nullable = false)
    private Counselor counselor;

    @Column(columnDefinition = "LONGTEXT")
    private String feedbackContent;

    @Enumerated(EnumType.STRING)
    private FeedbackStatus feedbackStatus;

    @CreatedDate
    private LocalDateTime createdDate;

    @LastModifiedDate
    private LocalDateTime updatedDate;


}
