package com.example.meal2.mealitem;


import com.example.meal2.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface MealItemService {
    List<MealItem> getAllMealItems(
            Integer userId,
            String search,
            Integer page,
            Integer size,
            MealItem.MealSize mealSize,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startTime,
            LocalTime endTime);
    void saveMealItem(MealItem mealItem);
    void updateMealItem(MealItem mealItem);
    Optional<MealItem> getMealItemById(Long id);
    void deleteMealItemById(@AuthenticationPrincipal User user, Long id);
    boolean existsById(Long id);
}
