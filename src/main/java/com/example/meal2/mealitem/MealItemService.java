package com.example.meal2.mealitem;


import com.example.meal2.mealitem.dto.MealItemCreationDTO;
import com.example.meal2.mealitem.dto.MealItemDetailedDTO;
import com.example.meal2.mealitem.dto.MealItemUpdateDTO;
import com.example.meal2.user.User;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.annotation.AuthenticationPrincipal;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface MealItemService {
    List<MealItemDetailedDTO> getAllMealItems(
            User user,
            String search,
            Integer page,
            Integer size,
            MealItem.MealSize mealSize,
            LocalDate startDate,
            LocalDate endDate,
            LocalTime startTime,
            LocalTime endTime);
    Long createMealItem(User user, MealItemCreationDTO mealItemCreationDTO);
    void saveMealItem(MealItem mealItem);
    void updateMealItem(User user, Long mealItemId, MealItemUpdateDTO mealItemUpdateDTO);
    Optional<MealItem> getMealItemById(Long id);
    MealItemDetailedDTO getMealItem(User user, Long id);
    void deleteMealItemById(User user, Long id);
    boolean existsById(Long id);

}
