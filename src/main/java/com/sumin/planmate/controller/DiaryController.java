package com.sumin.planmate.controller;

import com.sumin.planmate.dto.diary.DiaryDto;
import com.sumin.planmate.dto.diary.DiaryRequestDto;
import com.sumin.planmate.dto.diary.DiaryUpdateDto;
import com.sumin.planmate.dto.user.CustomUserDetails;
import com.sumin.planmate.service.DiaryService;
import com.sumin.planmate.util.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/diary")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    public ApiResponse<DiaryDto> createDiary(@Valid @RequestBody DiaryRequestDto dto,
                                   @AuthenticationPrincipal CustomUserDetails userDetails) {
        DiaryDto diary = diaryService.writeDiary(dto, userDetails.getUserId());
        return new ApiResponse<>(200, "일기 추가 완료", diary);
    }

    @GetMapping
    public ApiResponse<DiaryDto> getDiary(LocalDate date,
                                          @AuthenticationPrincipal CustomUserDetails userDetails) {
        DiaryDto diary = diaryService.getDiaryByDate(date, userDetails.getUserId());
        return new ApiResponse<>(200, "일기 조회 완료", diary);
    }

    @PutMapping("/{diaryId}")
    public ApiResponse<DiaryDto> updateDiary(@PathVariable Long diaryId,
                                             @Valid @RequestBody DiaryUpdateDto dto,
                                             @AuthenticationPrincipal CustomUserDetails userDetails) {
        DiaryDto diary = diaryService.updateDiary(diaryId, dto, userDetails.getUserId());
        return new ApiResponse<>(200, "일기 수정 완료", diary);
    }

    @DeleteMapping("/{diaryId}")
    public ApiResponse<String> deleteDiary(@PathVariable Long diaryId,
                                           @AuthenticationPrincipal CustomUserDetails userDetails) {
        diaryService.deleteDiary(diaryId, userDetails.getUserId());
        return new ApiResponse<>(200, "일기 삭제 완료", null);
    }
}
