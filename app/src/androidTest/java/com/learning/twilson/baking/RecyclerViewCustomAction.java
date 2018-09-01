package com.learning.twilson.baking;


import android.support.test.espresso.UiController;
import android.support.test.espresso.ViewAction;

import org.hamcrest.Matcher;

import android.view.View;

public class RecyclerViewCustomAction{
    public static ViewAction clickOnStepDetailButton(final int id){
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return null;
            }

            @Override
            public String getDescription() {
                return "Will click on the '?' button to get to step details.";
            }

            @Override
            public void perform(UiController uiController, View view) {
                view.findViewById(id).performClick();
            }
        };
    }
}
