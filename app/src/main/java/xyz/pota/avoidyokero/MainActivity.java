package xyz.pota.avoidyokero;

import android.app.Activity;
import android.content.ContentValues;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.WindowManager;

public class MainActivity extends Activity {

    DisplayMetrics displaymetrics = Resources.getSystem().getDisplayMetrics();
    int wid = displaymetrics.widthPixels;
    int hid = displaymetrics.heightPixels;
    float width = wid/2;

    GameSurfaceView view;

    static SQLiteDatabase mydb;
    static int[] oldRECORD = new int[10];
    static int[] newRECORD = new int[10];

    static float Xdens, Ydens;
    float positionx,positiony;
    boolean touchresp;
    static int position = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        MySQLiteOpenHelper hlpr = new MySQLiteOpenHelper(getApplicationContext());
            mydb = hlpr.getWritableDatabase();
            Cursor cursor = mydb.query("record_table", new String[]{"_id", "record"}, null, null, null, null, null);
            cursor.moveToFirst();
            for(int i = 0;i<cursor.getCount();i++){
                newRECORD[i] = cursor.getInt(1);
                oldRECORD[i] = cursor.getInt(1);
                cursor.moveToNext();
            }
           cursor.close();

        super.onCreate(savedInstanceState);
        view = new GameSurfaceView(this);
        setContentView(view);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics displayMetrics = new DisplayMetrics();
        display.getMetrics(displayMetrics);

        Xdens = displayMetrics.density;
        Ydens = displayMetrics.density;

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        positionx = event.getX();
        positiony = event.getY();
        if (GameMgr.status == GameMgr.eStatus.NORMAL) {
            if (event.getAction() == MotionEvent.ACTION_UP) {
                GameSurfaceView.pstatus = 3;
            }
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                if (positionx > width) {
                    GameSurfaceView.pstatus = 1;
                }
                if (positionx < width) {
                    GameSurfaceView.pstatus = 2;
                }
            }
        }else{
            GameSurfaceView.pstatus=0;
        }
        if(GameMgr.status == GameMgr.eStatus.GAMEOVER) {
            Square.throughball = false;
            if(!touchresp&&event.getAction() == MotionEvent.ACTION_DOWN) {
                if (50 < positionx && positionx < wid / 2 - 50 && hid - 350 < positiony && positiony < hid - 250) {
                    view = new GameSurfaceView(this);
                    setContentView(view);
                }
                if (wid / 2 + 50 < positionx && positionx < wid - 50 && hid - 350 < positiony && positiony < hid - 250) {
                    GameMgr.status = GameMgr.eStatus.MENU;
                }
            }
        }
        if(GameMgr.status == GameMgr.eStatus.MENU){
            if(event.getAction() == MotionEvent.ACTION_UP&&125<positionx&&positionx<wid-125&&hid-450<positiony&&positiony<hid-300){
                view = new GameSurfaceView(this);
                setContentView(view);
            }
            if(event.getAction() == MotionEvent.ACTION_UP&&125<positionx&&positionx<wid-125&&hid-650<positiony&&positiony<hid-500){
                GameMgr.status = GameMgr.eStatus.RECORD;
            }
        }
        if(GameMgr.status == GameMgr.eStatus.RECORD) {
            if(event.getAction() == MotionEvent.ACTION_UP&&150<positionx&&positionx<wid-150&&hid-300<positiony&&positiony<hid-250){
                GameMgr.status = GameMgr.eStatus.MENU;
            }
        }
        return true;
    }

    //連続増減
    private Runnable repeatPlus = new Runnable(){
        @Override
        public void run(){
            while(touchresp){
                try{
                    Thread.sleep(50); //0.05秒イベント中断
                }catch(InterruptedException e){
                }
                if(GameMgr.status == GameMgr.eStatus.NORMAL) {
                        if (positionx > width) {
                            position = 5+2*(Square.level+1);
                        }
                        if (positionx < width) {
                            position = -5-2*(Square.level+1);
                        }
                }
            }
        }
    };

    //連続ゼロ
    private Runnable repeatZero = new Runnable(){
        @Override
        public void run(){
            while(!touchresp){
                try{
                    Thread.sleep(50);  //0.05秒イベント中断
                }catch(InterruptedException e){
                }
                position=0;
            }
        }
    };

}

    //public void getdatabase(int[] record){
    //    MySQLiteOpenHelper hlpr = new MySQLiteOpenHelper(getApplicationContext());
    //    mydb = hlpr.getWritableDatabase();
    //    Cursor cursor = mydb.query("record_table", new String[]{"_id", "record"}, null, null, null, null, null);
    //    cursor.moveToFirst();
    //    for(int i = 0;i<cursor.getCount();i++){
    //        record[i] = cursor.getInt(1);
    //        cursor.moveToNext();
    //    }
     //   cursor.close();
    //}

