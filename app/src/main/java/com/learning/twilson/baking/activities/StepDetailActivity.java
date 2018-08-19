package com.learning.twilson.baking.activities;

import android.content.Intent;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.learning.twilson.baking.R;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learning.twilson.baking.models.Step;
import com.learning.twilson.baking.ui.StepFragment;

import java.util.ArrayList;
import java.util.List;

public class StepDetailActivity extends AppCompatActivity {
    private StepFragment mStepFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        if (savedInstanceState == null){
            mStepFragment = getStepFragment();

            FragmentManager fragmentManager = getSupportFragmentManager();

            fragmentManager.beginTransaction()
                    .add(R.id.flStep, mStepFragment)
                    .commit();
        }
    }

    private StepFragment getStepFragment(){
        StepFragment stepFragment = new StepFragment();
        List<Step> steps = getStepsFromIntent(getIntent());
        int currentStepPos = getCurrentStepPosFromIntent(getIntent());
        stepFragment.setSteps(steps);
        stepFragment.setCurrentStepPos(currentStepPos);
        return stepFragment;
    }

    private List<Step> getStepsFromIntent(Intent intent){
        Bundle extras = intent.getExtras();
        if (extras.containsKey("StepsJSON")){
            return new Gson().fromJson(extras.getString("StepsJSON"), new TypeToken<List<Step>>(){}.getType());
        }
        else{
            return new ArrayList<>();
        }
    }

    private int getCurrentStepPosFromIntent(Intent parentIntent){
        Bundle extras = parentIntent.getExtras();
        if (extras.containsKey("currentStep")){
            return extras.getInt("currentStep", 0);
        }
        else{
            return 0;
        }
    }

    public void onNavClick(View v){
        final View clickedButton = v;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String buttonClicked = clickedButton.getId() == R.id.btnNext ? "Next" : "Prev";
                List<Step> steps = mStepFragment.getSteps();
                SimpleExoPlayer player = mStepFragment.getExoPlayer();
                int currentStepPos = mStepFragment.getCurrentStepPos();
                if ( (buttonClicked == "Next" && currentStepPos+1 >= steps.size()) ||
                        (buttonClicked == "Prev" && currentStepPos-1 < 0) ){
                    return;
                }
                if (player!=null){
                    player.stop();
                }

                Intent nextNavigatedStepIntent = new Intent(StepDetailActivity.this, StepDetailActivity.class);
                String stepsJson = new Gson().toJson(steps);
                int nextStepPos = buttonClicked == "Next" ? currentStepPos+1: currentStepPos-1;
                nextNavigatedStepIntent.putExtra("StepsJSON", stepsJson);
                nextNavigatedStepIntent.putExtra("currentStep", nextStepPos);
                finish();
                startActivity(nextNavigatedStepIntent);

            }
        }, 0);
    }
}