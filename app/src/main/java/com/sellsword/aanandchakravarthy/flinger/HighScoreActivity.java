package com.sellsword.aanandchakravarthy.flinger;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class HighScoreActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score);

        // get high score from SharedPreferences to display
        TextView textView = new TextView(this);
        textView.setTextSize(50);
        textView.setText("High Score: " + getSharedPreferences(
                StartScreenActivity.LABEL_ALAGA_SP, Context.MODE_PRIVATE).getInt(
                getString(R.string.saved_high_score), 0));

        RelativeLayout relativeLayout = (RelativeLayout) findViewById(R.id.content);
        relativeLayout.addView(textView);
    }
}
