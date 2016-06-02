package com.sellsword.aanandchakravarthy.flinger;

import android.content.Context;
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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by aanand.chakravarthy on 4/29/2016.
 */
class MySurface extends SurfaceView implements SurfaceHolder.Callback {

    public FireThread mythread = null;
    class FireThread extends Thread {
        boolean running = true;
        SurfaceHolder myholder;
        Context myContext;
        int screenheight;
        int screenwidth;
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
            mBackgroundImage = BitmapFactory.decodeResource(context.getResources(),
                    R.drawable.earthrise);
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
                   Log.d("vasanth","surface is not valid");
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
        }

        float x=10,y=50;
        public void fireBullet(){
            Bullet b = new Bullet(screenheight,screenwidth);
            synchronized (bullets) {
                bullets.add(b);
            }
        }
        GunShip gun = null;
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
            gun.drawGun(mycanvas);

            mycanvas.drawText("Score : "+score,screenwidth-150,screenheight-100,white);
            //mycanvas.drawText("Score : "+score,
            ArrayList<Ship> deadShips = new ArrayList<Ship>();
            synchronized (ships) {
            for(Ship s:ships) {
                s.moveShipAndDraw(mycanvas,bullets);
                if(s.y > screenheight){
                    //Game over
                    white.setTextSize(50);
                    mycanvas.drawRect(0,0,screenwidth,screenheight,black);
                    mycanvas.drawText("Game Over", screenwidth/3,screenheight/2,white);
                    mythread.stopwork();

                }
                //Add to dead list if ship is dead
                if(s.dead){
                    deadShips.add(s);
                }
            }
                score = score + deadShips.size();
                ships.removeAll(deadShips);
                //Log.d("vasanth","ships size " + ships.size());
                if(ships.size() == 0 ){
                    ships.add(new Ship(screenheight,screenwidth));
                }

            synchronized (bullets) {
                for (Bullet b : bullets) {
                    b.moveBulletAndDraw(mycanvas);
                }
            }
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
            //mycanvas.save();

            //drawShip(mycanvas,x,y,circlePaint,10);


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
            //y++;
        }


    };
    Context mycontext = null;
    public MySurface(Context context) {
        super(context);
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        mycontext = context;
       //
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("vasanth","surface created");
        mythread = new FireThread(holder,mycontext);
        mythread.startwork();
        mythread.start();
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

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
}