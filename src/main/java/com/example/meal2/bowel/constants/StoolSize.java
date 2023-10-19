package com.example.meal2.bowel.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum StoolSize {
    VERY_SMALL("Very small"),
    SMALL("Small"),
    MEDIUM("Medium"),
    LARGE("Large"),
    VERY_LARGE("Very large"),
    NA("N/A");

    private final String desc;
}
