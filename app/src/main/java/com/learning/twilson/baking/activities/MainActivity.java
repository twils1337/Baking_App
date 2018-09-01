package com.learning.twilson.baking.activities;

import android.support.annotation.NonNull;
import android.support.annotation.VisibleForTesting;
import android.support.test.espresso.IdlingResource;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learning.twilson.baking.utils.RecipeRetriever;
import com.learning.twilson.baking.IdlingResource.SimpleIdlingResource;
import com.learning.twilson.baking.R;
import com.learning.twilson.baking.interfaces.RecipeAdapterOnClickHandler;
import com.learning.twilson.baking.ui.RecipeFragment;
import com.learning.twilson.baking.ui.RecipesFragment;
import com.learning.twilson.baking.models.Recipe;

import java.util.List;

public class MainActivity extends AppCompatActivity
                          implements RecipeAdapterOnClickHandler,
                                      RecipeRetriever.DelayerCallback{

    private RecipesFragment mRecipesFragment = new RecipesFragment();
    private RecipeFragment mRecipeFragment;
    private boolean mTwoPane;

    private SimpleIdlingResource mIdlingResource;

    @VisibleForTesting
    @NonNull
    public IdlingResource getIdlingResource() {
        if (mIdlingResource == null){
            mIdlingResource = new SimpleIdlingResource();
        }
        return mIdlingResource;
    }

    @Override
    protected void onStart() {
        super.onStart();
        List<Recipe> recipes = mRecipesFragment.getRecipes();
        if (recipes == null || recipes.isEmpty()){
            RecipeRetriever.fetchRecipes(MainActivity.this, mIdlingResource);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        clearFrags(getSupportFragmentManager());
        setContentView(R.layout.activity_main);
        mTwoPane = findViewById(R.id.llDoublePane) != null;
        if (savedInstanceState != null){
            mRecipesFragment = getRecipesFragment();
            List<Recipe> recipes = new Gson().fromJson(savedInstanceState.getString("RecipesJSON"),
                    new TypeToken<List<Recipe>>(){}.getType());
            mRecipesFragment.setRecipes(recipes);
            buildFragments();
        }
        getIdlingResource();
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

    @Override
    public void onDone(List<Recipe> recipes) {
        if (mRecipesFragment == null){
            mRecipesFragment = getRecipesFragment();
        }
        mRecipesFragment.setRecipes(recipes);
        buildFragments();
    }
}
