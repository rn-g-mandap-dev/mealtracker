package com.example.meal2.thoughtrecord.dto;

import com.example.meal2.thoughtrecord.entity.MoodType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.Column;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record MoodCreationDTO(
        @NotNull(message="moodType => must not be blank (MoodCreationDTO)")
        @Enumerated(EnumType.STRING)
        @JsonProperty("moodType")
        MoodType mood,
        @NotNull(message="level => must not be blank (MoodCreationDTO)")
        @Min(value=1, message="level => must be between 1 and 100 (MoodCreationDTO)")
        @Max(value=100, message="level => must be between 1 and 100 (MoodCreationDTO)")
        Integer level
) {
}
