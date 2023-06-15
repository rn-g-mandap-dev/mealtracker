package com.example.meal2.thoughtrecord.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;

@Entity
@Table(name="thought_v0")
public class Thought {

    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;

    @Schema(example="thought description")
    @NotBlank(message="thought => must not be blank")
    @Size(max=512, message="thought => must not exceed 512 characters")
    @Column(name="thought_desc", nullable=false)
    private String thought;

    @NotNull(message="level => must not be blank")
    @Min(value=1, message="level => must be between 1 and 100")
    @Max(value=100, message="level => must be between 1 and 100")
    @Column(name="level", nullable=false)
    private Integer level;

    public Thought() {
    }

    public Thought(String thought, Integer level) {
        this.thought = thought;
        this.level = level;
    }

    public Thought(Long id, String thought, Integer level) {
        this.id = id;
        this.thought = thought;
        this.level = level;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getThought() {
        return thought;
    }

    public void setThought(String thought) {
        this.thought = thought;
    }

    public Integer getLevel() {
        return level;
    }

    public void setLevel(Integer level) {
        this.level = level;
    }

}
