package com.sumin.planmate.dto.dailytask;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoItemRequestDto {

    @NotNull
    private LocalDate date;

    @NotBlank
    private String title;

    public static TodoItemRequestDto create(LocalDate date, String title){
        return new TodoItemRequestDto(date, title);
    }
}
