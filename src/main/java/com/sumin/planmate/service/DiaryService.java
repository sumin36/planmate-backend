package com.sumin.planmate.service;

import com.sumin.planmate.dto.diary.DiaryDto;
import com.sumin.planmate.dto.diary.DiaryRequestDto;
import com.sumin.planmate.dto.diary.DiaryUpdateDto;
import com.sumin.planmate.entity.Diary;
import com.sumin.planmate.entity.User;
import com.sumin.planmate.exception.NotFoundException;
import com.sumin.planmate.repository.DiaryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryService {

    private final UserService userService;
    private final DiaryRepository diaryRepository;

    // 일기 쓰기
    public DiaryDto writeDiary(DiaryRequestDto dto, Long userId) {
        User user = userService.getUser(userId);
        Diary diary = Diary.builder()
                .date(dto.getDate())
                .title(dto.getTitle())
                .content(dto.getContent())
                .user(user)
                .build();
        diaryRepository.save(diary);
        return DiaryDto.from(diary);
    }

    // 일기 조회
    @Transactional(readOnly = true)
    public DiaryDto getDiaryByDate(LocalDate date, Long userId) {
        Diary diary = diaryRepository.findByUserIdAndDate(userId, date);
        if(diary == null) return DiaryDto.empty();

        return DiaryDto.from(diary);
    }

    // 일기 수정
    public DiaryDto updateDiary(Long diaryId, DiaryUpdateDto dto, Long userId) {
        Diary diary = getDiary(diaryId);
        validateOwnership(userId, diary);
        diary.update(dto.getTitle(), dto.getContent());
        return DiaryDto.from(diary);
    }

    // 일기 삭제
    public void deleteDiary(Long diaryId, Long userId) {
        Diary diary = getDiary(diaryId);
        validateOwnership(userId, diary);
        diaryRepository.delete(diary);
    }

    private Diary getDiary(Long diaryId){
        return diaryRepository.findById(diaryId)
                .orElseThrow(() -> new NotFoundException("해당 날짜의 일기가 존재하지 않습니다."));
    }

    private void validateOwnership(Long userId, Diary diary) {
        if(!diary.getUser().getId().equals(userId)){
            throw new AccessDeniedException("접근 권한이 없습니다.");
        }
    }
}
