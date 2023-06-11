package com.example.meal2.aftermealnote;

import com.example.meal2.mealitem.MealItem;
import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;

@Entity
@Table(name="after_meal_note")
public class AfterMealNote {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotNull(message="mealId => must not be null")
    @JsonProperty("mealId")
    @Column(name="meal_item_id", nullable=false)
    private Long mealItemId;

    @NotNull(message="date => must not be blank (yyyy-mm-dd)")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
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

    @ManyToOne
    @JsonIgnoreProperties("afterMealNotes")
    @JoinColumn(name="meal_item_id", insertable=false, updatable=false, referencedColumnName = "id")
    private MealItem mealItem;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public MealItem getMealItem() {
        return mealItem;
    }

    public void setMealItem(MealItem mealItem) {
        this.mealItem = mealItem;
    }

    /*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AfterMealNote that)) return false;
        return Objects.equals(id, that.id) && Objects.equals(mealItemId, that.mealItemId) && Objects.equals(date, that.date) && Objects.equals(time, that.time) && Objects.equals(note, that.note) && Objects.equals(mealItem, that.mealItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mealItemId, date, time, note, mealItem);
    }
    */
    /*
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        AfterMealNote that = (AfterMealNote) o;
        return Objects.equals(id, that.id) && Objects.equals(mealItemId, that.mealItemId) && Objects.equals(date, that.date) && Objects.equals(time, that.time) && Objects.equals(note, that.note) && Objects.equals(mealItem, that.mealItem);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, mealItemId, date, time, note, mealItem);
    }

     */
}
