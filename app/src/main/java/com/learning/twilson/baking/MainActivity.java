package com.learning.twilson.baking;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class MainActivity extends AppCompatActivity
                          implements RecipeAdapterOnClickHandler {
    private RecipeAdapterOnClickHandler mHandler = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

    }

    @Override
    public void onClick(int recipeClickedIndex) {
        //TODO: Start intent for recipe display
    }
}
