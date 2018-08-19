package com.learning.twilson.baking.activities;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learning.twilson.baking.R;
import com.learning.twilson.baking.ui.RecipesFragment;
import com.learning.twilson.baking.models.Recipe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            RecipesFragment recipesFragment = getRecipesFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.flRecipes, recipesFragment)
                    .commit();
        }
    }

    private RecipesFragment getRecipesFragment(){
        RecipesFragment recipesFragment = new RecipesFragment();
        recipesFragment.setRecipes(loadRecipes(this));
        return recipesFragment;
    }

    private List<Recipe> loadRecipes(Context context){
        List<Recipe> loadedRecipes = null;
        try {
            String recipeJson = readRecipeFile(context);
            if(recipeJson.equals("")){
                throw new Exception("Json is malformed and/or could not be read.");
            }
            Gson gson = new Gson();
            loadedRecipes = gson.fromJson(recipeJson, new TypeToken<List<Recipe>>(){}.getType());
            return loadedRecipes;
        }
        catch (Exception e){
            Log.e("Error", "loadRecipes: " + e.getMessage());
        }
        return loadedRecipes;
    }

    private String readRecipeFile(Context context) throws IOException {
        try(InputStream iStream = context.getResources().openRawResource(R.raw.recipes)){
            if (iStream != null){
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(iStream));
                String readStr;
                StringBuilder sb = new StringBuilder();
                while( (readStr = bufferedReader.readLine()) != null ){
                    sb.append(readStr);
                }
                return sb.toString();
            }
            else{
                return "";
            }
        }

    }
}
