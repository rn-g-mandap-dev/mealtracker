package com.example.meal2.aftermealnote.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;

public class AfterMealNoteReq {

    @NotNull(message="mealId => must not be null")
    @JsonProperty("mealId")
    @Column(name="meal_item_id", nullable=false)
    private Long mealItemId;

    @NotNull(message="date => must not be blank (dd-MM-yyyy)")
    @DateTimeFormat(pattern="dd-MM-yyyy")
    @Column(name="note_date", columnDefinition="DATE", nullable=false)
    private LocalDate date;

    @Schema(type="string", pattern="hh:mm:ss", format="time", example="12:00:00")
    @NotNull(message="time => must not be blank (hh:mm:ss)")
    @DateTimeFormat(pattern="hh:mm:ss")
    @Column(name="note_time", columnDefinition="TIME", nullable=false)
    private LocalTime time;

    @Size(max=255, message="note => must not exceed 255 characters")
    @Column(name="note", nullable=false)
    private String note;

    public AfterMealNoteReq(Long mealItemId, LocalDate date, LocalTime time, String note) {
        this.mealItemId = mealItemId;
        this.date = date;
        this.time = time;
        this.note = note;
    }

    public Long getMealItemId() {
        return mealItemId;
    }

    public void setMealItemId(Long mealItemId) {
        this.mealItemId = mealItemId;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }
}
