package com.zerobase.mentalgrowhdiary.domain;

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
public class Counselor {


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long counselorId;

    @OneToOne
    @JoinColumn(name="user_id",nullable=false)
    private User user;

    private String name;
    private String feature;

    @Column(columnDefinition = "JSON")
    private String keywords;

    @Column(columnDefinition = "JSON")
    private String availableSlots;

    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime updatedDate;

}
