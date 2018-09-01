package com.learning.twilson.baking;

import android.support.test.espresso.IdlingRegistry;
import android.support.test.espresso.IdlingResource;
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
import static android.support.test.espresso.action.ViewActions.scrollTo;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.intent.Intents.intended;
import static android.support.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static android.support.test.espresso.matcher.ViewMatchers.withId;
import static android.support.test.espresso.matcher.ViewMatchers.withText;


/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class InstrumentedTests {

    @Rule
    public ActivityTestRule<MainActivity> mActivityTestRule = new ActivityTestRule<>(MainActivity.class);

    private IdlingResource mIdlingResource;

    @Before
    public void setup(){
        mIdlingResource = mActivityTestRule.getActivity().getIdlingResource();
        IdlingRegistry.getInstance().register(mIdlingResource);
        Intents.init();
    }

    @Test
    public void clickRecipeCard_startsRecipeDetailActivity() {
        onView(ViewMatchers.withId(R.id.rvRecipeCards))
                .perform(RecyclerViewActions
                .actionOnItemAtPosition(0, click()));
        intended(hasComponent(RecipeDetailActivity.class.getName()));
        onView(withId(R.id.tvRecipeTitle)).check(matches(withText("Nutella Pie")));
    }

    @Test
    public void navigationButtonsOnRecipeDetailsWork() {
        onView(ViewMatchers.withId(R.id.rvRecipeCards))
                .perform(RecyclerViewActions
                .actionOnItemAtPosition(0, click()));
        onView(ViewMatchers.withId(R.id.btnNext)).perform(scrollTo()).perform(click());
        onView(withId(R.id.tvRecipeTitle)).check(matches(withText("Brownies")));
        onView(ViewMatchers.withId(R.id.btnPrevious)).perform(scrollTo()).perform(click());
        onView(withId(R.id.tvRecipeTitle)).check(matches(withText("Nutella Pie")));
    }

    @Test
    public void videoIsDisplayedInStepActivity(){
        onView(ViewMatchers.withId(R.id.rvRecipeCards)).perform(RecyclerViewActions
                .actionOnItemAtPosition(0, click()));
        onView(ViewMatchers.withId(R.id.rvSteps)).perform(RecyclerViewActions
                .actionOnItemAtPosition(0, RecyclerViewCustomAction.clickOnStepDetailButton(R.id.btnStepInfo)));
        onView(withId(R.id.playerView)).check(matches(isDisplayed()));
    }



    @After
    public void cleanup(){
        Intents.release();
        IdlingRegistry.getInstance().unregister(mIdlingResource);
    }
}
