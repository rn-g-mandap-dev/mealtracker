package com.example.meal2.bowel.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Table(name="bowel_v0")
public class Bowel {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @NotNull(message="user => must not be null")
    @JsonProperty("user")
    @Column(name="user_id")
    private Integer userId;
    @NotNull(message="date => must not be blank (yyyy-mm-dd)")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name="b_date", columnDefinition="DATE", nullable=false)
    private LocalDate date;
    @Schema(type="string", pattern="hh:mm:ss", format="time", example="12:00:00")
    @NotNull(message="time => must not be blank (hh:mm:ss)")
    @DateTimeFormat(pattern="hh:mm:ss")
    @Column(name="b_time", columnDefinition="TIME", nullable=false)
    private LocalTime time;


    /*
    id
    userid
    date
    time

    bristolStoolChart
    color
    stoolsize

    isUrgent
    isBloated
    isFlatulent
    isPainful

    hasMucus (n/a true false)
    hasBlood (n/a true false)
    isLongWipe (n/a true false)
    hasFloatingStool (n/a true false)
    hasSoiledUnderwear (n/a true false)

    note *optional

    in future
    ???
    createdby
    createddatetime
    modifiedby
    modifieddatetime
     */

}
