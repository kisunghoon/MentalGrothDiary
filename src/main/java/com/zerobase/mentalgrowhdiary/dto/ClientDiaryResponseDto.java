package com.zerobase.mentalgrowhdiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ClientDiaryResponseDto {

    private Long userId;
    private String username;
    private Long diaryId;
    private String diaryContent;
    private LocalDate diaryDate;

}
