package com.zerobase.mentalgrowhdiary.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.zerobase.mentalgrowhdiary.dto.DiaryRequest;
import com.zerobase.mentalgrowhdiary.dto.DiaryResponseDto;
import com.zerobase.mentalgrowhdiary.service.DiaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/diary")
public class DiaryController {

    private final DiaryService diaryService;

    /**
     * 일기 작성
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @PreAuthorize("hasRole('CLIENT')")
    @PostMapping("/write")
    public ResponseEntity<?> writeDiary(@RequestBody DiaryRequest request,
                                        Authentication authentication) throws JsonProcessingException {


        String client = authentication.getName();
        diaryService.registerDiary(request,client);

        return ResponseEntity.ok("오늘의 일기가 작성되었습니다.");
    }

    /**
     * 일기 수정
     * @param request
     * @return
     * @throws JsonProcessingException
     */
    @PreAuthorize("hasRole('CLIENT')")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateDiary(@PathVariable Long id, @RequestBody DiaryRequest request
                                        ,Authentication authentication) throws JsonProcessingException {


        String client = authentication.getName();

        diaryService.updateDiary(client,id,request);

        return ResponseEntity.ok("오늘의 일기가 수정되었습니다.");
    }

    /**
     * 일기 삭제
     * @param id
     * @return
     * @throws JsonProcessingException
     */
    @PreAuthorize("hasRole('CLIENT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteDiary(@PathVariable Long id, Authentication authentication){

        String client = authentication.getName();

        diaryService.deleteDiary(id , client);

        return ResponseEntity.ok("오늘의 일기가 삭제되었습니다.");
    }

    /**
     * 사용자 일기 조회
     * @param diaryDate
     * @param hashtag
     * @return
     */
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/search")
    public ResponseEntity<List<DiaryResponseDto>> searchDiary(
            @RequestParam(required = false) LocalDate diaryDate,
            @RequestParam(required = false) String hashtag
            , Authentication authentication) {


        String client = authentication.getName();

        List<DiaryResponseDto> results = diaryService.searchDiary(client,diaryDate,hashtag);

        return ResponseEntity.ok(results);
    }

    /**
     * 해시태그 접두사 기준 단어
     * @param authentication
     * @param prefix
     * @return
     * @throws JsonProcessingException
     */
    @PreAuthorize("hasRole('CLIENT')")
    @GetMapping("/hashtag")
    public ResponseEntity<List<String>> getHashtagSuggest(Authentication authentication,@RequestParam String prefix) throws JsonProcessingException {

        String client = authentication.getName();

        List<String> suggestion = diaryService.getHashtagSuggest(client,prefix);

        return ResponseEntity.ok(suggestion);
    }
}
