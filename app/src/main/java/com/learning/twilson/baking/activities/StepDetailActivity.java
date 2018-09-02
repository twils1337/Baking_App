package com.learning.twilson.baking.activities;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Handler;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.google.android.exoplayer2.util.Util;
import com.learning.twilson.baking.R;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learning.twilson.baking.interfaces.StepsAdapterOnClickHandler;
import com.learning.twilson.baking.models.Step;
import com.learning.twilson.baking.ui.StepFragment;
import com.learning.twilson.baking.ui.StepsFragment;

import java.util.ArrayList;
import java.util.List;

public class StepDetailActivity extends AppCompatActivity
                                implements StepsAdapterOnClickHandler{
    public static final String AUTO_PLAY = "auto_play";
    public static final String PLAY_POSITION = "position";

    private StepFragment mStepFragment;
    private StepsFragment mStepsFragment;
    private boolean mTwoPane;
    private long mPlayPosition = 0;
    private boolean mIsAutoPlay = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        mTwoPane = findViewById(R.id.flStepsTwoPane) != null;
        clearFragments(getSupportFragmentManager());
        if (savedInstanceState == null)
        {
            mStepFragment = getStepFragment();
        }
        else if (mStepsFragment == null){
            if (mStepFragment == null){
                mStepFragment = new StepFragment();
            }
            mStepFragment = new StepFragment();
        }
        buildView();
    }

    private void buildView(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        if (mTwoPane){
            mStepsFragment = new StepsFragment();
            mStepsFragment.setSteps(mStepFragment.getSteps());
            mStepsFragment.setHandler(this);
            fragmentManager.beginTransaction()
                    .add(R.id.flStepsTwoPane, mStepsFragment)
                    .add(R.id.flStepTwoPane, mStepFragment)
                    .addToBackStack("Double Add")
                    .commit();
        }
        else{
            fragmentManager.beginTransaction()
                    .add(R.id.flStep, mStepFragment)
                    .addToBackStack("Single Add")
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

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString("StepsJSON", new Gson().toJson(mStepFragment.getSteps()));
        outState.putInt("currentStep", mStepFragment.getCurrentStepPos());
        outState.putLong(PLAY_POSITION, mStepFragment.getExoPlayer().getCurrentPosition());
        outState.putBoolean(AUTO_PLAY, mStepFragment.getExoPlayer().getPlayWhenReady());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        clearFragments(getSupportFragmentManager());
        if (savedInstanceState != null){
            List<Step> steps = new Gson().fromJson(savedInstanceState.getString("StepsJSON"),
                    new TypeToken<List<Step>>(){}.getType());
            mStepFragment.setSteps(steps);
            mStepFragment.setCurrentStepPos(savedInstanceState.getInt("currentStep"));
            mStepFragment.setPlayPosition(savedInstanceState.getLong(PLAY_POSITION));
            mStepFragment.setAutoPlay(savedInstanceState.getBoolean(AUTO_PLAY));
            mTwoPane = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
            FragmentManager manager = getSupportFragmentManager();
            if (mTwoPane){
                mStepsFragment.setSteps(mStepFragment.getSteps());
                mStepsFragment.setHandler(this);
                manager.beginTransaction()
                        .add(R.id.flStepsTwoPane, mStepsFragment)
                        .add(R.id.flStepTwoPane, mStepFragment)
                        .addToBackStack("Double Add")
                        .commit();
            }
            else{
                manager.beginTransaction()
                        .add(R.id.flStep, mStepFragment)
                        .addToBackStack("Single Add")
                        .commit();
            }
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23){
            if (mStepFragment.getCurrentStep() != null){
                mStepFragment.initializePlayer();
            }
        }
    }


    @Override
    public void onStop() {
        super.onStop();
        if (Util.SDK_INT > 23){
            mStepFragment.releasePlayer();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mPlayPosition = mStepFragment.getExoPlayer().getCurrentPosition();
        mIsAutoPlay = mStepFragment.getExoPlayer().getPlayWhenReady();
        if (Util.SDK_INT <= 23){
            mStepFragment.releasePlayer();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mStepFragment.getExoPlayer() == null){
            if (mStepFragment.getCurrentStep() != null){
                mStepFragment.initializePlayer();
            }
        }
        else{
            mStepFragment.getExoPlayer().seekTo(mPlayPosition);
            mStepFragment.getExoPlayer().setPlayWhenReady(mIsAutoPlay);
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
                if ( (buttonClicked.equals("Next") && currentStepPos+1 >= steps.size()) ||
                     (buttonClicked.equals("Prev") && currentStepPos-1 < 0) ){
                    return;
                }
                if (player!=null){
                    player.stop();
                }
                Intent nextNavigatedStepIntent = new Intent(StepDetailActivity.this, StepDetailActivity.class);
                String stepsJson = new Gson().toJson(steps);
                int nextStepPos = buttonClicked.equals("Next") ? currentStepPos+1: currentStepPos-1;
                nextNavigatedStepIntent.putExtra("StepsJSON", stepsJson);
                nextNavigatedStepIntent.putExtra("currentStep", nextStepPos);
                finish();
                startActivity(nextNavigatedStepIntent);

            }
        }, 0);
    }

    private void clearFragments(FragmentManager manager){
        while(manager.getBackStackEntryCount() > 0){
            manager.popBackStackImmediate();
        }
    }

    @Override
    public void onClick(int stepClickedIndex) {
        FragmentManager manager = getSupportFragmentManager();
        mStepFragment.setCurrentStepPos(stepClickedIndex);
        manager.beginTransaction()
                .detach(mStepFragment)
                .attach(mStepFragment)
                .addToBackStack("Replace Step")
                .commit();
    }
}
