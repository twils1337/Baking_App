package com.learning.twilson.baking.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.view.View.OnClickListener;

import com.learning.twilson.baking.R;
import com.learning.twilson.baking.interfaces.RecipeAdapterOnClickHandler;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class RecipeAdapter extends RecyclerView.Adapter<RecipeAdapter.RecipeHolder> {
    private List<String> mRecipes;
    private final RecipeAdapterOnClickHandler mParentClickListener;

    public RecipeAdapter(List<String> recipes, RecipeAdapterOnClickHandler handler){
        mRecipes = recipes;
        mParentClickListener = handler;
    }

    @Override
    public void onBindViewHolder(@NonNull RecipeHolder holder, int position) {
        holder.bind(mRecipes.get(position));
    }

    @NonNull
    @Override
    public RecipeHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.card_item, parent, false);
        return new RecipeHolder(view);
    }

    @Override
    public int getItemCount() {
        return mRecipes == null? 0 : mRecipes.size();
    }


    public class RecipeHolder extends RecyclerView.ViewHolder
                              implements OnClickListener{
        @BindView(R.id.tvRecipeName)
        TextView tvRecipeName;


        public RecipeHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
            ButterKnife.bind(this, itemView);
        }

        void bind(String recipeName){
            tvRecipeName.setText(recipeName);
        }

        @Override
        public void onClick(View v) {
            int recipeIndex = getAdapterPosition();
            mParentClickListener.onClick(recipeIndex);
        }
    }
}
