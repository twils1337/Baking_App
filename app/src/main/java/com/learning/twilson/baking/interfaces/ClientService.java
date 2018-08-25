package com.learning.twilson.baking.interfaces;

import com.learning.twilson.baking.models.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ClientService {
    @GET("topher/2017/May/59121517_baking/baking.json")
    Call<List<Recipe>> getRecipes();
}
