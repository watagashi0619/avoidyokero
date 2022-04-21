package xyz.pota.avoidyokero;

import android.content.res.Resources;
import android.util.DisplayMetrics;

/**
 * Created by pota on 2016/10/22.
 */

public class Circle {

    DisplayMetrics dm = Resources.getSystem().getDisplayMetrics();
    int wid = dm.widthPixels;
    float width = wid/2;

    public float _x, _y, _r;

    Circle(){
        _x = width/dm.density;
        _y = _r = 0;
    }

    Circle(float x, float y, float r){
        _x = x;
        _y = y;
        _r = r;
    }

}
