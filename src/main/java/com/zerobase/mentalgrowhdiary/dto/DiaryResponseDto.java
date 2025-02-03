package com.zerobase.mentalgrowhdiary.dto;

import com.zerobase.mentalgrowhdiary.domain.Diary;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class DiaryResponseDto {

    private Long diaryId;
    private LocalDate diaryDate;
    private String diaryContent;
    private String hashtag;

    public static DiaryResponseDto fromEntity(Diary diary) {
        return new DiaryResponseDto(
                diary.getDiaryId(),
                diary.getDiaryDate(),
                diary.getDiaryContent(),
                diary.getHashtag()
        );
    }
}
