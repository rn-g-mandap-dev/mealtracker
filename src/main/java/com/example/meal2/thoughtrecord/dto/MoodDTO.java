package com.example.meal2.thoughtrecord.dto;

import com.example.meal2.thoughtrecord.entity.MoodType;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;

public record MoodDTO(
        Long id,
        @Enumerated(EnumType.STRING)
        @JsonProperty("moodType")
        MoodType mood,
        Integer level
) {
}
