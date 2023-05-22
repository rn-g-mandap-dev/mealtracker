package com.example.meal2.mealitem;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;


import java.util.List;

@Repository
public interface MealItemRepository extends JpaRepository<MealItem, Long> {

    @Query("SELECT mi FROM MealItem mi WHERE mi.meal LIKE %?1% OR mi.note LIKE %?1%")
    List<MealItem> getAllMealItems(String search, Pageable pageable);

}
