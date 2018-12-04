package com.community.jboss.leadmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import com.community.jboss.leadmanagement.main.MainActivity;
import com.hololo.tutorial.library.Step;
import com.hololo.tutorial.library.TutorialActivity;

public class InfoSliderActivity extends TutorialActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        addFragment(new Step.Builder().setTitle(getString(R.string.slide_1_header))
                .setContent(getString(R.string.slide_1_content))
                .setBackgroundColor(Color.parseColor("#04bcf4"))
                .setDrawable(R.drawable.ic_hello)
                .build());

        addFragment(new Step.Builder().setTitle(getString(R.string.slide_2_header))
                .setContent(getString(R.string.slide_2_content))
                .setBackgroundColor(Color.parseColor("#153f87"))
                .setDrawable(R.drawable.ic_client)
                .build());

        addFragment(new Step.Builder().setTitle(getString(R.string.slide_3_header))
                .setContent(getString(R.string.slide_3_content))
                .setBackgroundColor(Color.parseColor("#FF4081"))
                .setDrawable(R.drawable.ic_micro)
                .build());
    }

    //Whole tutorial has been completed. Open MainActivity and don't show Info Slider again
    @Override
    public void finishTutorial() {
        super.finishTutorial();
        final Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);

        SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_name), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();

        editor.putBoolean(getString(R.string.was_run_before_key), true);
        editor.apply();

        finish();
    }
}
