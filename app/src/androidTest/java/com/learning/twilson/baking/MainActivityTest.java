package com.learning.twilson.baking;

import android.support.test.espresso.intent.Intents;
import android.support.test.espresso.matcher.ViewMatchers;
import android.support.test.rule.ActivityTestRule;
import android.support.test.runner.AndroidJUnit4;

import com.learning.twilson.baking.activities.MainActivity;
import com.learning.twilson.baking.activities.RecipeDetailActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import android.support.test.espresso.contrib.RecyclerViewActions;

import static android.support.test.espresso.Espresso.onView;
import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class MainActivityTest {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup(){
        Intents.init();
    }
    @Test
    public void clickRecipeCard_startsCorrectActivity() {
        onView(ViewMatchers.withId(R.id.rvRecipeCards)).perform(RecyclerViewActions
                .actionOnItemAtPosition(0, click()));
        intended(hasComponent(RecipeDetailActivity.class.getCanonicalName()));
    }

    @After
    public void cleanup(){
        Intents.release();
    }
}
