package com.zerobase.mentalgrowhdiary.dto;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zerobase.mentalgrowhdiary.domain.Counselor;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Collections;
import java.util.List;
import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class CounselorResponseDto {
    private Long id;
    private String name;
    private String feature;
    private List<String> keywords;
    private List<Map<String,String>> availableSlots;


    private static final ObjectMapper objectMapper = new ObjectMapper();


    public static CounselorResponseDto fromEntity(Counselor counselor) {
        return new CounselorResponseDto(
                counselor.getCounselorId(),
                counselor.getUser().getUsername(),
                counselor.getFeature(),
                parseJsonList(counselor.getKeywords()),
                parseJsonMapList(counselor.getAvailableSlots())
        );
    }

    /**
     * JSON -> List<String>
     */
    private static List<String> parseJsonList(String json) {

        if(json == null || json.isEmpty()){
            return Collections.emptyList();
        }

        try{

            return objectMapper.readValue(json, new TypeReference<List<String>>() {});

        } catch(JsonProcessingException e){
            return Collections.emptyList();
        }
    }

    /**
     * JSON -> List<Map<String,String>> 변환 하는 메서드
     */
    private static List<Map<String,String>> parseJsonMapList(String json) {
        if(json == null || json.isEmpty()){
            return Collections.emptyList();
        }

        try {
            return objectMapper.readValue(json, new TypeReference<List<Map<String, String>>>() {});
        } catch(JsonProcessingException e){
            return Collections.emptyList();
        }
    }

    /**
     * availableSlots에서 day 리스트 만 추출
     */
/*
    public List<String> getDays(){
        return availableSlots.stream().map(slot -> slot.get("day")).toList();
    } */

    /**
     * availableSlots에서 time 리스트만 추출
     */
/*
    public List<String> getTimes(){
        return availableSlots.stream().map(slot -> slot.get("time")).toList();
    }
*/
}
