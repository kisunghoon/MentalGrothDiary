package com.zerobase.mentalgrowhdiary.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class DiaryRequest {

    private LocalDate diaryDate;
    private Map<String,String> diaryContent;
    private List<String> hashtag;
}
