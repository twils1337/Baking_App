package com.learning.twilson.baking.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.gson.Gson;
import com.learning.twilson.baking.R;
import com.learning.twilson.baking.adapters.StepsAdapter;
import com.learning.twilson.baking.interfaces.StepsAdapterOnClickHandler;
import com.learning.twilson.baking.models.Ingredient;
import com.learning.twilson.baking.models.Recipe;
import com.learning.twilson.baking.models.Step;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity
                                  implements StepsAdapterOnClickHandler {
    private Recipe mRecipe;
    private RecyclerView mStepsRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Intent parentIntent = getIntent();
        Bundle extras = parentIntent.getExtras();
        if (mRecipe == null){
            if (extras.containsKey("RecipeJSON")){
                String recipeJson = extras.getString("RecipeJSON");
                Gson gson = new Gson();
                mRecipe = gson.fromJson(recipeJson, Recipe.class);
            }
        }
        buildRecipeTitle();
        buildIngredientsList(extras);
        buildStepsList();
    }

    private void buildRecipeTitle(){
        TextView recipeTitle = findViewById(R.id.tvRecipeTitle);
        recipeTitle.setText(mRecipe.getName());
    }

    private void buildIngredientsList(Bundle extras){

        populateIngredientsList(mRecipe.getIngredients());
    }

    private void populateIngredientsList(List<Ingredient> ingredients) {
        TextView ingredientsTV = findViewById(R.id.tvIngredientsList);
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < ingredients.size(); ++i){
            String row = getFormattedStringForIngredientList(ingredients.get(i));
            sb.append(row);
            if (i < ingredients.size()-1){
                sb.append(System.getProperty("line.separator"));
            }
        }
        ingredientsTV.setText(sb.toString());
    }

    private String getFormattedStringForIngredientList(Ingredient ingredient){
        return String.format("• %s %s %s",
                             ingredient.getQuantity().toString(),
                             ingredient.getMeasure().toString(),
                             ingredient.getIngredient());
    }

    private void buildStepsList(){
        mStepsRV = findViewById(R.id.rvSteps);
        LinearLayoutManager llManager = new LinearLayoutManager(this);
        mStepsRV.setLayoutManager(llManager);
        mStepsRV.setAdapter(new StepsAdapter(mRecipe.getSteps(), this));
    }

    @Override
    public void onClick(int stepClickedIndex) {
        Intent stepDetailIntent = new Intent(this, StepDetailActivity.class);
        Step step = mRecipe.getSteps().get(stepClickedIndex);
        Gson gson = new Gson();
        String stepJson = gson.toJson(step);
        stepDetailIntent.putExtra("StepJSON", stepJson);
        startActivity(stepDetailIntent);
    }
}
