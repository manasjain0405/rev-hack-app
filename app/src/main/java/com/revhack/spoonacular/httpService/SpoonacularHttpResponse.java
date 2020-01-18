package com.revhack.spoonacular.httpService;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.example.manas.tnp.tpo.MainActivity;
import com.revhack.spoonacular.dtos.RecipeSearchResponseDto;
import com.revhack.spoonacular.dtos.SearchResponse;

import org.json.JSONArray;

public class SpoonacularHttpResponse {

    public SearchResponse getSearchResponse(String query, int i) {
        return null;
    }

    public RecipeSearchResponseDto getRecipeSearchResponse(int id) {
        // get method for receipe
        // this method provides equiments
        String URL = "https://api.spoonacular.com/recipes/"+String.valueOf(id)+"/analyzedInstructions?apiKey=8917e952b5074395856aea4402376c8a";
        JsonArrayRequest objectRequest = new JsonArrayRequest(
                Request.Method.GET,
                URL,
                null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.e("Receipe details",response.toString());
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e("Error", error.toString());
                    }
                }
        );
        //requestQueue.add(objectRequest);
        return null;
    }
}
