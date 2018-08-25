package com.learning.twilson.baking.activities;

import android.support.v4.app.FragmentManager;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.learning.twilson.baking.R;
import com.learning.twilson.baking.interfaces.ClientService;
import com.learning.twilson.baking.ui.RecipesFragment;
import com.learning.twilson.baking.models.Recipe;
import com.learning.twilson.baking.utils.ServiceGenerator;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {

    private RecipesFragment mRecipesFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {
            mRecipesFragment = getRecipesFragment();
            loadRecipes();
        }
    }

    private RecipesFragment getRecipesFragment(){
        RecipesFragment recipesFragment = new RecipesFragment();
        return recipesFragment;
    }

    private void loadRecipes(){
        ClientService client = ServiceGenerator.createService(ClientService.class);
        Call<List<Recipe>> call = client.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                List<Recipe> recipes = response.body();
                mRecipesFragment.setRecipes(recipes);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.flRecipes, mRecipesFragment)
                        .commit();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e("Loading Recipes", "onFailure: "+t.getMessage() );
            }
        });
    }
}
