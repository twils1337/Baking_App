package com.learning.twilson.baking.utils;

import android.os.Handler;
import android.util.Log;

import com.learning.twilson.baking.IdlingResource.SimpleIdlingResource;
import com.learning.twilson.baking.interfaces.ClientService;
import com.learning.twilson.baking.models.Recipe;
import com.learning.twilson.baking.utils.ServiceGenerator;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RecipeRetriever {
    private static final int DELAY_MS = 10000;

    private static List<Recipe> mRecipes = new ArrayList<>();

    public interface DelayerCallback{
        void onDone(List<Recipe> recipes);
    }

    public static void fetchRecipes(final DelayerCallback callback, final SimpleIdlingResource idlingResource){
        if (idlingResource != null){
            idlingResource.setIdleState(false);
        }

        ClientService client = ServiceGenerator.createService(ClientService.class);
        Call<List<Recipe>> call = client.getRecipes();
        call.enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, Response<List<Recipe>> response) {
                mRecipes = response.body();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {
                Log.e("Loading Recipes", "onFailure: "+t.getMessage() );
            }
        });

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (callback != null) {
                    callback.onDone(mRecipes);
                    if (idlingResource != null) {
                        idlingResource.setIdleState(true);
                    }
                }
            }
        }, DELAY_MS);
    }
}
