package com.sumin.planmate.dto.dailytask;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class TodoItemRequestDto {

    @NotBlank
    private String title;

    @NotNull
    private LocalDate date;

    public static TodoItemRequestDto create(String title, LocalDate date){
        return new TodoItemRequestDto(title, date);
    }
}
