package xyz.pota.avoidyokero;

import android.content.ContentValues;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import static xyz.pota.avoidyokero.MainActivity.mydb;
import static xyz.pota.avoidyokero.MainActivity.newRECORD;
import static xyz.pota.avoidyokero.MainActivity.position;

/**
 * Created by pota on 2016/10/22.
 */
public class GameSurfaceView extends SurfaceView implements SurfaceHolder.Callback, Runnable {

    DisplayMetrics displaymetrics = Resources.getSystem().getDisplayMetrics();
    int wid = displaymetrics.widthPixels;
    int hid = displaymetrics.heightPixels;
    float width = wid/2;

    static int pstatus;

    private enum reStatus{
        //NORMAL,
        GAMEOVER,
        MENU,
        RECORD;
    }

    reStatus restatus;

    private long mTime =0;

    private GameMgr _gameMgr = new GameMgr();
    private Thread _thread;

    public GameSurfaceView(Context context) {
        super(context);
        getHolder().addCallback(this);
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        Log.d("surfacecreated","surfacecreated");

        Canvas canvas = holder.lockCanvas();
        _thread = new Thread(this);             //別スレッドでメインループを作る
        _thread.start();
        holder.unlockCanvasAndPost(canvas);
        if(GameMgr.firsttask){
            GameMgr.status = GameMgr.eStatus.MENU;
            GameMgr.firsttask = false;
        }else{
            Square.level = 1;
            Square.recordstarter = true;
            GameMgr.status = GameMgr.eStatus.NORMAL;
        }
        if(restatus == reStatus.MENU){
            GameMgr.status = GameMgr.eStatus.MENU;
        }
        if(restatus == reStatus.RECORD){
            GameMgr.status = GameMgr.eStatus.RECORD;
        }
        if(restatus == reStatus.GAMEOVER){
            GameMgr.status = GameMgr.eStatus.GAMEOVER;
        }
        Square.startp = false;
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d("surfacedestroyed","surfacedestroyed");
        if(GameMgr.status == GameMgr.eStatus.MENU) {
            restatus = reStatus.MENU;
        }
        if(GameMgr.status == GameMgr.eStatus.RECORD) {
            restatus = reStatus.RECORD;
        }
        if(GameMgr.status == GameMgr.eStatus.GAMEOVER) {
            restatus = reStatus.GAMEOVER;
            ContentValues values = new ContentValues();
            mydb.delete("record_table", "_id like '%'", null);
            for (int i = 0; i < 10; i++) {
                values.put("record", newRECORD[i]);
                mydb.insert("record_table", null, values);
                Log.d("Destroy", String.valueOf(newRECORD[i]));
            }
        }
        _thread = null;
    }

    @Override
    public void run() {
        while (_thread!=null) { //メインループ
            long delta = System.currentTimeMillis() - mTime;
            mTime      = System.currentTimeMillis();

            //次の描画位置
            if (GameMgr.status == GameMgr.eStatus.NORMAL) {
                //Log.d("pstatus",String.valueOf(pstatus));
                int nextPosition = (int) ((delta / 1000.0) * 200) + 3 * (Square.level + 1); //1秒間に200px動くとして
                if (pstatus == 1) {
                    position = nextPosition;
                }
                if (pstatus == 2) {
                    position = -nextPosition;
                }
                if (pstatus == 3) {
                    position = 0;
                }
            }

            _gameMgr.onUpdate();
            onDraw(getHolder());
        }
    }



    private void onDraw(SurfaceHolder holder) {
        Canvas c = holder.lockCanvas();
        if(c == null){
            return;
        }
        _gameMgr.onDraw(c);
        holder.unlockCanvasAndPost(c);
    }
}