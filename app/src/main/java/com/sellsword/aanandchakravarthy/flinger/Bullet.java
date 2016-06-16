package com.sellsword.aanandchakravarthy.flinger;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by aanand.chakravarthy on 5/13/2016.
 */
public class Bullet {

    int x,y;

    boolean alive = true;

    int size = 10;
    public Bullet(int sheight,int swidth,int newx, int newy){
        y = newy;//10 hard coded to be changed based on the GUN size
        x = newx;
        alive = true;
    }

    public boolean isAlive(){
        return alive;
    }

    public void killbullet(){
        alive = false;
    }

    private void drawBullet(Canvas mycanvas, float x, float y, Paint scolor, int size){
      mycanvas.drawRect(x,y,x+size,y+size,scolor);
    }
    public void moveBulletAndDraw(Canvas mycanvas){
        Paint white = new Paint();
        white.setAntiAlias(true);
        white.setARGB(255, 255, 255, 255);
        drawBullet(mycanvas,x,y,white,size);
        y = y - 100;
        if(y < 0){ // bullet moves beyond the screen, we kill it
            killbullet();
        }
    }
}
