package com.example.meal2.mealitem.dto;

import com.example.meal2.aftermealnote.dto.AfterMealNoteInfoDTO;
import com.example.meal2.mealitem.MealItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Set;

public record MealItemDetailedDTO(
        Long id,
        @JsonProperty("user")
        Integer userId,
        @Schema(example="meal description")
        String meal,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate date,
        @Schema(type="string", pattern="hh:mm:ss", format="time", example="12:00:00")
        @DateTimeFormat(pattern="hh:mm:ss")
        LocalTime time,
        @Enumerated(EnumType.STRING)
        @JsonProperty("size")
        MealItem.MealSize mealSize,
        @Schema(example="meal note")
        String note,
        List<AfterMealNoteInfoDTO> afterMealNotes
) {
}
