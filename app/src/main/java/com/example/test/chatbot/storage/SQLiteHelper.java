package com.example.test.chatbot.storage;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.test.chatbot.accessors.MessagesDataAccessor;

public class SQLiteHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "chatbot";
    private static final int DATABASE_VERSION = 1;
    private static final String COLUMN_ID = "_id";

    private static Context mContext;

    public static SQLiteHelper getInstance(Context context) {
        mContext = context;
        return new SQLiteHelper(mContext);
    }

    public SQLiteHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + MessagesDataAccessor.TABLE_NAME + " ("
                + COLUMN_ID + " integer primary key autoincrement, "
                + MessagesDataAccessor.CHAT_WINDOW + " TEXT, "
                + MessagesDataAccessor.MESSAGE + " TEXT, "
                + MessagesDataAccessor.NAME + " TEXT, "
                + MessagesDataAccessor.TYPE + " TEXT"
                + ");");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
