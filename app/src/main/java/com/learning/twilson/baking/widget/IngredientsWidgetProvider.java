package com.learning.twilson.baking.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.widget.RemoteViews;

import com.google.gson.Gson;
import com.learning.twilson.baking.R;

import com.learning.twilson.baking.activities.MainActivity;
import com.learning.twilson.baking.activities.RecipeDetailActivity;
import com.learning.twilson.baking.models.Ingredient;
import com.learning.twilson.baking.models.Recipe;
import com.learning.twilson.baking.ui.RecipeFragment;


import java.util.List;

import static com.learning.twilson.baking.activities.RecipeDetailActivity.EXTRA_RECIPE_ID;

/**
 * Implementation of App Widget functionality.
 */
public class IngredientsWidgetProvider extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager, Recipe recipe, int appWidgetId) {

        RemoteViews views = getRemoteViews(context, recipe);

        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    private static RemoteViews getRemoteViews(Context context, Recipe recipe){
        Intent intent;
        if (recipe.getId() <= 0){
            intent = new Intent(context, MainActivity.class);
        }
        else{
            intent = new Intent(context, RecipeDetailActivity.class);
            intent.putExtra("RecipeJSON", new Gson().toJson(recipe));
            intent.putExtra(EXTRA_RECIPE_ID, recipe.getId());
        }
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.ingredients_widget);
        buildWidgetView(views, recipe);
        views.setOnClickPendingIntent(R.id.tvWidgetRecipe, pendingIntent);
        return views;
    }

    private static String getFormattedIngredients(Recipe recipe){
        StringBuilder sb = new StringBuilder();
        List<Ingredient> ingredients = recipe.getIngredients();
        for (int i = 0; i < ingredients.size(); ++i) {
            sb.append(RecipeFragment.getFormattedStringForIngredientList(ingredients.get(i)));
            if (i < ingredients.size()-1){
                sb.append(System.getProperty("line.separator"));
            }
        }
        return sb.toString();
    }

    public static void buildWidgetView(RemoteViews views, Recipe recipe){
        views.setTextViewText(R.id.tvWidgetRecipe, recipe.getName());
        String ingredients = getFormattedIngredients(recipe);
        views.setTextViewText(R.id.tvIngredients, ingredients);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // There may be multiple widgets active, so update all of them
    }

    public static void updateBakingWidgets(Context context, AppWidgetManager appWidgetManager,
                                           Recipe recipe, int[] appWidgetIds){
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, recipe, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

