package com.example.a277hackathon.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.a277hackathon.Model.Trade;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "mySQLite.db";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("CREATE TABLE trade (url VARCHAR(255), datapoint VARCHAR(255))");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }

    public void insertTrade(Trade trade) {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("INSERT INTO trade(url, datapoint) VALUES(?,?)", new Object[]{trade.getUrl(), trade.getDatapoint()});
    }
    public void dropTrade() {
        SQLiteDatabase database = getWritableDatabase();
        database.execSQL("DROP TABLE trade");
    }

    @SuppressLint("Range")
    public Trade readTreade(String url) {
        SQLiteDatabase database = getReadableDatabase();
        Cursor cursor  = database.rawQuery(
                "SELECT * FROM trade WHERE url=?",
                new String[]{url});
        if (cursor.moveToFirst()) {
            Trade trade = new Trade();
            trade.setUrl(cursor.getString(cursor.getColumnIndex("url")));
            trade.setDatapoint(cursor.getString(cursor.getColumnIndex("datapoint")));
            cursor.close();
            return trade;
        } else {
            return null;
        }
    }

    /**
     * This is the call for graph library
     * @param trade
     * @return the list of list of list of float of trade information
     */
    public List<List<List<Float>>> convertTreadList(Trade trade) {
        List<List<List<Float>>> output = new ArrayList<>();
        output.add(trade.toList());
        return output;
    }



}