package xyz.pota.avoidyokero;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;

import java.sql.SQLInput;

/**
 * Created by pota on 2016/10/22.
 */

public class Player extends Task {

    DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
    int wid = dm.widthPixels;
    int hid = dm.heightPixels;
    float width = wid/2;
    float height = hid/100*75;

    public static Circle circle = null;
    private Paint paint = new Paint();

    public Player(){
        circle = new Circle(width,height,20);
        paint.setColor(Color.BLACK);
        paint.setAntiAlias(true);
    }
    @Override
    public boolean onUpdate(){
        if(Square.startp) {
            circle._x += MainActivity.position;
        }
        return true;
    }

    @Override
    public void onDraw(Canvas c ){
        c.drawCircle(circle._x, circle._y, circle._r, paint);
        if(GameMgr.status == GameMgr.eStatus.GAMEOVER){
            paint.setColor(Color.RED);
            MainActivity.position=0;
        }
    }
}
