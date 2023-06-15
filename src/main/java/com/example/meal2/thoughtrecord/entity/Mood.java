package com.example.meal2.thoughtrecord.entity;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

@Entity
@Table(name="mood_v0")
public class Mood {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @NotNull(message="moodType => must not be blank")
    @Enumerated(EnumType.STRING)
    @JsonProperty("moodType")
    @Column(
            name="mood_type",
            columnDefinition="ENUM(" +
                    "'AFRAID'" +
                    ",'ANGRY'" +
                    ",'ANXIOUS'" +
                    ",'ASHAMED'" +
                    ",'CHEERFUL'" +
                    ",'CONTENT'" +
                    ",'DEPRESSED'" +
                    ",'DISAPPOINTED'" +
                    ",'DISGUSTED'" +
                    ",'GRATEFUL'" +
                    ",'EAGER'" +
                    ",'EMBARRASSED'" +
                    ",'ENRAGED'" +
                    ",'EXCITED'" +
                    ",'FRIGHTENED'" +
                    ",'FRUSTRATED'" +
                    ",'GRIEF'" +
                    ",'GUILTY'" +
                    ",'HAPPY'" +
                    ",'HUMILIATED'" +
                    ",'HURT'" +
                    ",'INSECURE'" +
                    ",'IRRITATED'" +
                    ",'LOVING'" +
                    ",'MAD'" +
                    ",'NERVOUS'" +
                    ",'PANIC'" +
                    ",'PROUD'" +
                    ",'SAD'" +
                    ",'SCARED')"
            , nullable=false
    )
    private MoodType mood;

    @NotNull(message="level => must not be blank")
    @Min(value=1, message="level => must be between 1 and 100")
    @Max(value=100, message="level => must be between 1 and 100")
    @Column(name="level", nullable=false)
    private Integer level;

    public Mood() {
    }

    public Mood(MoodType mood, Integer level) {
        this.mood = mood;
        this.level = level;
    }

    public Mood(Long id, MoodType mood, Integer level) {
        this.id = id;
        this.mood = mood;
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public MoodType getMood() {
        return mood;
    }

    public void setMood(MoodType mood) {
        this.mood = mood;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

}
