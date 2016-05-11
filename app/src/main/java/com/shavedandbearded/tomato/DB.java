package com.shavedandbearded.tomato;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.AsyncTask;

public class DB extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "app.db";
    public static final int DATABASE_VERSION = 29;


    public interface OnDBReadyListener {
        public void onReady(SQLiteDatabase db);
    }

    private static DB db;

    private DB(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public static synchronized DB getInstance(Context context){
        if(db == null)
            db = new DB(context.getApplicationContext());
        return db;
    }

    private static final String SQL_CREATE_SHOPPING_LIST =
            "CREATE TABLE shopping_list (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "quantity REAL," +
                    "label TEXT)";

    private static final String SQL_CREATE_PANTRY_LIST =
            "CREATE TABLE pantry_list (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "quantity REAL," +
                    "label TEXT)";

    private static final String SQL_CREATE_COOKBOOK =
            "CREATE TABLE cookbook (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "name TEXT," +
                    "instructions TEXT)";

    private static final String SQL_CREATE_INGREDIENTS =
            "CREATE TABLE ingredients (" +
                    "_id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "recipeName TEXT," +
                    "ingName TEXT," +
                    "quantity REAL," +
                    "label TEXT)";

    private static final String SQL_DELETE_SHOPPING_LIST = "DROP TABLE IF EXISTS shopping_list";
    private static final String SQL_DELETE_PANTRY = "DROP TABLE IF EXISTS pantry_list";
    private static final String SQL_DELETE_COOKBOOK = "DROP TABLE IF EXISTS cookbook";
    private static final String SQL_DELETE_INGREDIENTS = "DROP TABLE IF EXISTS ingredients";

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL(SQL_CREATE_SHOPPING_LIST);
        db.execSQL(SQL_CREATE_PANTRY_LIST);
        db.execSQL(SQL_CREATE_COOKBOOK);
        db.execSQL(SQL_CREATE_INGREDIENTS);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int a, int b)
    {
        db.execSQL(SQL_DELETE_SHOPPING_LIST);
        db.execSQL(SQL_DELETE_PANTRY);
        db.execSQL(SQL_DELETE_COOKBOOK);
        db.execSQL(SQL_DELETE_INGREDIENTS);
        onCreate(db);
    }

    public void getWritableDatabase(OnDBReadyListener listener)
    {
        new OpenDBAsyncTask().execute(listener);
    }

    private class OpenDBAsyncTask extends AsyncTask<OnDBReadyListener, Void, SQLiteDatabase>
    {
        OnDBReadyListener listener;

        @Override
        protected SQLiteDatabase doInBackground(OnDBReadyListener... params)
        {
            listener = params[0];
            return db.getWritableDatabase();
        }

        @Override
        protected void onPostExecute(SQLiteDatabase db)
        {
            listener.onReady(db);
        }

    }
}
