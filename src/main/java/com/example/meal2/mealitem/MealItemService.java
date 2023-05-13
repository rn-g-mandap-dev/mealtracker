package com.example.meal2.mealitem;

import java.util.List;
import java.util.Optional;

public interface MealItemService {
    List<MealItem> getAllMealItems();
    void saveMealItem(MealItem mealItem);
    void updateMealItem(MealItem mealItem);
    Optional<MealItem> getMealItemById(Long id);
    void deleteMealItemById(Long id);
}
