package com.learning.twilson.baking.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.learning.twilson.baking.R;
import com.learning.twilson.baking.models.Step;

import java.util.List;

public class StepDetailActivity extends AppCompatActivity {
    private List<Step> mSteps;
    private Step mCurrentStep;
    private PlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    public Integer mCurrentStepPos;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step_detail);
        Intent parentIntent = getIntent();
        Bundle extras = parentIntent.getExtras();
        if (mSteps == null || mSteps.size() == 0){
            if (extras.containsKey("StepsJSON") && extras.containsKey("currentStep")){
                String stepJson = extras.getString("StepsJSON");
                Gson gson = new Gson();
                mSteps = gson.fromJson(stepJson, new TypeToken<List<Step>>(){}.getType());
                mCurrentStepPos = extras.getInt("currentStep",0);
                mCurrentStep = mSteps.get(mCurrentStepPos);
            }
        }
        mPlayerView = findViewById(R.id.playerView);
        buildView();
    }

    private void buildView(){
        if (mCurrentStep != null){
            if ( !mCurrentStep.getVideoURL().equals("")){
                initializePlayer();
            }
            else{
                mPlayerView.setVisibility(View.GONE);
            }
            TextView stepDescription = findViewById(R.id.tvStepText);
            stepDescription.setText(mCurrentStep.getDescription());
        }
    }

    private void initializePlayer(){
        if (mExoPlayer == null){
            Uri videoUri = Uri.parse(mCurrentStep.getVideoURL());
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(this, new DefaultTrackSelector());
            mPlayerView.setPlayer(mExoPlayer);

            ExtractorMediaSource.Factory emsFactory = new ExtractorMediaSource.Factory(
                    new DefaultDataSourceFactory(this, Util.getUserAgent(this, "Baking")));
            MediaSource videoSource = emsFactory.createMediaSource(videoUri);

            mExoPlayer.prepare(videoSource);
            mExoPlayer.setPlayWhenReady(true);
        }
    }

    private void releasePlayer(){
        if (mExoPlayer!=null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

    }

    public void onNavClick(View v){
        final View clickedButton = v;
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String buttonClicked = clickedButton.getId() == R.id.btnNext ? "Next" : "Prev";
                if ( (buttonClicked == "Next" && mCurrentStepPos+1 >= mSteps.size()) ||
                        (buttonClicked == "Prev" && mCurrentStepPos-1 < 0) ){
                    return;
                }
                if (mExoPlayer!=null){
                    mExoPlayer.stop();
                }

                Intent nextNavigatedStepIntent = new Intent(StepDetailActivity.this, StepDetailActivity.class);
                String stepsJson = new Gson().toJson(mSteps);
                int nextStepPos = buttonClicked == "Next" ? mCurrentStepPos+1: mCurrentStepPos-1;
                nextNavigatedStepIntent.putExtra("StepsJSON", stepsJson);
                nextNavigatedStepIntent.putExtra("currentStep", nextStepPos);
                finish();
                startActivity(nextNavigatedStepIntent);

            }
        }, 0);
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        releasePlayer();
    }
}
