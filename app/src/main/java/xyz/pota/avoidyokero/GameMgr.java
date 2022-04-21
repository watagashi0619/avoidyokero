package xyz.pota.avoidyokero;

import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.util.Log;

import java.util.LinkedList;

/**
 * Created by pota on 2016/10/22.
 */

public class GameMgr {

    private LinkedList<Task> _taskList = new LinkedList<Task>(); //タスクリスト

    public enum eStatus{
        NORMAL,
        GAMEOVER,
        MENU,
        RECORD;
    }

    public static eStatus status = eStatus.MENU;
    static boolean firsttask = true;
    boolean resultcolor = true;

    DisplayMetrics displaymetrics = Resources.getSystem().getDisplayMetrics();
    int wid = displaymetrics.widthPixels;
    int hid = displaymetrics.heightPixels;
    int dens = (int)(displaymetrics.density);

    GameMgr() {
        _taskList.add(new Player());
        _taskList.add(new Square());

    }

    public boolean onUpdate() {
        for (int i = 0; i < _taskList.size(); i++) {
            if (_taskList.get(i).onUpdate() == false) { //更新失敗なら
                _taskList.remove(i);              //そのタスクを消す
                i--;
            }
        }
        return true;
    }

    Paint paint = new Paint();

    public void onDraw(Canvas c) {
        c.drawColor(Color.WHITE);
        for (int i = 0; i < _taskList.size(); i++) {
            _taskList.get(i).onDraw(c);
        }
        if (status == eStatus.MENU) {
            c.drawColor(Color.WHITE);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(60);
            c.drawText("よけろ！", (wid/2-paint.measureText("よけろ！"))/dens, hid/4/dens, paint);
            paint.setTextSize(40);
            paint.setStrokeWidth(2.0f);
            c.drawText("RECORD", ((wid/2)-paint.measureText("RECORD"))/dens, (hid-575+paint.getFontMetrics(null)/2)/dens, paint);
            c.drawText("START", ((wid/2)-paint.measureText("START"))/dens, (hid-375+paint.getFontMetrics(null)/2)/dens, paint);
            paint.setStyle(Paint.Style.STROKE);
            c.drawRoundRect(new RectF(125/dens,(hid-650)/dens,(wid-125)/dens,(hid-500)/dens), 3, 3, paint);
            c.drawRoundRect(new RectF(125/dens,(hid-450)/dens,(wid-125)/dens,(hid-300)/dens), 3, 3, paint);

        }
        if (status == eStatus.GAMEOVER) {
            c.drawARGB(210,255,255,255);
            paint.setColor(Color.BLACK);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(40);
            c.drawText("GAME OVER", (wid/2-paint.measureText("GAME OVER"))/dens, 100, paint);
            paint.setTextSize(60);
            paint.setColor(Color.RED);
            c.drawText(String.valueOf(Square.count), (wid/2-paint.measureText(String.valueOf(Square.count)))/dens, 200, paint);
            paint.setColor(Color.BLACK);
            paint.setTextSize(20);
            resultcolor = true;
            for(int i=1;i<6;i++) {
                if (resultcolor&&Square.count!=0&&Square.count == MainActivity.newRECORD[i-1]) {
                    paint.setColor(Color.RED);
                    resultcolor = false;
                }else{
                    paint.setColor(Color.BLACK);
                }
                c.drawText(i+". "+MainActivity.newRECORD[i-1], (wid / 4 ) / dens, 250+30*i, paint);
            }
            for(int i=1;i<5;i++) {
                if (resultcolor&&Square.count!=0&&Square.count == MainActivity.newRECORD[i+4]) {
                    paint.setColor(Color.RED);
                    resultcolor = false;
                }else{
                    paint.setColor(Color.BLACK);
                }
                    c.drawText((i+5)+". "+MainActivity.newRECORD[i+4], (wid*3 / 4 - 70) / dens, 250+30*i, paint);
            }
            c.drawText("10. "+MainActivity.newRECORD[9], (wid*3 / 4 - 92) / dens, 250+30*5, paint);

            c.drawText("RETRY", ((wid/4)-paint.measureText("RETRY"))/dens, (hid-300+paint.getFontMetrics(null)/2)/dens, paint);
            c.drawText("EXIT", ((wid*3/4)-paint.measureText("EXIT"))/dens, (hid-300+paint.getFontMetrics(null)/2)/dens, paint);
            paint.setStyle(Paint.Style.STROKE);
            paint.setStrokeWidth(2.0f);
            c.drawRoundRect(new RectF(50/dens,(hid-350)/dens,(wid/2-50)/dens,(hid-250)/dens), 3, 3, paint);
            c.drawRoundRect(new RectF((wid/2+50)/dens,(hid-350)/dens,(wid-50)/dens,(hid-250)/dens), 3, 3, paint);
            paint.setAntiAlias(true);
        }
        if (status == eStatus.RECORD) {
            c.drawColor(Color.WHITE);
            paint.setAntiAlias(true);
            paint.setStyle(Paint.Style.FILL);
            paint.setTextSize(40);
            c.drawText("RECORD", (wid/2-paint.measureText("RECORD"))/dens, 100, paint);
            paint.setTextSize(20);
            for(int i=1;i<10;i++) {
                c.drawText(i+". "+MainActivity.oldRECORD[i-1], (wid / 2 - 70) / dens, 120+30*i, paint);
            }
            c.drawText("10. "+MainActivity.oldRECORD[9], (wid / 2 - 92) / dens, 120+30*10, paint);
            paint.setTextSize(30);
            c.drawText("EXIT", ((wid/2)-paint.measureText("EXIT"))/dens, (hid-300+paint.getFontMetrics(null)/2)/dens, paint);
            paint.setStyle(Paint.Style.STROKE);
            c.drawRoundRect(new RectF(150/dens,(hid-350)/dens,(wid-150)/dens,(hid-250)/dens), 3, 3, paint);
        }
    }
}

