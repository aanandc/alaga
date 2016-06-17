package com.sellsword.aanandchakravarthy.flinger;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by aanand.chakravarthy on 5/17/2016.
 */
public class GunShip extends Ship {
    long lastFired = 0;
    long gunCoolDownTime = 1000;
   public GunShip(int sheight,int swidth){
    super(sheight,swidth);
       x = swidth / 2;
       y = sheight - 50;
       //size = 5;
   }
    private void drawShip(Canvas mycanvas, float x, float y,int size){
        Paint currColor = new Paint();
        currColor.setAntiAlias(true);
        long currtime = System.currentTimeMillis();
        if(currtime - lastFired < gunCoolDownTime){
            //red - gun is still hot
            currColor.setARGB(255, 255, 0,0);

        }
        else {
            //green - gun in cool
            currColor.setARGB(255, 0, 255, 0);
        }
        //Bottom lean horizontal rectangle
        mycanvas.drawRect(x-(8*size),y+(8*size),x+(8*size),y+(8*size)+size,currColor);
        //Vertical central fat rectangle
        mycanvas.drawRect(x-(0.75f*size),y-(4*size),x+(0.75f*size),y,currColor);
        //Above bottom little fat horizontal rectangle
        mycanvas.drawRect(x-(6*size),y+(6*size),x+(6*size),y+(6*size)+(2*size),currColor);
        //Second above bottom , bigger fat horizontal rectangle
        mycanvas.drawRect(x-(4*size),y+(3*size),x+(4*size),y+(6*size)+(6*size),currColor);
        //third above bottm, fat horizontal rectangle
        mycanvas.drawRect(x-(2*size),y,x+(2*size),y+(4*size),currColor);

    }
    public void moveAndDrawGun(Canvas mycanvas){
        if(x >= screenwidth || x <= 0){
            direction = direction * -1;
            if(x >= screenwidth ) {
                x = screenwidth - 10;
            }
            if(x <= 0 ){
                x = 10;
            }
        }
        //x = x + (direction * Math.round(5f * (float)screenwidth/(float)RefSize.width));
        drawShip(mycanvas,x,y,size);
    }
    public void moveGunRight(float distance){
        x = x + distance;
        if (x >= screenwidth){
            x = screenwidth - 10;
        }
    }
    public void moveGunLeft(float distance){
        x = x - distance;
        if (x <= 0) {
            x = 10;
        }
    }

}
