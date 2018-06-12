package com.example.win10.streetfighter;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;


public class DBAssistant extends SQLiteOpenHelper {

    private static final String TAG = "DBAssistant";
    private static final String TABLE_NAME = "people_score";
    private static final String COL1 = "name";
    private static final String COL2 = "score";
    private static final String COL3 = "lat";
    private static final String COL4 = "lng";

    public DBAssistant(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String dropTable = "DROP IF TABLE EXISTS "+TABLE_NAME;
        db.execSQL(dropTable);
    }


    @Override
    public void onCreate(SQLiteDatabase database) {
        String createTable = "CREATE TABLE "+TABLE_NAME+" (ID INTEGER PRIMARY KEY AUTOINCREMENT, "+COL1+" TEXT, "+COL2+" LONG,"
                +COL3+" REAL, "+COL4+" REAL )";
        database.execSQL(createTable);

    }

    public Cursor getTop10(){
        SQLiteDatabase database = this.getWritableDatabase();
        String query = "SELECT * FROM "+TABLE_NAME+" ORDER BY "+COL2 +" DESC LIMIT 10";
        Cursor data = database.rawQuery(query,null);
        return data;
    }

    public boolean addData(String name, long score, double lat, double lng){
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL1,name);
        contentValues.put(COL2,score);
        contentValues.put(COL3,lat);
        contentValues.put(COL4,lng);


        Log.d(TAG, "addData: Adding "+name+" to "+TABLE_NAME);

        long result = db.insert(TABLE_NAME,null,contentValues);

        if(result == -1)
            return false;
        else
            return true;
    }



}
