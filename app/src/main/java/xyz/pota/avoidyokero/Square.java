package xyz.pota.avoidyokero;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.ArrayList;
import java.util.StringTokenizer;

import static xyz.pota.avoidyokero.MainActivity.newRECORD;
import static xyz.pota.avoidyokero.MainActivity.oldRECORD;
import static xyz.pota.avoidyokero.Player.circle;

/**
 * Created by pota on 2016/10/22.
 */

public class Square extends Task {

    protected Paint paint = new Paint();

    int a,b,c,d;
    DisplayMetrics displaymetrics = Resources.getSystem().getDisplayMetrics();
    int wid = displaymetrics.widthPixels;
    int hid = displaymetrics.heightPixels;
    int dens = (int)(displaymetrics.density);
    int hid75p = (int)(hid/100*75/dens);
    int centerH = (int)(hid-paint.getFontMetrics(null))/2/dens;
    int centerW = wid/2/dens;

    ArrayList<Integer> arrayLsquare = new ArrayList<Integer>();
    ArrayList<Integer> arrayRsquare = new ArrayList<Integer>();
    ArrayList<Integer> arrayUsquare = new ArrayList<Integer>();
    ArrayList<Integer> arrayBsquare = new ArrayList<Integer>();

    static int count=0;
    static int level=1;
    int cadd = level * 3;
    int dadd = level * level;
    int phase;
    int size;
    int fire,fire2;
    boolean starter=true;
    static boolean startp = false;
    static boolean throughball = false;
    static boolean recordstarter = true;

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        canvas.scale(MainActivity.Xdens, MainActivity.Ydens);
        if (GameMgr.status == GameMgr.eStatus.NORMAL) {
            paint.setTextSize(40);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            canvas.drawText(String.valueOf(count), 50/dens, 100, paint);
            paint.setAntiAlias(false);
        }
        paint.setStyle(Paint.Style.STROKE);
        a = (int) (Math.random() * (wid - 150) / dens);
        b = (int) ((Math.random() * 100 + 100 )/ dens);
        c = (int) ((Math.random() * 100) + 75 / dens)+cadd;
        d = (int) ((Math.random() * 100 + 150 )/dens)+dadd;
        if(cadd==level*2||dadd==level*level/2){
            level++;
            phase=0;
            cadd = level * 3;
            dadd = level * level;
        }
        if(phase==20) {
            phase=0;
            cadd--;
            dadd--;
        }

        if (starter) {
                count = 0;
                level = 1;
                cadd = level * 3;
                dadd = level * level;
                phase = 0;
                arrayRsquare.add(new Integer(a));
                arrayLsquare.add(new Integer(a + c));
                arrayUsquare.add(new Integer(-650 - b));
                arrayBsquare.add(new Integer(-650));
                starter = false;
            }

        if(level==1&&arrayBsquare.get(0)<-500&&-600<arrayBsquare.get(0)){
            paint.setTextSize(40);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            canvas.drawText("3", centerW-paint.measureText("3")/2, centerH, paint);
            MainActivity.position=0;
        }
        if(level==1&&arrayBsquare.get(0)<-400&&-500<arrayBsquare.get(0)){
            paint.setTextSize(40);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            canvas.drawText("2", centerW-paint.measureText("2")/2, centerH, paint);
            MainActivity.position=0;
        }
        if(level==1&&arrayBsquare.get(0)<-300&&-400<arrayBsquare.get(0)){
            paint.setTextSize(40);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            canvas.drawText("1", centerW-paint.measureText("1")/2, centerH, paint);
        }
        if(level==1&&arrayBsquare.get(0)<-200&&-300<arrayBsquare.get(0)){
            paint.setTextSize(40);
            paint.setStyle(Paint.Style.FILL);
            paint.setAntiAlias(true);
            canvas.drawText("START!", centerW-paint.measureText("START!")/2, centerH, paint);
            startp = true;
        }

        for (int k = 0; k < arrayUsquare.size(); k++) {
            canvas.drawRoundRect(new RectF(0, arrayUsquare.get(k), arrayRsquare.get(k), arrayBsquare.get(k)), 3, 3, paint);
            canvas.drawRoundRect(new RectF(arrayLsquare.get(k), arrayUsquare.get(k), (int) (wid / displaymetrics.density), arrayBsquare.get(k)), 3, 3, paint);
            size = arrayUsquare.size();
            if (arrayUsquare.get(size - 1) > d) {
                arrayRsquare.add(new Integer(a));
                arrayLsquare.add(new Integer(a + c));
                arrayUsquare.add(new Integer(-d - b));
                arrayBsquare.add(new Integer(-d));
            }
            if (arrayUsquare.get(0) > hid / displaymetrics.density) {
                arrayLsquare.remove(0);
                arrayRsquare.remove(0);
                arrayUsquare.remove(0);
                arrayBsquare.remove(0);
                k--;
            }
        }
    }

    public boolean onUpdate() {
        if(GameMgr.status == GameMgr.eStatus.NORMAL) {
            for (int i = 0; i < size; i++) {
                int l = arrayBsquare.get(i);
                int m = arrayUsquare.get(i);
                arrayBsquare.set(i, l + (level+1));
                arrayUsquare.set(i, m + (level+1));

                if (arrayBsquare.get(i) > hid75p - (circle._r / dens)) {
                    throughball = true;
                    fire = i;
                    fire2 = arrayUsquare.get(fire);
                }
                if (arrayUsquare.get(i) > hid75p + (circle._r / dens)) {
                    throughball = false;
                }
            }
            for(int j=0;j<level+1;j++) {
                if (fire2+j == hid75p + (circle._r / dens)) {
                    count++;
                    phase++;
                }
            }
            if (throughball == true) {
                if ((arrayRsquare.get(fire) > (circle._x - circle._r) / dens || ((circle._x + circle._r) / dens) > arrayLsquare.get(fire))) {
                    recorder();
                    GameMgr.status = GameMgr.eStatus.GAMEOVER;
                }
            }
            if(wid<circle._x||circle._x<0){
                recorder();
                GameMgr.status = GameMgr.eStatus.GAMEOVER;
            }
        }
        return true;
    }

    public void recorder(){
        boolean through = true;
        for (int i = 0; i < 10; i++) {
            if (count!=0&&i==0 && count >= oldRECORD[0]) {
                newRECORD[0] = count;
                through = false;
            }else if(through&&count!=0&&0<i&& oldRECORD[i-1] >= count && count >= oldRECORD[i]) {
                newRECORD[i] = count;
                through = false;
            }else if(through){
                newRECORD[i] = oldRECORD[i];
            }else{
                newRECORD[i] = oldRECORD[i-1];
            }
        }
        for (int i = 0; i < 10; i++) {
            oldRECORD[i] = newRECORD[i];
        }
    }
}

