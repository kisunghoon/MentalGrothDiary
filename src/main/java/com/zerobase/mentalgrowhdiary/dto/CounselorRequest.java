package com.zerobase.mentalgrowhdiary.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class CounselorRequest {

    private Long userId;
    private String name;
    private String feature;
    private List<String> keywords;
    private List<Map<String,String>> availableSlots;
}
