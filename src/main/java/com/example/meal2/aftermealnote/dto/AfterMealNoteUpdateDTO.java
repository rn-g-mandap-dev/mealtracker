package com.example.meal2.aftermealnote.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public record AfterMealNoteUpdateDTO(
        @NotNull(message="date => must not be blank (yyyy-mm-dd)")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate date,
        @Schema(type="string", pattern="hh:mm:ss", format="time", example="12:00:00")
        @NotNull(message="time => must not be blank (hh:mm:ss)")
        @DateTimeFormat(pattern="hh:mm:ss")
        LocalTime time,
        @Size(max=255, message="note => must not exceed 255 characters")
        String note
) {
}
