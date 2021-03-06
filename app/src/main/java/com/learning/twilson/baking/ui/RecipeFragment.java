package com.learning.twilson.baking.ui;


import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.learning.twilson.baking.R;
import com.learning.twilson.baking.activities.StepDetailActivity;
import com.learning.twilson.baking.adapters.StepAdapter;
import com.learning.twilson.baking.interfaces.StepsAdapterOnClickHandler;
import com.learning.twilson.baking.models.Ingredient;
import com.learning.twilson.baking.models.Recipe;
import com.learning.twilson.baking.models.Step;
import com.learning.twilson.baking.widget.IngredientService;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeFragment extends Fragment
                            implements StepsAdapterOnClickHandler {
    private List<Recipe> mRecipes;
    private int mCurrentRecipePos;
    private Recipe mRecipe;
    private RecyclerView mStepsRV;

    public RecipeFragment() {
        // Required empty public constructor
        setRetainInstance(true);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (savedInstanceState != null) {
            if (savedInstanceState.containsKey("RecipeJson")) {
                mRecipe = new Gson().fromJson(savedInstanceState.getString("RecipeJson"), Recipe.class);
            }
        }
        Intent parentIntent = getActivity().getIntent();
        Bundle extras = parentIntent.getExtras();
        if (mRecipe == null){
            if (extras.containsKey("RecipeJSON")){
                String recipeJson = extras.getString("RecipeJSON");
                mRecipe = new Gson().fromJson(recipeJson, Recipe.class);
            }
        }
        View rootView = inflater.inflate(R.layout.fragment_recipe, container, false);
        buildRecipeTitle(rootView);
        buildIngredientsList(rootView);
        buildStepsList(rootView);
        IngredientService.startActionGetIngredients(getActivity(), mRecipe);
        return rootView;
    }

    private void buildRecipeTitle(View rootView){
        TextView recipeTitle = rootView.findViewById(R.id.tvRecipeTitle);
        recipeTitle.setText(mRecipe.getName());
    }

    private void buildIngredientsList(View rootView){
        populateIngredientsList(mRecipe.getIngredients(), rootView);
    }

    private void buildStepsList(View rootView){
        mStepsRV = rootView.findViewById(R.id.rvSteps);
        LinearLayoutManager llManager = new LinearLayoutManager(getActivity());
        mStepsRV.setLayoutManager(llManager);
        mStepsRV.setAdapter(new StepAdapter(mRecipe.getSteps(), this));
    }

    private void populateIngredientsList(List<Ingredient> ingredients, View rootView) {
        TextView ingredientsTV = rootView.findViewById(R.id.tvIngredientsList);
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

    public static String getFormattedStringForIngredientList(Ingredient ingredient){
        return String.format("• %s %s %s",
                ingredient.getQuantity().toString(),
                ingredient.getMeasure(),
                ingredient.getIngredient());
    }

    public void setRecipes(List<Recipe> recipes){
        mRecipes = recipes;
    }

    public void setCurrentRecipePos(int pos){
        mCurrentRecipePos = pos;
        mRecipe = mRecipes.get(mCurrentRecipePos);
    }
    public List<Recipe> getRecipes(){
        return mRecipes;
    }
    public int getCurrentRecipePos(){
        return mCurrentRecipePos;
    }

    @Override
    public void onClick(int stepClickedIndex) {
            Intent stepDetailIntent = new Intent(getActivity(), StepDetailActivity.class);
            List<Step> steps = mRecipe.getSteps();
            Gson gson = new Gson();
            String stepJson = gson.toJson(steps);
            stepDetailIntent.putExtra("StepsJSON", stepJson);
            stepDetailIntent.putExtra("currentStep", stepClickedIndex);
            startActivity(stepDetailIntent);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("RecipeJson", new Gson().toJson(mRecipe));
    }
}
