package com.example.meal2.bowel.constants;

import lombok.Getter;

@Getter
public enum StoolColor {
    BROWN("Brown", "Normal"),
    GREY("Grey", "Lack of bile, pancreatic and liver diseases"),
    YELLOW("Yellow", "Fat in stool, malabsorption"),
    GREEN("Green", "Excess bile, change in diet, diarrhea"),
    RED("Red", "Bleeding in lower GI tract"),
    BLACK("Black", "Bleeding in upper GI tract"),
    NA("N/A", "N/A");

    private final String desc;
    private final String causes;

    StoolColor(String desc, String causes){
        this.desc = desc;
        this.causes = causes;
    }

}
