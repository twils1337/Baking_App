package com.learning.twilson.baking.activities;

import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learning.twilson.baking.R;
import com.learning.twilson.baking.interfaces.ClientService;
import com.learning.twilson.baking.interfaces.RecipeAdapterOnClickHandler;
import com.learning.twilson.baking.ui.RecipeFragment;
import com.learning.twilson.baking.ui.RecipesFragment;
import com.learning.twilson.baking.models.Recipe;
import com.learning.twilson.baking.utils.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity
                          implements RecipeAdapterOnClickHandler{

    private RecipesFragment mRecipesFragment = new RecipesFragment();
    private RecipeFragment mRecipeFragment;
    private boolean mTwoPane;


    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clearFrags(getSupportFragmentManager());
        setContentView(R.layout.activity_main);
        mTwoPane = findViewById(R.id.llDoublePane) != null;
        if (savedInstanceState == null) {
            mRecipesFragment = getRecipesFragment();
            loadRecipes();
        }
        else{
            mRecipesFragment = getRecipesFragment();
            List<Recipe> recipes = new Gson().fromJson(savedInstanceState.getString("RecipesJSON"),
                    new TypeToken<List<Recipe>>(){}.getType());
            mRecipesFragment.setRecipes(recipes);
            buildFragments();
        }
    }

    private void buildFragments() {
        FragmentManager manager = getSupportFragmentManager();
        mRecipesFragment.setTwoPane(mTwoPane);
        if (mTwoPane){
            mRecipeFragment = new RecipeFragment();
            mRecipeFragment.setRecipes(mRecipesFragment.getRecipes());
            mRecipeFragment.setCurrentRecipePos(0);
            manager.beginTransaction()
                    .add(R.id.flRecipesTwoPane, mRecipesFragment)
                    .add(R.id.flRecipeTwoPane, mRecipeFragment)
                    .addToBackStack("Double Add")
                    .commit();
        }
        else{
                manager.beginTransaction()
                        .add(R.id.flRecipes, mRecipesFragment)
                        .addToBackStack("Single Add")
                        .commit();
        }
    }

    private RecipesFragment getRecipesFragment(){
        RecipesFragment recipesFragment = new RecipesFragment();
        recipesFragment.setOnClickHandler(this);
        return recipesFragment;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("RecipesJSON", new Gson().toJson(mRecipesFragment.getRecipes()));
    }

    private void loadRecipes(){
        ClientService client = ServiceGenerator.createService(ClientService.class);
        Call<List<Recipe>> call = client.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                mRecipesFragment.setRecipes(response.body());
                buildFragments();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e("Loading Recipes", "onFailure: "+t.getMessage() );
            }
        });
    }

    @Override
    public void onClick(int recipeClickedIndex) {
        FragmentManager manager =  getSupportFragmentManager();
        RecipeFragment recipeFragment = new RecipeFragment();
        recipeFragment.setRecipes(mRecipesFragment.getRecipes());
        recipeFragment.setCurrentRecipePos(recipeClickedIndex);
        manager.beginTransaction()
                .replace(R.id.flRecipeTwoPane, recipeFragment)
                .addToBackStack("Update Double").commit();
        mRecipeFragment = recipeFragment;
    }

    private void clearFrags(FragmentManager manager){
        while(manager.getBackStackEntryCount() > 0){
            manager.popBackStackImmediate();
        }
    }
}
