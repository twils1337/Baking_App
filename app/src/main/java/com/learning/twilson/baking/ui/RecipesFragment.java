package com.learning.twilson.baking.ui;


import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learning.twilson.baking.R;
import com.learning.twilson.baking.activities.RecipeDetailActivity;
import com.learning.twilson.baking.adapters.RecipeAdapter;
import com.learning.twilson.baking.interfaces.RecipeAdapterOnClickHandler;
import com.learning.twilson.baking.models.Recipe;

import java.util.ArrayList;
import java.util.List;

import static com.learning.twilson.baking.activities.RecipeDetailActivity.EXTRA_RECIPE_ID;
/**
 * A simple {@link Fragment} subclass.
 */
public class RecipesFragment extends Fragment implements RecipeAdapterOnClickHandler {

    private List<Recipe> mRecipes;
    private RecyclerView mRecipeRV;
    private boolean mTwoPane;
    private RecipeAdapterOnClickHandler mHandler;

    public RecipesFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (savedInstanceState != null){
            mRecipes = new Gson().fromJson(savedInstanceState.getString("RecipesJson"),
                                    new TypeToken<List<Recipe>>(){}.getType());
        }
        View rootView = inflater.inflate(R.layout.fragment_recipes, container, false);

        List<String> names = getNamesForCards();
        mRecipeRV = rootView.findViewById(R.id.rvRecipeCards);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), calculateNumOfColumns(getActivity()));
        mRecipeRV.setLayoutManager(gridLayoutManager);
        mRecipeRV.setAdapter(new RecipeAdapter(names, this));
        mRecipeRV.setHasFixedSize(true);
        return rootView;
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
        if (mRecipes != null)
        {
            for (Recipe recipe: mRecipes){
                names.add(recipe.getName());
            }
        }
        return names;
    }

    public void setRecipes(List<Recipe> recipes){
        mRecipes = recipes;
    }

    public List<Recipe> getRecipes(){
        return mRecipes;
    }

    public void setTwoPane(boolean enabled){
        mTwoPane = enabled;
    }

    public void setOnClickHandler(RecipeAdapterOnClickHandler handler){
        mHandler = handler;
    }
    @Override
    public void onClick(int recipeClickedIndex) {
        if (!mTwoPane){
            Intent recipeDetailIntent = new Intent(getActivity(), RecipeDetailActivity.class);
            String recipeJson = new Gson().toJson(mRecipes);
            recipeDetailIntent.putExtra("RecipesJSON", recipeJson);
            recipeDetailIntent.putExtra(EXTRA_RECIPE_ID, recipeClickedIndex+1);
            startActivity(recipeDetailIntent);
        }
        else{
            mHandler.onClick(recipeClickedIndex);
        }

    }

}
