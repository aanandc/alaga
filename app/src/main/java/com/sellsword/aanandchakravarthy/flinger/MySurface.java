package com.sellsword.aanandchakravarthy.flinger;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aanand.chakravarthy on 4/29/2016.
 */
class MySurface extends SurfaceView implements SurfaceHolder.Callback {

    public FireThread mythread = null;
    public boolean threadStarted = false;
    public boolean DEBUGSCREEN = true;
    public boolean gameover = false;
    int screenheight;
    int screenwidth;
    long oldtime = 0;
    int bullets_fired = 0;

    class FireThread extends Thread {
        boolean running = true;
        SurfaceHolder myholder;
        Context myContext;

        int score=0;

        private Bitmap mBackgroundImage;
        public FireThread(SurfaceHolder sholder,Context context){
            WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
            Display display = wm.getDefaultDisplay();
            DisplayMetrics metrics = new DisplayMetrics();
            display.getMetrics(metrics);
            screenheight = metrics.heightPixels;
            screenwidth = metrics.widthPixels;
            tempship =  new Ship(screenheight,screenwidth);
            gun = new GunShip(screenheight,screenwidth);
            ships.add(tempship);
            myholder = sholder;
            myContext = context;

        }
        public void startwork(){
            running = true;
        }

        public void stopwork(){

            running = false;
        }

        Ship tempship;
        @Override
        public void run() {


            while(running){
                if(!myholder.getSurface().isValid()) {

                    continue;
                }
                Canvas c = null;
                try{
                    c = myholder.lockCanvas();
                    if(c!=null) {
                        doDraw(c);
                    }
                    else{
                        Log.e("vasanth","c is null");
                    }
                }
                finally{
                    if(c!=null) {
                        myholder.unlockCanvasAndPost(c);
                    }
                }
            }
            saveHighScore();
        }

        // Call this method in the game end
        private void saveHighScore() {
            SharedPreferences sharedPreferences = myContext.getSharedPreferences(
                    StartScreenActivity.LABEL_ALAGA_SP, Context.MODE_PRIVATE);
            if(sharedPreferences.getInt(myContext.getString(R.string.saved_high_score), score) < score) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putInt(myContext.getString(R.string.saved_high_score), score);
                editor.apply();
            }
        }

        float x=10,y=50;
        public boolean fireBullet(){
            //if we did fire the bullet then we return true
            boolean retval = false;
            long currtime = System.currentTimeMillis();
            if((bullet == null || bullet.isAlive() == false) && (oldtime == 0 || currtime - oldtime >= gun.gunCoolDownTime) ) {
                oldtime = currtime;
                gun.lastFired = currtime ;
                bullets_fired++;
                //modify gun.y so that the bullet starts from the tip of the gun
                bullet = new Bullet(screenheight, screenwidth,(int)gun.x,(int)gun.y);
                synchronized (bullets) {
                    bullets.add(bullet);
                    retval = true;
                }
            }
            return retval;
        }
        GunShip gun = null;
        private Bullet bullet = null;
        private List<Ship> ships = new ArrayList<Ship>();
        private ArrayList<Bullet>  bullets = new ArrayList<Bullet>();
        private void doDraw(Canvas mycanvas){
            Paint black = new Paint();
            Paint white = new Paint();
            black.setAntiAlias(true);
            white.setAntiAlias(true);
            black.setARGB(255, 0, 0, 0);
            white.setARGB(128,255,255,255);
            white.setTextSize(30);//Avoid hardcoding, do based on screen size
            //mycanvas.drawBitmap(mBackgroundImage, 0, 0, null);
            //Log.d("vasanth","screenheight is " + screenheight + "screenwidth is " + screenwidth);
            mycanvas.drawRect(0,0,screenwidth,screenheight,black);
            gun.moveAndDrawGun(mycanvas);

            mycanvas.drawText("Score : "+score,screenwidth-150,screenheight-100,white);
            if(DEBUGSCREEN){
                mycanvas.drawText("width : " + screenwidth , screenwidth - 200, screenheight - 200, white);
                mycanvas.drawText("height : " + screenheight , screenwidth - 200, screenheight - 300, white);
            }
            //mycanvas.drawText("Score : "+score,
            ArrayList<Ship> deadShips = new ArrayList<Ship>();
            ArrayList<Bullet> deadBullets = new ArrayList<Bullet>();
            synchronized (ships) {
            for(Ship s:ships) {
                s.moveShipAndDraw(mycanvas,bullets);
                if(s.y > screenheight){
                    //Game over
                    white.setTextSize(50);
                    mycanvas.drawRect(0,0,screenwidth,screenheight,black);
                    mycanvas.drawText("Game Over", screenwidth/3,screenheight/2,white);
                    mycanvas.drawText("Score : "+ score ,screenwidth/3,screenheight/2-100,white);
                    Log.d("aanand","bullets fired : " + bullets_fired);
                    mycanvas.drawText("Accuracy : " + Math.round((float)score/(float)bullets_fired*100f) + " %",screenwidth/3,screenheight/2-200,white);
                    gameover = true;
                    mythread.stopwork();

                }
                //Add to dead list if ship is dead
                if(s.dead){
                    deadShips.add(s);
                }
            }
                score = score + deadShips.size();
                ships.removeAll(deadShips);
                if(ships.size() == 0 ){
                    ships.add(new Ship(screenheight,screenwidth));
                }

            synchronized (bullets) {
                for (Bullet b : bullets) {
                    b.moveBulletAndDraw(mycanvas);
                    if(b.isAlive() == false){
                        deadBullets.add(b);
                    }
                }
            }
                bullets.removeAll(deadBullets);
            Ship newship = null;
            for(Ship s:ships){
                if(s.readyForNextShip()){
                    newship = new Ship(screenheight,screenwidth);
                }
            }
            if(newship != null){

                    ships.add(newship);
                }
            }
             x++;
            if(x>screenwidth) {
                x = 0;
                y = y + 100;
            }
            try {
                Thread.sleep(30);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    };
    Context mycontext = null;
    public MySurface(Context context) {
        super(context);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        mycontext = context;
        mythread = new FireThread(holder,mycontext);

    }

    private void startLoadingActivity(){
        Intent loadingIntent = new Intent();
        loadingIntent.setClass(mycontext,LoadingActivity.class);
        mycontext.startActivity(loadingIntent);

    }



    @Override
    protected void onAttachedToWindow (){
        super.onAttachedToWindow();
        Log.d("vasanth","surface attached");
        startThread();

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("vasanth","surface created");

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    public void moveGunRight(float distance){
        mythread.gun.moveGunRight(distance);
    }
    public void moveGunLeft(float distance){
        mythread.gun.moveGunLeft(distance);
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
       mythread.stopwork();
        try {
            mythread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void startThread(){

        mythread.startwork();
        mythread.start();
    }

}