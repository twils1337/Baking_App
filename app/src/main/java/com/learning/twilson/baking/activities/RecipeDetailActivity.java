package com.learning.twilson.baking.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learning.twilson.baking.R;
import com.learning.twilson.baking.interfaces.ClientService;
import com.learning.twilson.baking.models.Recipe;
import com.learning.twilson.baking.ui.RecipeFragment;
import com.learning.twilson.baking.utils.ServiceGenerator;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class RecipeDetailActivity extends AppCompatActivity{
    private RecipeFragment mRecipeFragment;
    public static final String EXTRA_RECIPE_ID = "com.learning.twilson.baking.RECIPE_ID";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        if (savedInstanceState == null){
            Bundle extras = getIntent().getExtras();
            if (extras.containsKey("RecipesJSON")){
                mRecipeFragment = getRecipeFragment(extras);
                AddFragmentToLayout();
            }
            else{
                loadRecipes();
            }
        }
        else{
            if (savedInstanceState.containsKey("RecipesJSON")){
                mRecipeFragment = getRecipeFragment(savedInstanceState);
                AddFragmentToLayout();
            }
        }
    }

    private void AddFragmentToLayout() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .add(R.id.flRecipe, mRecipeFragment)
                .commit();
    }

    private RecipeFragment getRecipeFragmentFromInternet(List<Recipe> recipes, Bundle extras){
        RecipeFragment recipeFragment = new RecipeFragment();
        int recipePos = extras.getInt(EXTRA_RECIPE_ID, 0)-1;
        recipeFragment.setRecipes(recipes);
        recipeFragment.setCurrentRecipePos(recipePos);
        return recipeFragment;
    }

    private RecipeFragment getRecipeFragment(Bundle bundle){
        RecipeFragment recipeFragment = new RecipeFragment();
        List<Recipe> recipes = null;
        if (bundle.containsKey("RecipesJSON")){
            String recipeJson = bundle.getString("RecipesJSON");
            recipes = new Gson().fromJson(recipeJson, new TypeToken<List<Recipe>>(){}.getType());
        }
        int recipePos = bundle.getInt(EXTRA_RECIPE_ID, 0)-1;
        recipeFragment.setRecipes(recipes);
        recipeFragment.setCurrentRecipePos(recipePos);
        return recipeFragment;
    }

    public void onNavClick(View v){
        final View clickedButton = v;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String buttonClicked = clickedButton.getId() == R.id.btnNext ? "Next" : "Prev";
                List<Recipe> recipes = mRecipeFragment.getRecipes();
                int currentRecipePos = mRecipeFragment.getCurrentRecipePos();
                if ( (buttonClicked.equals("Next") && currentRecipePos+1 >= recipes.size()) ||
                     (buttonClicked.equals("Prev") && currentRecipePos-1 < 0) ){
                    return;
                }

                Intent nextNavigatedStepIntent = new Intent(RecipeDetailActivity.this, RecipeDetailActivity.class);
                String recipeGson = new Gson().toJson(recipes);
                int nextStepPos = buttonClicked.equals("Next") ? currentRecipePos+2: currentRecipePos;
                nextNavigatedStepIntent.putExtra("RecipesJSON", recipeGson);
                nextNavigatedStepIntent.putExtra(EXTRA_RECIPE_ID, nextStepPos);
                finish();
                startActivity(nextNavigatedStepIntent);

            }
        }, 0);
    }

    public void loadRecipes(){
        ClientService client = ServiceGenerator.createService(ClientService.class);
        Call<List<Recipe>> call = client.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                Bundle extras = getIntent().getExtras();
                mRecipeFragment = getRecipeFragmentFromInternet(response.body(), extras);
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.flRecipe, mRecipeFragment)
                        .commit();
            }
            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e("Loading Recipes", "onFailure: "+t.getMessage() );
            }
        });
    }
}
