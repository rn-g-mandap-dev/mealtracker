package com.example.meal2.bowel.constants;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public enum BristolStoolChart {
    TYPE_1("Constipation", "Separate hard lumps, like nuts (hard to pass)"),
    TYPE_2("Constipation", "Sausage-shaped but lumpy"),
    TYPE_3("Healthy", "Like a sausage but with cracks on its surface"),
    TYPE_4("Healthy", "Like a sausage or snake, smooth and soft"),
    TYPE_5("Diarrhea", "Soft blobs with clear-cut edges (passed easily)"),
    TYPE_6("Diarrhea", "Fluffy pieces with ragged edges, a mushy stool"),
    TYPE_7("Diarrhea", "Watery, no solid pieces, entirely liquid"),
    NA("N/A", "N/A");

    private final String type;
    private final String desc;

    BristolStoolChart(String type, String desc){
        this.type = type;
        this.desc = desc;
    }

}
