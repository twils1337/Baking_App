package com.learning.twilson.baking.activities;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learning.twilson.baking.R;
import com.learning.twilson.baking.models.Recipe;
import com.learning.twilson.baking.ui.RecipeFragment;

import java.util.List;


public class RecipeDetailActivity extends AppCompatActivity{
    private RecipeFragment mRecipeFragment;
    public static final String EXTRA_RECIPE_ID = "com.learning.twilson.baking.RECIPE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        if (savedInstanceState == null){
            mRecipeFragment = getRecipeFragment();
            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.flRecipe, mRecipeFragment)
                    .commit();
        }
    }

    private RecipeFragment getRecipeFragment(){
        RecipeFragment recipeFragment = new RecipeFragment();
        List<Recipe> recipes = null;
        Intent parentIntent = getIntent();
        Bundle extras = parentIntent.getExtras();
        if (extras.containsKey("RecipesJSON")){
            String recipeJson = extras.getString("RecipesJSON");
            recipes = new Gson().fromJson(recipeJson, new TypeToken<List<Recipe>>(){}.getType());
        }
        int recipePos = extras.getInt(EXTRA_RECIPE_ID, 0);
        recipeFragment.setRecipes(recipes);
        recipeFragment.setCurrentRecipePos(recipePos);
        return recipeFragment;
    }
}
