package com.example.meal2.aftermealnote.dto;

import com.example.meal2.mealitem.dto.MealItemInfoDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record AfterMealNoteDetailedDTO(
        Long id,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate date,
        @Schema(type="string", pattern="hh:mm:ss", format="time", example="12:00:00")
        @DateTimeFormat(pattern="hh:mm:ss")
        LocalTime time,
        String note,
        MealItemInfoDTO mealItem
) {
}
