package com.interlog.ilcargoreporting;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String TAG = "DBHelper";
    public static final String TABLE_NAME = "wcstock";
    public static final String COL1 = "id";
    //public static final String COL18 = "USERID";
    public static final String COL2 = "CUSTNAM";
    public static final String COL3 = "ADDR";
    public static final String COL4 = "PRODTNAM";
    public static final String COL5 = "CDATE";
    public static final String COL6 = "RDATE";
    public static final String COL7 = "VESSNAM";
    public static final String COL8 = "OPBAL";
    public static final String COL9 = "TAKON";
    public static final String COL10 = "REL1";
    public static final String COL11 = "REL2";
    public static final String COL12 = "REL3";
    public static final String COL13 = "CLSBAL";
    public static final String COL14 = "BANKRZ";
    public static final String COL15 = "BANKBA";

    public static final String SYNC_STATUS = "syncstatus";

    public DBHelper(Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "CUSTNAM TEXT, ADDR TEXT, PRODTNAM TEXT, CDATE TEXT, RDATE TEXT, VESSNAM TEXT, OPBAL TEXT, TAKON TEXT, REL1 TEXT, REL2 TEXT, REL3 TEXT, CLSBAL TEXT, BANKRZ TEXT, BANKBA TEXT, syncstatus integer)";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);

    }

    public boolean addData(String custmName, String address, String prodcName, String cdat, String rdat, String vessNam, String opeBal, String takOn, String relz1, String relz2, String relz3, String clsBal, String bankRlz, String bankBlc, int sync_status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        //contentValues.put(COL18, userID);
        contentValues.put(COL2, custmName);
        contentValues.put(COL3, address);
        contentValues.put(COL6, prodcName);
        contentValues.put(COL4, cdat);
        contentValues.put(COL5, rdat);
        contentValues.put(COL7, vessNam);
        contentValues.put(COL8, opeBal);
        contentValues.put(COL9, takOn);
        contentValues.put(COL10, relz1);
        contentValues.put(COL11, relz2);
        contentValues.put(COL12, relz3);
        contentValues.put(COL13, clsBal);
        contentValues.put(COL14, bankRlz);
        contentValues.put(COL15, bankBlc);
        contentValues.put(SYNC_STATUS, sync_status);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if data is inserted correctly it will return -1
        if (result == -1) {
            return false;
        } else {
            return true;
        }
    }

    public Cursor getData() {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor data = db.rawQuery(query, null);

        return data;
    }

    public boolean updateNameStatus(int id, int sync_status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(SYNC_STATUS, sync_status);
        db.update(TABLE_NAME, contentValues, COL1 + "=" + id, null);
        db.close();
        return true;
    }

    /*
     * this method is for getting all the unsynced name
     * so that we can sync it with database
     * */
   public Cursor getUnsyncedNames() {
        SQLiteDatabase db = this.getReadableDatabase();
        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + SYNC_STATUS + " = 0;";
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

   /* public List<String> getFN() {
        List<String> labels = new ArrayList<String>();
        // Select All Query
        //String selectQuery = "SELECT * FROM " + TABLE_NAME;
        String selectQuery = "SELECT * FROM " + TABLE_NAME + " LIMIT 1";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                labels.add(cursor.getString(2));
            } while (cursor.moveToNext());
        }

        // closing connection
        cursor.close();
        db.close();

        // returning lables
        return labels;
    } */

}