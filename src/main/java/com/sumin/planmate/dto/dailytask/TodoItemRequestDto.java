package com.sumin.planmate.dto.dailytask;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class TodoItemRequestDto {

    @NotBlank
    private String title;
    private String description;

    @NotNull
    private LocalDate date;
}
