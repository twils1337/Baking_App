package com.learning.twilson.baking.ui;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import com.learning.twilson.baking.adapters.StepsAdapter;
import com.learning.twilson.baking.interfaces.StepsAdapterOnClickHandler;
import com.learning.twilson.baking.models.Step;

import java.util.ArrayList;
import java.util.List;

public class StepsFragment extends Fragment implements StepsAdapterOnClickHandler{
    private RecyclerView mStepsRV;
    private List<Step> mSteps;
    private StepsAdapterOnClickHandler mHandler;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (savedInstanceState != null){
            mSteps = new Gson().fromJson(savedInstanceState.getString("StepsJson"),
                    new TypeToken<List<Step>>(){}.getType());
        }
        View rootView = inflater.inflate(R.layout.fragment_steps, container, false);
        mStepsRV = rootView.findViewById(R.id.rvStepCards);
        GridLayoutManager manager= new GridLayoutManager(getActivity(), calculateNumOfColumns(getActivity()));
        mStepsRV.setLayoutManager(manager);
        mStepsRV.setAdapter(new StepsAdapter(getStepsDescriptions() , (StepsAdapterOnClickHandler) getActivity()));
        return rootView;
    }

    private List<String> getStepsDescriptions(){
        List<String> stepDescriptions = new ArrayList<>();
        if (mSteps != null){
            for (Step step: mSteps){
                stepDescriptions.add(step.getDescription());
            }
        }
        return stepDescriptions;
    }

    public void setSteps(List<Step> steps){
        mSteps = steps;
    }
    public void setHandler(StepsAdapterOnClickHandler handler){
        mHandler = handler;
    }

    private int calculateNumOfColumns(Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        float dpWidth = metrics.widthPixels;
        int scalingFactor = 1000;
        int numOfColumns = (int) (dpWidth/scalingFactor);
        if (numOfColumns < 1){
            numOfColumns = 1;
        }
        return numOfColumns;
    }

    @Override
    public void onClick(int stepClickedIndex) {
        mHandler.onClick(stepClickedIndex);
    }
}
