package com.example.meal2.mealitem;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MealItemRepository extends JpaRepository<MealItem, Long> {
}
