package com.revhack.spoonacular.dtos;

import java.util.List;

public class StepsDto {

    private int number;

    private String step;

    private List<IngredientsDto> ingredients;

    private List<EquipmentsDto> equipment;

    private LengthDto length;

    public int getNumber() {
        return number;
    }

    public void setNumber(int number) {
        this.number = number;
    }

    public String getStep() {
        return step;
    }

    public void setStep(String step) {
        this.step = step;
    }

    public List<IngredientsDto> getIngredients() {
        return ingredients;
    }

    public void setIngredients(List<IngredientsDto> ingredients) {
        this.ingredients = ingredients;
    }

    public List<EquipmentsDto> getEquipment() {
        return equipment;
    }

    public void setEquipment(List<EquipmentsDto> equipment) {
        this.equipment = equipment;
    }

    public LengthDto getLength() {
        return length;
    }

    public void setLength(LengthDto length) {
        this.length = length;
    }
}
