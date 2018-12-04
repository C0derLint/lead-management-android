package com.community.jboss.leadmanagement;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.community.jboss.leadmanagement.main.MainActivity;

public class LaunchActivity extends AppCompatActivity {
    boolean wasRunBefore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);

        ImageView logo = findViewById(R.id.app_logo);
        TextView title = findViewById(R.id.app_title);

        Animation slide_up = AnimationUtils.loadAnimation(getApplicationContext(),
                R.anim.slide_up);

        logo.startAnimation(slide_up);
        title.startAnimation(slide_up);

        new Handler().postDelayed( () -> {
            SharedPreferences preferences = getSharedPreferences(getString(R.string.preferences_name), Context.MODE_PRIVATE);
            wasRunBefore = preferences.getBoolean(getString(R.string.was_run_before_key), false);
            //Show info slider on first run
            if(wasRunBefore) {
                final Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                finish();
            }else {
                final Intent intent = new Intent(this, InfoSliderActivity.class);
                startActivity(intent);
                finish();
            }
        }, 2000);
    }
}
