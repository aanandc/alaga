package com.sellsword.aanandchakravarthy.flinger;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

public class StartScreenActivity extends Activity {
    final static String LABEL_ALAGA_SP = "LABEL_ALAGA_SP";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        SharedPreferences sharedPreferences;
        super.onCreate(savedInstanceState);

        // For entire app the screen orientation is portrait
        setRequestedOrientation (ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        setContentView(R.layout.activity_start_screen);

        // Save high score for first time, initial high score is zero
        sharedPreferences = getSharedPreferences(LABEL_ALAGA_SP, Context.MODE_PRIVATE);
        if(!sharedPreferences.contains(getString(R.string.saved_high_score))) {
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putInt(getString(R.string.saved_high_score), 0);
            editor.apply();
        }

        //String banner = "Alaga";
        final TextView tv = (TextView) findViewById(R.id.banner);
        //tv.setText(banner);

       /* Animation animation=new TranslateAnimation(0,480,0,0);
        animation.setDuration(5000);
        animation.setRepeatMode(Animation.RESTART);
        animation.setRepeatCount(Animation.INFINITE);
        tv.startAnimation(animation);*/

        Animation a = AnimationUtils.loadAnimation(StartScreenActivity.this, R.anim.scale_up);
        a.reset();
        a.setDuration(4000);
        a.setFillAfter(true);
        tv.startAnimation(a);

    }

    public void startGame(View view) {
        Intent intent = new Intent(this, FlingerActivity.class);
        startActivity(intent);
    }

    public void showHighScore(View view) {
        Intent intent = new Intent(this, HighScoreActivity.class);
        startActivity(intent);
    }
}
