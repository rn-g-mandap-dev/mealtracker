package com.example.meal2.thoughtrecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record ThoughtDTO(
        Long id,
        @Schema(example="thought description")
        String thought,
        Integer level
) {
}
