package com.example.meal2.aftermealnote.dto;

import com.example.meal2.mealitem.dto.MealItemRes;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.LocalDate;
import java.time.LocalTime;

public record AfterMealNoteRes (
        Long id,
        Long mealItemId,
        LocalDate date,
        LocalTime time,
        String note,
        @JsonProperty("mealItem")
        MealItemRes mealItemRes
){}
