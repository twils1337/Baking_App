package com.learning.twilson.baking.ui;


import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.learning.twilson.baking.R;
import com.learning.twilson.baking.models.Step;

import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class StepFragment extends Fragment {

    private List<Step> mSteps;
    private Step mCurrentStep;
    private PlayerView mPlayerView;
    private SimpleExoPlayer mExoPlayer;
    private Integer mCurrentStepPos;
    private long mPlayPosition = 0;
    private boolean mAutoPlay = true;

    public StepFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_step, container, false);
        mPlayerView = rootView.findViewById(R.id.playerView);
        buildView(rootView);
        return rootView;
    }

    public void setSteps(List<Step> steps){
        mSteps = steps;
    }

    private void setCurrentStep(Step step){
        mCurrentStep = step;
    }

    public void setCurrentStepPos(int pos){
        mCurrentStepPos = pos;
        setCurrentStep(mSteps.get(pos));
    }

    private void buildView(View rootView){
        if (mCurrentStep != null){
            if ( mCurrentStep.getVideoURL().equals("")){
                mPlayerView.setVisibility(View.GONE);
            }
            TextView stepDescription = rootView.findViewById(R.id.tvStepText);
            stepDescription.setText(mCurrentStep.getDescription());
        }
    }

    public void setPlayPosition(long position){
        mPlayPosition = position;
    }

    public void setAutoPlay(boolean autoPlay){
        mAutoPlay = autoPlay;
    }

    @Override
    public void onStart() {
        super.onStart();
        if (Util.SDK_INT > 23){
            if (mCurrentStep != null){
                initializePlayer();
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (Util.SDK_INT <= 23 || mExoPlayer == null){
            if (mCurrentStep != null){
                initializePlayer();
            }

        }
    }

    private void initializePlayer(){
        if (mExoPlayer == null){
            Uri videoUri = Uri.parse(mCurrentStep.getVideoURL());
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getActivity(), new DefaultTrackSelector());
            mPlayerView.setPlayer(mExoPlayer);

            ExtractorMediaSource.Factory emsFactory = new ExtractorMediaSource.Factory(
                    new DefaultDataSourceFactory(getContext(), Util.getUserAgent(getActivity(), "Baking")));
            MediaSource videoSource = emsFactory.createMediaSource(videoUri);

            mExoPlayer.prepare(videoSource);
            mExoPlayer.setPlayWhenReady(mAutoPlay);
            mExoPlayer.seekTo(mPlayPosition);
        }
    }

    public void releasePlayer(){
        if (mExoPlayer!=null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }

    }

    public SimpleExoPlayer getExoPlayer(){
        return mExoPlayer;
    }

    public List<Step> getSteps(){
        return mSteps;
    }

    public int getCurrentStepPos(){
        return mCurrentStepPos;
    }
}
