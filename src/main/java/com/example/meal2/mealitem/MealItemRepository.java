package com.example.meal2.mealitem;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface MealItemRepository extends JpaRepository<MealItem, Long> {

    @Query(value=
    """
        SELECT mi.*
        FROM meal_item AS mi
        WHERE
            (mi.meal LIKE CONCAT('%', :s, '%') OR mi.note LIKE CONCAT('%', :s, '%')) AND
            (:#{#ms?.name()} IS NULL OR :#{#ms?.name()} = mi.meal_size) AND
            (:sd <= mi.meal_date AND :ed >= mi.meal_date) AND 
            (:st <= mi.meal_time AND :et >= mi.meal_time) AND 
            (:uid = mi.user_id)
        ORDER BY mi.meal_date asc, mi.meal_time asc
    """, nativeQuery=true)
    List<MealItem> getAllMealItems(
            @Param("uid") Integer userId,
            @Param("s") String search,
            @Param("ms") MealItem.MealSize mealSize,
            @Param("sd") LocalDate startDate,
            @Param("ed") LocalDate endDate,
            @Param("st") LocalTime startTime,
            @Param("et") LocalTime endTime,
            Pageable pageable
    );

    @Query(value=
            """
                SELECT COUNT(1)
                FROM meal_item AS mi
                WHERE
                    (mi.meal LIKE CONCAT('%', :s, '%') OR mi.note LIKE CONCAT('%', :s, '%')) AND
                    (:#{#ms?.name()} IS NULL OR :#{#ms?.name()} = mi.meal_size) AND
                    (:sd <= mi.meal_date AND :ed >= mi.meal_date) AND 
                    (:st <= mi.meal_time AND :et >= mi.meal_time) AND 
                    (:uid = mi.user_id)
                ORDER BY mi.meal_date asc, mi.meal_time asc
            """, nativeQuery=true)
    Long getAllMealItemsCount(
            @Param("uid") Integer userId,
            @Param("s") String search,
            @Param("ms") MealItem.MealSize mealSize,
            @Param("sd") LocalDate startDate,
            @Param("ed") LocalDate endDate,
            @Param("st") LocalTime startTime,
            @Param("et") LocalTime endTime
    );

    // not yet tested, should to test all queries in all repositories in the future for detailed testing...
    @Query(value=
        """
            SELECT COUNT(*)
            FROM meal_item AS mi
            WHERE
                (:uid = mi.user_id)
        """, nativeQuery=true)
    Integer countUserMealItems(
            @Param("uid") Integer userId
    );

}
