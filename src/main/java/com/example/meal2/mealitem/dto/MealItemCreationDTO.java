package com.example.meal2.mealitem.dto;

import com.example.meal2.mealitem.MealItem;

import java.time.LocalDate;
import java.time.LocalTime;

public record MealItemCreationDTO(
        Integer userId,
        String meal,
        LocalDate date,
        LocalTime time,
        MealItem.MealSize mealSize,
        String note
) {
}
