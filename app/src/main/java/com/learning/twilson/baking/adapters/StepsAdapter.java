package com.learning.twilson.baking.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.learning.twilson.baking.R;
import com.learning.twilson.baking.interfaces.StepsAdapterOnClickHandler;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsHolder> {
    private List<String> mStepDescriptions;
    private StepsAdapterOnClickHandler mHandler;

    public StepsAdapter(List<String> steps, StepsAdapterOnClickHandler handler) {
        mStepDescriptions = steps;
        mHandler = handler;
    }

    @Override
    public void onBindViewHolder(@NonNull StepsHolder holder, int position) {
        holder.bind(mStepDescriptions.get(position));
    }

    @NonNull
    @Override
    public StepsHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_item, parent, false);
        return new StepsHolder(view);
    }

    @Override
    public int getItemCount() {
        return mStepDescriptions != null ? mStepDescriptions.size() : 0;
    }

    public class StepsHolder extends RecyclerView.ViewHolder
                             implements OnClickListener{
        @BindView(R.id.tvCard)
        TextView tvCard;


        public StepsHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        void bind(String stepDescription){
            tvCard.setText(stepDescription);
            tvCard.setTextSize(12);
        }

        @Override
        public void onClick(View v) {
            int stepIndex = getAdapterPosition();
            mHandler.onClick(stepIndex);
        }
    }
}
