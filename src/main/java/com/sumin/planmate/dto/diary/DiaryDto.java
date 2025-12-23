package com.sumin.planmate.dto.diary;

import com.sumin.planmate.entity.Diary;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@Builder
public class DiaryDto {
    private Long DiaryId;
    private LocalDate date;
    private String title;
    private String content;

    public static DiaryDto from(Diary diary) {
        return DiaryDto.builder()
                .DiaryId(diary.getId())
                .date(diary.getDate())
                .title(diary.getTitle())
                .content(diary.getContent())
                .build();
    }

    public static DiaryDto empty(){
        return DiaryDto.builder().build();
    }
}
