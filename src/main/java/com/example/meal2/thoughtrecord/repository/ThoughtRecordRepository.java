package com.example.meal2.thoughtrecord.repository;

import com.example.meal2.aftermealnote.AfterMealNote;
import com.example.meal2.thoughtrecord.entity.Thought;
import com.example.meal2.thoughtrecord.entity.ThoughtRecord;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Repository
public interface ThoughtRecordRepository extends JpaRepository<ThoughtRecord, Long> {

    long countByUserId(Integer userId);

    @Query(value=
            """
                SELECT trv0.*
                FROM thought_record_v0 AS trv0
                WHERE
                    (trv0.situation_desc LIKE CONCAT('%', :s, '%')) AND
                    (:sd <= trv0.tr_date AND :ed >= trv0.tr_date) AND 
                    (:st <= trv0.tr_time AND :et >= trv0.tr_time) AND 
                    (:uid = trv0.user_id)
                ORDER BY trv0.tr_date asc, trv0.tr_time asc
            """, nativeQuery=true)
    List<ThoughtRecord> getAllThoughtRecords(
            @Param("uid") Integer userId,
            @Param("s") String search,
            @Param("sd") LocalDate startDate,
            @Param("ed") LocalDate endDate,
            @Param("st") LocalTime startTime,
            @Param("et") LocalTime endTime,
            Pageable pageable
    );

}
