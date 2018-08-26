package com.learning.twilson.baking.widget;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Intent;
import android.content.Context;

import com.google.gson.Gson;
import com.learning.twilson.baking.models.Recipe;

import static com.learning.twilson.baking.activities.RecipeDetailActivity.EXTRA_RECIPE_ID;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class IngredientService extends IntentService {
    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_GET_INGREDIENTS = "com.learning.twilson.baking.widget.action.FOO";



    public IngredientService() {
        super("IngredientService");
    }

    public static void startActionGetIngredients(Context context, Recipe recipe) {
        Intent intent = new Intent(context, IngredientService.class);
        intent.setAction(ACTION_GET_INGREDIENTS);
        intent.putExtra("RecipeJSON", new Gson().toJson(recipe));
        intent.putExtra(EXTRA_RECIPE_ID, recipe.getId());
        context.startService(intent);
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_GET_INGREDIENTS.equals(action)) {
                handleActionGetIngredients(intent);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionGetIngredients(Intent intent) {
        Recipe recipe = new Gson().fromJson(intent.getExtras().getString("RecipeJSON"), Recipe.class);
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);
        int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, IngredientsWidgetProvider.class));
        IngredientsWidgetProvider.updateBakingWidgets(this, appWidgetManager,
                recipe, appWidgetIds);
    }
}
