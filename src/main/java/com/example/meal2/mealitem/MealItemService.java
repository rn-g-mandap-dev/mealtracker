package com.example.meal2.mealitem;


import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface MealItemService {
    List<MealItem> getAllMealItems(
            String search,
            Integer page,
            Integer size,
            MealItem.MealSize mealSize,
            LocalDate startDate,
            LocalDate endDate);
    void saveMealItem(MealItem mealItem);
    void updateMealItem(MealItem mealItem);
    Optional<MealItem> getMealItemById(Long id);
    void deleteMealItemById(Long id);
    boolean existsById(Long id);
}
