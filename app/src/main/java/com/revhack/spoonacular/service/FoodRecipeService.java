package com.revhack.spoonacular.service;

import com.revhack.spoonacular.dtos.EquipmentsDto;
import com.revhack.spoonacular.dtos.FinalResponse;
import com.revhack.spoonacular.dtos.IngredientsDto;
import com.revhack.spoonacular.dtos.Instruction;
import com.revhack.spoonacular.dtos.RecipeSearchResponseDto;
import com.revhack.spoonacular.dtos.ResultsDto;
import com.revhack.spoonacular.dtos.SearchResponse;
import com.revhack.spoonacular.dtos.StepsDto;
import com.revhack.spoonacular.httpService.SpoonacularHttpResponse;

import java.util.ArrayList;
import java.util.List;

public class FoodRecipeService {

    SpoonacularHttpResponse spoonacularHttpResponse = new SpoonacularHttpResponse();

    public FinalResponse getQueryResponse(String query) {

       return getSearchResponse(query, 1);

    }

    private FinalResponse getSearchResponse(String query, int number) {

        FinalResponse finalResponse = new FinalResponse();

        SearchResponse searchResponse = spoonacularHttpResponse.getSearchResponse(query, 1);


        RecipeSearchResponseDto recipeSearchResponseDto = spoonacularHttpResponse
                .getRecipeSearchResponse(searchResponse.getResults().get(0).getId());

        List<ResultsDto> resultsList = searchResponse.getResults();

        finalResponse.setName(query);
        finalResponse.setReadyInMinutes(searchResponse.getResults().get(0).getReadyInMinutes());
        finalResponse.setInstructions(getInstructions(recipeSearchResponseDto));

        return finalResponse;
    }

    private List<Instruction> getInstructions(RecipeSearchResponseDto recipeSearchResponseDto) {
        List<Instruction> instructionList = new ArrayList<>();
        for (StepsDto step: recipeSearchResponseDto.getSteps()) {
            Instruction instruction = new Instruction();
            instruction.setStep(step.getStep());
            instruction.setLength(step.getLength().getNumber()  + " " + step.getLength().getUnit());
            instruction.setIngredients(getIngredients(step.getIngredients()));
            instruction.setEquipments(getEuipments(step.getEquipment()));
            instructionList.add(instruction);
        }
        return instructionList;
    }

    private String getIngredients(List<IngredientsDto> ingredients) {
        String response = "";
        for (IngredientsDto i: ingredients) {
            response.concat(i.getName()+ ", ");
        }
        return response.substring(0, response.length()-2);
    }

    private String getEuipments(List<EquipmentsDto> equipments) {
        String response = "";
        for(EquipmentsDto e: equipments) {
            response.concat(e.getName()+ ", ");
        }
        return response.substring(0, response.length()-2);
    }

}
