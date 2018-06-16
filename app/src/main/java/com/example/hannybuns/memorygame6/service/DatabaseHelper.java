package com.example.hannybuns.memorygame6.service;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.hannybuns.memorygame6.object.MyLocation;
import com.example.hannybuns.memorygame6.object.Player;
import com.google.android.gms.maps.model.LatLng;

/**
 * Created by HannyBuns on 6/16/2018.
 */

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "NEW_players.db";
    public static final String TABLE_NAME = "best_players_table";
    public static final String COL1 = "id";
    public static final String COL2 = "name";
    public static final String COL3 = "birthday";
    public static final String COL4 = "points";
    public static final String COL5 = "LAT";
    public static final String COL6 = "LNG";
    public static MyLocation myloca;

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
        SQLiteDatabase db = this.getWritableDatabase();
        myloca = new MyLocation(context);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + TABLE_NAME + "("+ COL1 + " Integer,"+COL2+" Text,"+COL3+
                " Text," +COL4+" Integer,"+COL5+" Double, "+COL6+" Double"+")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public int insertData(Player player){
        SQLiteDatabase db = this.getWritableDatabase();
        LatLng myLoc= new LatLng(myloca.getCurrentLocation().getLatitude(),
                myloca.getCurrentLocation().getLongitude());
        int size =dataBaseSize();
        player.setMyLoc(myLoc);
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1, size);
        contentValues.put(COL2, player.getName());
        contentValues.put(COL3, player.getBirthday());
        contentValues.put(COL4, player.getPoints());
        contentValues.put(COL5, myLoc.latitude);
        contentValues.put(COL6, myLoc.longitude);
        long res = db.insert(TABLE_NAME, null, contentValues);
        if(res == -1)
            return -1;
        else
            return size;
    }


    public boolean updateData(int playerId, int playerPoints) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        Cursor cursor =getALLData();
        while (cursor.moveToNext()) {
            if (cursor.getInt(0) == playerId) {
                contentValues.put(COL1, playerId);
                contentValues.put(COL2, cursor.getString(1));
                contentValues.put(COL3, cursor.getString(2));
                contentValues.put(COL4, playerPoints);
                contentValues.put(COL5, cursor.getDouble(4));
                contentValues.put(COL6, cursor.getDouble(5));
                db.update(TABLE_NAME, contentValues, "id = ?", new String[]{String.valueOf(playerId)});
                return true;

            }
        }
        return true;
    }

    public Cursor getALLData(){
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.rawQuery("select * from "+TABLE_NAME, null);
        return res;
    }

    public int dataBaseSize(){
        SQLiteDatabase db = this.getWritableDatabase();
        int size=0;
        Cursor res = db.rawQuery("select * from "+TABLE_NAME, null);
        while (res.moveToNext()) {
            size++;
        }
        return size;
    }

    public Cursor getRecords() {
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor res = db.
                rawQuery("SELECT * FROM " + TABLE_NAME + " ORDER BY " + COL4 + " DESC LIMIT 10 ", null);
        return res;

    }

}
