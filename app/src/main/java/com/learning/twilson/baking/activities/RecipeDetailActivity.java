package com.learning.twilson.baking.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.google.gson.Gson;
import com.learning.twilson.baking.R;
import com.learning.twilson.baking.models.Ingredient;
import com.learning.twilson.baking.models.Recipe;

import org.w3c.dom.Text;

import java.util.List;

public class RecipeDetailActivity extends AppCompatActivity {
    private Recipe mRecipe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe_detail);
        Intent parentIntent = getIntent();
        Bundle extras = parentIntent.getExtras();
        if (extras.containsKey("RecipeJSON")){
            String recipeJson = extras.getString("RecipeJSON");
            Gson gson = new Gson();
            mRecipe = gson.fromJson(recipeJson, Recipe.class);
        }
        buildRecipeTitle();
        buildIngredientsList(extras);
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
            String row = String.format("• %s %s %s",
                    ingredients.get(i).getQuantity().toString(),
                    ingredients.get(i).getMeasure().toString(),
                    ingredients.get(i).getIngredient());
            sb.append(row);
            if (i < ingredients.size()-1){
                sb.append(System.getProperty("line.separator"));
            }
        }
        ingredientsTV.setText(sb.toString());
    }
}
