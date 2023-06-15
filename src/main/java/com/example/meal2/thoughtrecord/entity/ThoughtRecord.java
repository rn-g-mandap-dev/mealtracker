package com.example.meal2.thoughtrecord.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="thought_record_v0")
public class ThoughtRecord {

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
    @Column(name="tr_date", columnDefinition="DATE", nullable=false)
    private LocalDate date;

    @Schema(type="string", pattern="hh:mm:ss", format="time", example="12:00:00")
    @NotNull(message="time => must not be blank (hh:mm:ss)")
    @DateTimeFormat(pattern="hh:mm:ss")
    @Column(name="tr_time", columnDefinition="TIME", nullable=false)
    private LocalTime time;

    @Schema(example="situation description")
    @NotBlank(message="situation => must not be blank")
    @Size(max=512, message="situation => must not exceed 512 characters")
    @Column(name="situation_desc", nullable=false)
    private String situation;

    @OneToMany(orphanRemoval=true, cascade=CascadeType.ALL)
    @OrderBy("level DESC, mood ASC")
    @JoinColumn(name = "thought_record_id")
    private List<Mood> moods = new ArrayList<>();

    @OneToMany(orphanRemoval=true, cascade=CascadeType.ALL)
    @OrderBy("level DESC, thought ASC")
    @JoinColumn(name = "thought_record_id")
    private List<Thought> thoughts = new ArrayList<>();

    public ThoughtRecord() {
    }

    public ThoughtRecord(LocalDate date, LocalTime time, String situation) {
        this.date = date;
        this.time = time;
        this.situation = situation;
    }

    public ThoughtRecord(LocalDate date, LocalTime time, String situation, List<@Valid Mood> moods, List<@Valid Thought> thoughts) {
        this.date = date;
        this.time = time;
        this.situation = situation;
        this.moods = moods;
        this.thoughts = thoughts;
    }



    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public List<Mood> getMoods() {
        return moods;
    }

    public void setMoods(List<Mood> moods) {
        this.moods.clear();
        this.moods.addAll(moods);
        //this.moods = moods;
    }

    public List<Thought> getThoughts() {
        return thoughts;
    }

    public void setThoughts(List<Thought> thoughts) {
        this.thoughts.clear();
        this.thoughts.addAll(thoughts);
        //this.thoughts = thoughts;
    }

    public ThoughtRecord(Integer userId, LocalDate date, LocalTime time, String situation, List<@Valid Mood> moods, List<@Valid Thought> thoughts) {
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.situation = situation;
        this.moods = moods;
        this.thoughts = thoughts;
    }

    public ThoughtRecord(Integer userId, LocalDate date, LocalTime time, String situation) {
        this.userId = userId;
        this.date = date;
        this.time = time;
        this.situation = situation;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getSituation() {
        return situation;
    }

    public void setSituation(String situation) {
        this.situation = situation;
    }
}
