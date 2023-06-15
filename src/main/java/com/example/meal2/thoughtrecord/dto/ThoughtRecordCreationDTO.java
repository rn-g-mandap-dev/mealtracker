package com.example.meal2.thoughtrecord.dto;

import com.example.meal2.thoughtrecord.entity.Mood;
import com.example.meal2.thoughtrecord.entity.Thought;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ThoughtRecordCreationDTO(
        @NotNull(message="date => must not be blank (yyyy-mm-dd) (ThoughtRecordCreationDTO)")
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate date,
        @Schema(type="string", pattern="hh:mm:ss", format="time", example="12:00:00")
        @NotNull(message="time => must not be blank (hh:mm:ss) (ThoughtRecordCreationDTO)")
        @DateTimeFormat(pattern="hh:mm:ss")
        LocalTime time,
        @Schema(example="situation description")
        @NotBlank(message="situation => must not be blank (ThoughtRecordCreationDTO)")
        @Size(max=512, message="situation => must not exceed 512 characters (ThoughtRecordCreationDTO)")
        String situation,

        @NotNull(message="moods => must not be empty (ThoughtRecordCreationDTO)")
        @NotEmpty(message="moods => must not be empty (ThoughtRecordCreationDTO)")
        @Size(min=1, max=12, message="moods => must have 1 to 12 items (ThoughtRecordCreationDTO)")
        List<@Valid MoodCreationDTO> moods,

        @NotNull(message="thoughts => must not be empty (ThoughtRecordCreationDTO)")
        @NotEmpty(message="thoughts => must not be empty (ThoughtRecordCreationDTO)")
        @Size(min=1, max=24, message="thoughts => must have 1 to 24 items (ThoughtRecordCreationDTO)")
        List<@Valid ThoughtCreationDTO> thoughts
) {
}
