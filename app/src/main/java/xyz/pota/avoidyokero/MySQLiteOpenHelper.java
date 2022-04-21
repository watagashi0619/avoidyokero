package xyz.pota.avoidyokero;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import static xyz.pota.avoidyokero.MainActivity.mydb;

/**
 * Created by pota on 2016/10/25.
 */

class MySQLiteOpenHelper extends SQLiteOpenHelper {

    static final String DB_NAME = "record.db";
    static final int DB_VERSION = 1;

    static final String CREATE_TABLE =  "create table record_table ( " +
            "_id integer primary key autoincrement, " +
            "record integer not null );";

    static final String DROP_TABLE = "drop table record_table;";

    public MySQLiteOpenHelper(Context c) {
        super(c, DB_NAME, null, DB_VERSION);
    }

    /**
     * データベースファイル初回使用時に実行される処理
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        // テーブル作成のクエリを発行
        db.execSQL(CREATE_TABLE);

        db.execSQL("INSERT INTO record_table(record) values(0)");
        db.execSQL("INSERT INTO record_table(record) values(0)");
        db.execSQL("INSERT INTO record_table(record) values(0)");
        db.execSQL("INSERT INTO record_table(record) values(0)");
        db.execSQL("INSERT INTO record_table(record) values(0)");
        db.execSQL("INSERT INTO record_table(record) values(0)");
        db.execSQL("INSERT INTO record_table(record) values(0)");
        db.execSQL("INSERT INTO record_table(record) values(0)");
        db.execSQL("INSERT INTO record_table(record) values(0)");
        db.execSQL("INSERT INTO record_table(record) values(0)");
    }

    /**
     * データベースのバージョンアップ時に実行される処理
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // テーブルの破棄と再作成
        db.execSQL(DROP_TABLE);
        onCreate(db);
    }
}
