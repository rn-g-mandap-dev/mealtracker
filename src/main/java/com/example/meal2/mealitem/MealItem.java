package com.example.meal2.mealitem;

import com.example.meal2.aftermealnote.AfterMealNote;
import com.fasterxml.jackson.annotation.*;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.hibernate.annotations.SQLInsert;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="meal_item")
public class MealItem {

    public enum MealSize{
        light, medium, heavy
    }

    @Id
    @GeneratedValue(strategy=GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotNull(message="user => must not be null")
    @JsonProperty("user")
    @Column(name="user_id")
    private Integer userId;

    @Schema(example="meal description")
    @NotBlank(message="meal => must not be blank")
    @Size(max=512, message="meal => must not exceed 512 characters")
    @Column(name="meal", nullable=false)
    private String meal;

    @NotNull(message="date => must not be blank (yyyy-mm-dd)")
    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    @Column(name="meal_date", columnDefinition="DATE", nullable=false)
    private LocalDate date;

    @Schema(type="string", pattern="hh:mm:ss", format="time", example="12:00:00")
    @NotNull(message="time => must not be blank (hh:mm:ss)")
    @DateTimeFormat(pattern="hh:mm:ss")
    @Column(name="meal_time", columnDefinition="TIME", nullable=false)
    private LocalTime time;

    @NotNull(message="size => must not be blank (light, medium, heavy)")
    @Enumerated(EnumType.STRING)
    @JsonProperty("size")
    @Column(name="meal_size", columnDefinition="ENUM('light','medium','heavy')", nullable=false)
    private MealSize mealSize;

    @Schema(example="meal note")
    @Size(max=255, message="note => must not exceed 255 characters")
    @Column(name="note", nullable=true)
    private String note;

    @OneToMany(mappedBy="mealItemId", orphanRemoval=true)
    @JsonIgnoreProperties("mealItem")
    private Set<AfterMealNote> afterMealNotes;

    public Set<AfterMealNote> getAfterMealNotes() {
        return afterMealNotes;
    }

    public void setAfterMealNotes(Set<AfterMealNote> afterMealNotes) {
        this.afterMealNotes = afterMealNotes;
    }

    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getMeal() {
        return meal;
    }
    public void setMeal(String meal) {
        this.meal = meal;
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

    public MealSize getMealSize() {
        return mealSize;
    }
    public void setMealSize(MealSize mealSize) {
        this.mealSize = mealSize;
    }

    public String getNote() {
        return note;
    }
    public void setNote(String note) {
        this.note = note;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Override
    public String toString() {
        return "MealItem{" +
                "id=" + id +
                ", userId=" + userId +
                ", meal='" + meal + '\'' +
                ", date=" + date +
                ", time=" + time +
                ", mealSize=" + mealSize +
                ", note='" + note + '\'' +
                ", afterMealNotes=" + afterMealNotes +
                '}';
    }

}
