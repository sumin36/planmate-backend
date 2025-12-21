package com.sumin.planmate.dto.diary;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class DiaryRequestDto {

    @NotNull
    private LocalDate date;

    @NotBlank
    @Size(max = 50)
    private String title;

    private String content;
}
