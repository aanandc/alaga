package com.sellsword.aanandchakravarthy.flinger;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.TextView;

public class LoadingActivity extends Activity {
    String loading_msg[] = new String[]{"prepare to fight","be ready to blast alien scum","precision helps",
            "kill them all", "Never stop fighting", "Hey little fighter, things will get brighter",
            "Fight club", "Keep calm and fight on", "True love is a tight hug after a fight"};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        TextView loadingText = (TextView)findViewById(R.id.textLoadingMessage);
        long val = System.currentTimeMillis()%(long)(loading_msg.length);
        loadingText.setText(loading_msg[(int)val]);
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


    @Override
    public void onBackPressed() {
        Log.d("CDA", "onBackPressed Called");
        moveTaskToBack(true);
        //finish();
        finishAndRemoveTask();
        //android.os.Process.killProcess(android.os.Process.myPid());

    }

    @Override
    protected void onResume(){
        super.onResume();
        final Activity myAct = this;

        Thread myThread = new Thread(){

          public void run(){
              try {
                  Thread.sleep(3000);
                  Log.d("aanand","sleeping..");
              } catch (InterruptedException e) {
                  e.printStackTrace();
              }
              Log.d("aanand","sleeping..over");
              myAct.runOnUiThread(new Runnable(){
                  public void run(){
                      Log.d("aanand","code comes here");
                      //myAct.finishActivity(10);//10 is a dummy value, its not used
                      myAct.finish();

                  }
              });

          }
        };
        myThread.start();

    }

}
