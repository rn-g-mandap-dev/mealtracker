package com.example.meal2.mealitem.dto;

import com.example.meal2.mealitem.MealItem;
import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record MealItemUpdateDTO(
        @Schema(example="meal description")
        @NotBlank(message="meal => must not be blank")
        @Size(max=512, message="meal => must not exceed 512 characters")
        String meal,
        @NotNull(message="date => must not be blank (yyyy-mm-dd)")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate date,
        @Schema(type="string", pattern="hh:mm:ss", format="time", example="12:00:00")
        @NotNull(message="time => must not be blank (hh:mm:ss)")
        @DateTimeFormat(pattern="hh:mm:ss")
        LocalTime time,
        @NotNull(message="size => must not be blank (light, medium, heavy)")
        @Enumerated(EnumType.STRING)
        @JsonProperty("size")
        MealItem.MealSize mealSize,
        @Schema(example="meal note")
        @Size(max=255, message="note => must not exceed 255 characters")
        String note
) {
}
