package com.example.meal2.mealitem;


import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public interface MealItemService {
    List<MealItem> getAllMealItems(String search, Pageable pageable);
    void saveMealItem(MealItem mealItem);
    void updateMealItem(MealItem mealItem);
    Optional<MealItem> getMealItemById(Long id);
    void deleteMealItemById(Long id);
    boolean existsById(Long id);
}
