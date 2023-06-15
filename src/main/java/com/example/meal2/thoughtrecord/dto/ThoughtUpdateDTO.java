package com.example.meal2.thoughtrecord.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;

public record ThoughtUpdateDTO(
        Long id,
        @Schema(example="thought description")
        @NotBlank(message="thought => must not be blank (ThoughtCreationDTO)")
        @Size(max=512, message="thought => must not exceed 512 characters (ThoughtCreationDTO)")
        String thought,
        @NotNull(message="level => must not be blank (ThoughtCreationDTO)")
        @Min(value=1, message="level => must be between 1 and 100 (ThoughtCreationDTO)")
        @Max(value=100, message="level => must be between 1 and 100 (ThoughtCreationDTO)")
        Integer level
) {
}
