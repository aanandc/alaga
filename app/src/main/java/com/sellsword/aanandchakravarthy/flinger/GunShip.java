package com.sellsword.aanandchakravarthy.flinger;

import android.graphics.Canvas;
import android.graphics.Paint;

/**
 * Created by aanand.chakravarthy on 5/17/2016.
 */
public class GunShip extends Ship {

   public GunShip(int sheight,int swidth){
    super(sheight,swidth);
       x = swidth / 2;
       y = sheight - 50;
       //size = 5;
   }
    private void drawShip(Canvas mycanvas, float x, float y,int size){
        Paint white = new Paint();
        white.setAntiAlias(true);
        white.setARGB(255, 255, 255,255);
        //Bottom lean horizontal rectangle
        mycanvas.drawRect(x-(8*size),y+(8*size),x+(8*size),y+(8*size)+size,white);
        //Vertical central fat rectangle
        mycanvas.drawRect(x-(0.75f*size),y-(4*size),x+(0.75f*size),y,white);
        //Above bottom little fat horizontal rectangle
        mycanvas.drawRect(x-(6*size),y+(6*size),x+(6*size),y+(6*size)+(2*size),white);
        //Second above bottom , bigger fat horizontal rectangle
        mycanvas.drawRect(x-(4*size),y+(3*size),x+(4*size),y+(6*size)+(6*size),white);
        //third above bottm, fat horizontal rectangle
        mycanvas.drawRect(x-(2*size),y,x+(2*size),y+(4*size),white);

    }
    public void drawGun(Canvas mycanvas){
        drawShip(mycanvas,x,y,size);
    }

}
