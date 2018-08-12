package com.learning.twilson.baking;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learning.twilson.baking.models.Recipe;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity
                          implements RecipeAdapterOnClickHandler {
    private RecipeAdapterOnClickHandler mHandler = this;
    private List<Recipe> recipes = null;
    private RecyclerView mRecipeRV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (recipes == null){
            loadRecipes(this);
        }
        List<String> names = getNamesForCards();
        mRecipeRV = findViewById(R.id.rvRecipeCards);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, calculateNumOfColumns(this));
        mRecipeRV.setLayoutManager(gridLayoutManager);
        mRecipeRV.setAdapter(new RecipeAdapter(names, this));
        mRecipeRV.setHasFixedSize(true);

    }
    public static int calculateNumOfColumns(Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float dpWidth = metrics.widthPixels;
        int scalingFactor = 1000;
        int numOfColumns = (int) (dpWidth/scalingFactor);
        if (numOfColumns < 1){
            numOfColumns = 1;
        }
        return numOfColumns;
    }

    private List<String> getNamesForCards() {
        List<String> names = new ArrayList<>();
        for (Recipe recipe:recipes){
            names.add(recipe.getName());
        }
        return names;
    }

    private void loadRecipes(Context context){
        try {
            String recipeJson = readRecipeFile(context);
            Gson gson = new Gson();
            recipes = gson.fromJson(recipeJson, new TypeToken<List<Recipe>>(){}.getType());
        }
        catch (Exception e){
            Log.e("File Read Error", "loadRecipes: " + e.getMessage());
        }
    }

    private String readRecipeFile(Context context) throws IOException {
        try(InputStream iStream= context.getResources().openRawResource(R.raw.recipes)){
            if (iStream != null){
                InputStreamReader inpReader = new InputStreamReader(iStream);
                BufferedReader bufferedReader = new BufferedReader(inpReader);
                String readStr = "";
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

    @Override
    public void onClick(int recipeClickedIndex) {
        //TODO: Start intent for recipe display
    }
}
