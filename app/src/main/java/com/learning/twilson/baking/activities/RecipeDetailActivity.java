package com.learning.twilson.baking.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.learning.twilson.baking.R;
import com.learning.twilson.baking.models.Recipe;
import com.learning.twilson.baking.ui.RecipeFragment;


public class RecipeDetailActivity extends AppCompatActivity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        if (savedInstanceState == null){
            RecipeFragment recipeFragment = getRecipeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.flRecipe, recipeFragment)
                    .commit();
        }
    }

    private RecipeFragment getRecipeFragment(){
        RecipeFragment recipeFragment = new RecipeFragment();
        Recipe recipe = null;
        Intent parentIntent = getIntent();
        Bundle extras = parentIntent.getExtras();
        if (extras.containsKey("RecipeJSON")){
            String recipeJson = extras.getString("RecipeJSON");
            recipe = new Gson().fromJson(recipeJson, Recipe.class);
        }
        recipeFragment.setRecipe(recipe);
        return recipeFragment;
    }
}
