package com.example.mymovieapp.Adapters;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBAdapter extends SQLiteOpenHelper
{
    public static final String DATABASE_NAME = "MovieApp.db";
    public static final String USERS_TABLE = "Users";


    public DBAdapter(Context context)
    {
        super(context,DATABASE_NAME,null,1);
    }

    @Override
    public void onCreate(SQLiteDatabase db)
    {
        db.execSQL("CREATE TABLE  Users (id integer primary key, username VARCHAR, password VARCHAR)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion)
    {
        db.execSQL("DROP TABLE IF EXISTS Users");
        onCreate(db);
    }

    public boolean insertUser(String username, String password)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put("username",username);
        contentValues.put("password",password);
        db.insert("Users", null, contentValues);
        return true;
    }

    public Cursor getDataUsers()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Users",null);
        return res;
    }

    public Cursor getDataUsersById(int id)
    {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor res = db.rawQuery("SELECT * FROM Users where id ="+ id + "",null );
        return res;
    }
}
