package xyz.pota.avoidyokero;

import android.graphics.Canvas;

/**
 * Created by pota on 2016/10/22.
 */

public abstract class Task {

    public boolean onUpdate(){
        return true;
    }

    public void onDraw(Canvas c){
    }

}
