package com.example.manas.tnp.tpo.dtos;

import java.util.List;

public class RecipeSearchResponseDto {

    private String name;

    private List<StepsDto> steps;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<StepsDto> getSteps() {
        return steps;
    }

    public void setSteps(List<StepsDto> steps) {
        this.steps = steps;
    }
}
