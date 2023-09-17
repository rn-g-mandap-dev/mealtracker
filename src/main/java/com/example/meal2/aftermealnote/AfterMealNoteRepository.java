package com.example.meal2.aftermealnote;

import com.example.meal2.mealitem.MealItem;
import org.aspectj.lang.annotation.After;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface AfterMealNoteRepository extends JpaRepository<AfterMealNote, Long> {

    /*
    select a.*
    from after_meal_note a
    join meal_item b
    on a.meal_item_id = b.id
    where b.user_id = 3;
     */

    @Query(value=
    """
        SELECT amn.*
        FROM after_meal_note AS amn
        JOIN meal_item AS mi
        ON amn.meal_item_id = mi.id
        WHERE
            (amn.note LIKE CONCAT('%', :s, '%')) AND
            (:sd <= amn.note_date AND :ed >= amn.note_date) AND 
            (:st <= amn.note_time AND :et >= amn.note_time) AND 
            (:uid = mi.user_id)
        ORDER BY amn.note_date asc, amn.note_time asc
    """, nativeQuery=true)
    List<AfterMealNote> getAllAfterMealNotes(
            @Param("uid") Integer userId,
            @Param("s") String search,
            @Param("sd") LocalDate startDate,
            @Param("ed") LocalDate endDate,
            @Param("st") LocalTime startTime,
            @Param("et") LocalTime endTime,
            Pageable pageable
    );

    @Query(value=
            """
                SELECT COUNT(*)
                FROM after_meal_note AS amn
                WHERE
                    (:mid = amn.meal_item_id)
            """, nativeQuery=true)
    Integer countMealItemAfterMealNotes(
            @Param("mid") Long mealItemId
    );

}
