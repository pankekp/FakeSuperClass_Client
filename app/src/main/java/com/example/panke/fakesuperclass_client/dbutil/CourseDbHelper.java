package com.example.panke.fakesuperclass_client.dbutil;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import static android.R.attr.version;

/**
 * Created by sbml1 on 2017/3/25.
 */

public class CourseDbHelper extends SQLiteOpenHelper {
    public CourseDbHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version, null);

    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql="create table teachers (tId text primary key,tName text)";
        db.execSQL(sql);
        String sql2="create table schedules (sId integer primary key,classNo text,classes text,peopleNum integer,time text,location text,tId text,cId text)";
        db.execSQL(sql2);
        String sql3="create table courses (cId String primary key,cName text,credit text,method text,type text)";
        db.execSQL(sql3);
        String sql4="create table semesters (SemesterId text primary key,SemesterName text)";
        db.execSQL(sql4);
        String sql5="create table timestamps (TimeStampId integer primary key,timeStamp integer)";
        db.execSQL(sql5);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table teachers");
        db.execSQL("drop table schedule");
        db.execSQL("drop table course");
        db.execSQL("drop table semesters");
        db.execSQL("drop table timestamps");
        onCreate(db);
    }
}
