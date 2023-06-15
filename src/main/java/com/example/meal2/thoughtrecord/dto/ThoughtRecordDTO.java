package com.example.meal2.thoughtrecord.dto;

import com.example.meal2.thoughtrecord.entity.Mood;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

public record ThoughtRecordDTO(
        Long id,
        Integer userId,
        @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
        LocalDate date,
        @DateTimeFormat(pattern="hh:mm:ss")
        LocalTime time,
        String situation,
        List<MoodDTO> moods,
        List<ThoughtDTO> thoughts
) {
}
