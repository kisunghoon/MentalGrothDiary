package com.zerobase.mentalgrowhdiary.domain;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.JdbcTypeCode;
import org.hibernate.type.SqlTypes;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

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

    private String feature;

    @Column(name="keywords",columnDefinition = "JSON")
    private String keywords;

    @Column(name="available_slots",columnDefinition = "JSON")
    private String  availableSlots;

    @CreatedDate
    private LocalDateTime createdDate;
    @LastModifiedDate
    private LocalDateTime updatedDate;

}
