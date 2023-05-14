package edu.university.livechat.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class KeyStoreHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "my_app_db";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "tokens";
    private static final String COLUMN_TOKEN = "token";
    private static final String COLUMN_TIMESTAMP = "timestamp";

    public KeyStoreHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + TABLE_NAME + " (" +
                COLUMN_TOKEN + " TEXT PRIMARY KEY, " +
                COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP" +
                ")";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public void insertToken(String token) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COLUMN_TOKEN, token);
        db.insert(TABLE_NAME, null, values);
        db.close();
    }

    public String getLatestToken() {
        String token = null;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_TOKEN}, null, null, null, null, COLUMN_TIMESTAMP + " DESC", "1");
        if (cursor != null && cursor.moveToFirst()) {
            token = cursor.getString(0);
        }
        if (cursor != null) {
            cursor.close();
        }
//        db.close();
        return token;
    }

    public void deleteAllTokens() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_NAME, null, null);
        db.close();
    }
}