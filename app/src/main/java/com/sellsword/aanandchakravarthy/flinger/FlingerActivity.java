package com.sellsword.aanandchakravarthy.flinger;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class FlingerActivity extends Activity {
    AudioAttributes attrs = new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build();
    SoundPool sp = new SoundPool.Builder()
            .setMaxStreams(10)
            .setAudioAttributes(attrs)
            .build();
    int soundIds[] = new int[10];

//TODO when activity is paused, pause the thread also
MySurface mySurface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("loading in progress");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();*/
        soundIds[0] = sp.load(this, R.raw.gun_fire_p90, 1);
        mySurface = new MySurface(this);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //setContentView(R.layout.activity_flinger);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(mySurface);
        Log.d("vasanth","oncreate completed");
        //dialog.hide();
    }
    @Override
    protected void onResume(){
        super.onResume();
        Log.d("vasanth","onresume activity");
        /*if(mySurface.mythread == null){
            mythread = new Thread();
        }*/
    }
    @Override
    protected void onPause(){
        super.onPause();
        mySurface.mythread.stopwork();
        sp.release();
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //Log.i("vasanth","down");
                mySurface.mythread.fireBullet();
                sp.play(soundIds[0], 1, 1, 1, 0, 1.0f);
                break;

            case MotionEvent.ACTION_UP:
                //Log.i("vasanth","up");
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.i("vasanth","move");
                break;
        }
             return true;
    }


}


