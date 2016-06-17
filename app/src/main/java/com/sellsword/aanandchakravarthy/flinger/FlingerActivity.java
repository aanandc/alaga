package com.sellsword.aanandchakravarthy.flinger;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.SoundPool;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.Window;
import android.view.WindowManager;

public class FlingerActivity extends Activity implements
        GestureDetector.OnGestureListener {
    private GestureDetector mDetector;

    AudioAttributes attrs = new AudioAttributes.Builder()
            .setUsage(AudioAttributes.USAGE_GAME)
            .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
            .build();
    SoundPool sp = new SoundPool.Builder()
            .setMaxStreams(10)
            .setAudioAttributes(attrs)
            .build();
    int soundIds[] = new int[10];
boolean loadActivity = false;
//TODO when activity is paused, pause the thread also
MySurface mySurface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        soundIds[0] = sp.load(this, R.raw.gun_fire_p90, 1);
        mySurface = new MySurface(this);
        setVolumeControlStream(AudioManager.STREAM_MUSIC);
        //setContentView(R.layout.activity_flinger);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(mySurface);
        Log.d("vasanth","oncreate completed");
        mDetector = new GestureDetector(this,this);
        startLoadingActivity();
        //dialog.hide();
    }

    @Override
    protected void onStart(){
        super.onStart();

    }

    @Override
    protected void onResume(){
        super.onResume();
        Log.d("vasanth","onresume activity");

    }

    protected void onDestroy(){
        super.onDestroy();
        sp.release();
    }
    @Override
    protected void onPause(){
        super.onPause();
        if(mySurface.mythread != null && mySurface.mythread.isAlive()) {
            mySurface.mythread.stopwork();
        }


    }

    private void startLoadingActivity(){
        Intent loadingIntent = new Intent();
        loadingIntent.setClass(this,LoadingActivity.class);
        this.startActivityForResult(loadingIntent,10);//dummy value



    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        mDetector.onTouchEvent(event);
        return true;
        /*switch(event.getAction()){
            case MotionEvent.ACTION_DOWN:
                //Log.i("vasanth","down");

                if(mySurface.mythread.fireBullet()) {
                    sp.play(soundIds[0], 1, 1, 1, 0, 1.0f);
                }
                else{
                    return false;
                }

                break;

            case MotionEvent.ACTION_UP:
                //Log.i("vasanth","up");
                break;
            case MotionEvent.ACTION_MOVE:
                //Log.i("vasanth","move");
                break;
        }
             return true;*/
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);

        //startThread();

    }

    public void startThread(){
        Log.d("aanand","code comes here too...");
        mySurface.mythread.startwork();
        mySurface.mythread.start();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)  {
        if (Integer.parseInt(android.os.Build.VERSION.SDK) > 5
                && keyCode == KeyEvent.KEYCODE_BACK
                && event.getRepeatCount() == 0) {
            Log.d("CDA", "onKeyDown Called");
            onBackPressed();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onBackPressed(){
        if(!mySurface.gameover) {
            //Ask the user to confirm if the game is not over
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setMessage("Quit the game?").setPositiveButton("Yes", dialogClickListener)
                    .setNegativeButton("No", dialogClickListener).show();
        }
        else{
            finish();
        }
    }

    DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which){
                case DialogInterface.BUTTON_POSITIVE:
                    //Yes button clicked
                    finish();
                    break;

                case DialogInterface.BUTTON_NEGATIVE:
                    //No button clicked
                    break;
            }
        }
    };


    @Override
    public boolean onDown(MotionEvent e) {
        return true;

    }

    @Override
    public void onShowPress(MotionEvent e) {

    }

    @Override
    public boolean onSingleTapUp(MotionEvent e) {
        if(mySurface.mythread.fireBullet()) {
            sp.play(soundIds[0], 1, 1, 1, 0, 1.0f);
            return true;
        }
        else{
            return false;
        }
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        //float distanceX = e2.getX() - e1.getX();
        //float distanceY = e2.getY() - e1.getY();
        Log.d("aanand","on fling operation");

            if (distanceX > 0) {
                //swipe right
                mySurface.moveGunLeft(distanceX);
            }
            else{
                //swipe left
                mySurface.moveGunRight(-1*distanceX);
            }
            return true;

    }

    @Override
    public void onLongPress(MotionEvent e) {

    }

    @Override
    public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
        return false;
    }
}


