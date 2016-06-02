package com.sellsword.aanandchakravarthy.flinger;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

import java.util.ArrayList;

/**
 * Created by aanand.chakravarthy on 5/11/2016.
 */
public class Ship {
    float x,y;

    boolean dead = false;
    int screenheight,screenwidth;
    int size = 5;
    private boolean readyForNext = true;
    public Ship(int sheight,int swidth){
        x = 10;
        y = 50;
        screenheight = sheight;
        screenwidth = swidth;
    }
    int direction = 1;
    private void drawShip(Canvas mycanvas, float x, float y, Paint scolor, int size){
        mycanvas.drawCircle(x,y,size,scolor);
        mycanvas.drawRect(x+size,y-(2*size),x+(2*size),y+(2*size),scolor);
        mycanvas.drawRect(x-(2*size),y-(2*size),x-size,y+(2*size),scolor);
        //mycanvas.drawRect(x-size,y,x+15,y+10,scolor);

    }

    public boolean isCollision(Bullet b){
        //bullet size added extra to compensate for bullet movement (100)
        //as our bullet moves in steps of 100, it will not hit all the places,
        //so to appear fast and yet kill the enemies, add 100 in the collision detection.
        Rect bulletRect = new Rect(b.x-25,b.y-100,b.x+b.size+25,b.y+b.size);
        //Rect bulletRect = new Rect(b.x,b.y,b.x+1,b.y+1);
        Rect spaceRect = new Rect((int)x-(2*size),(int)y-(2*size),(int)x+(2*size),(int)y+(2*size));

        if(spaceRect.intersect(bulletRect)) {
            Log.d("vasanth","collided");
            return true;
        }
        else{
            return false;
        }
    }

    public boolean readyForNextShip(){
       if (y > 100 && x > screenwidth/2 && readyForNext){
           readyForNext = false;
           return true;
       }
        return false;
    }
    public void moveShipAndDraw(Canvas mycanvas,ArrayList<Bullet> bullets){
        Paint circlePaint = new Paint();
        circlePaint.setAntiAlias(true);
        circlePaint.setARGB(255, 0, 255, 0);
        drawShip(mycanvas,x,y,circlePaint,size);//TODO size to be based on screensize
        synchronized (bullets) {
            for (Bullet b : bullets) {
                if (isCollision(b)) {
                    dead = true;
                    break;
                }
            }
        }
        x = x + (direction * 10);
        y = y + 2; // Makes the game much faster
        //Log.d("vasanth","x is " + x);
        if(x >= screenwidth || x <= 0){
            direction = direction * -1;
            if(x >= screenwidth) {
                x = screenwidth - 10;
            }
            if(x <= 0 ){
                x = 10;
            }

            y = y + 10;

        }
    }
}
