package com.sumin.planmate.dto.diary;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;

@Getter
public class DiaryUpdateDto {

    @NotBlank
    @Size(max = 50)
    private String title;

    private String content;
}
