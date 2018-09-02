package com.learning.twilson.baking.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.learning.twilson.baking.R;
import com.learning.twilson.baking.interfaces.StepsAdapterOnClickHandler;
import com.learning.twilson.baking.models.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepAdapter extends RecyclerView.Adapter<StepAdapter.StepHolder>{
    private List<Step> mSteps;
    private final StepsAdapterOnClickHandler mParentClickListener;

    public StepAdapter(List<Step> steps, StepsAdapterOnClickHandler handler){
        mSteps = steps;
        mParentClickListener = handler;
    }

    @Override
    public int getItemCount() {
        return mSteps == null ? 0 : mSteps.size();
    }

    @Override
    public void onBindViewHolder(@NonNull StepHolder holder, int position) {
        holder.bind(mSteps.get(position).getDescription());
    }

    @NonNull
    @Override
    public StepHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.step_description, parent, false);
        return new StepHolder(view);
    }

    public class StepHolder extends RecyclerView.ViewHolder
                            implements OnClickListener{
        @BindView(R.id.tvStepsDetail)
        TextView tvSteps;

        @BindView(R.id.btnStepInfo)
        ImageButton btnStepsInfo;

        public StepHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            btnStepsInfo.setOnClickListener(this);
        }

        void bind(String stepDescription)
        {
            tvSteps.setText(stepDescription);
        }

        @Override
        public void onClick(View v) {
            int stepIndex = getAdapterPosition();
            mParentClickListener.onClick(stepIndex);
        }
    }
}
