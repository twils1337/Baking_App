package com.learning.twilson.baking.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.google.gson.Gson;
import com.learning.twilson.baking.R;
import com.learning.twilson.baking.models.Step;

public class StepDetailActivity extends AppCompatActivity {
    private Step mStep;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        Intent parentIntent = getIntent();
        Bundle extras = parentIntent.getExtras();
        if (mStep == null){
            if (extras.containsKey("StepJSON")){
                String stepJson = extras.getString("StepJSON");
                Gson gson = new Gson();
                mStep = gson.fromJson(stepJson, Step.class);
            }
        }
        //TODO: Build View
        //TODO: Add Media Player
    }
}
