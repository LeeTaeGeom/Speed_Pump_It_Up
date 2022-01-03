package com.studyandroid.game;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBHelper extends SQLiteOpenHelper {

    public static final String DBNAME="login.db";
    public DBHelper(Context context) {
        super(context, DBNAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table users ( username TEXT primary key, password TEXT, time FLOAT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists users");
    }

    public boolean newRank(String username, float time){
        boolean check= true;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = null;
        cursor = db.rawQuery("select * from users where username=?",new String[] {username});

        try{
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
//          아이디 String a=cursor.getString(0);
//          비번  String b=cursor.getString(1);
                Float oldtime=cursor.getFloat(2);
//                Log.v("end",oldtime+"? ");
//                Log.v("endn",cursor.getString(2)+"? ");
                if(oldtime<time){
                    check=false;
                }
                if(cursor.getString(2)==null){
                    check=true;
                }


                cursor.moveToNext();

            }
//            Log.v("endn",cursor.getString(2)+"? ");

            return check;
        }
        finally {
            cursor.close();
        }
    }
    public boolean changeRank(String username, float time){

        ContentValues values = new ContentValues();
        SQLiteDatabase db = this.getWritableDatabase();
        try{
//            values.put("username",username);
//            values.put("password",password);
            values.put("time",time);
//            Log.v("end12",time+"? ");
//            Log.v("end123",password+"? ");


            db.update("users",values,"username=?",new String[] {username});
            return true;
        }
        finally {
            db.close();
        }

    }
    public Cursor getAllData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users order by time asc",null);
        return cursor;
    }

    public Boolean insertData(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        try{
            values.put("username",username);
            values.put("password",password);
            long result = db.insert("users",null,values);
            if(result>=1)return true;
            else
                return false;
        }
        finally {
            db.close();
        }

    }

    public Boolean checkusername(String username){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where username=?",new String[] {username});
        try{
            if(cursor.getCount()>0)
                return true;
            else
                return false;
        }
        finally {
            cursor.close();
        }

    }

    public Boolean checkusernamepassword(String username, String password){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery("select * from users where username=? and password=?",new String[] {username,password});
        try{
           if(cursor.getCount()>0)
                return true;
            else
                return false;
        }
        finally {
            cursor.close();
        }

    }

}
