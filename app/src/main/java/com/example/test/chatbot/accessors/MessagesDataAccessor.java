package com.example.test.chatbot.accessors;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.test.chatbot.models.Message;
import com.example.test.chatbot.storage.SQLiteHelper;

import java.util.ArrayList;

public class MessagesDataAccessor {

    public static final String TABLE_NAME = "messages";

    public static final String CHAT_WINDOW = "chat_window";
    public static final String MESSAGE = "message";
    public static final String NAME = "name";
    public static final String TYPE = "type";

    private SQLiteDatabase mDatabase;
    private SQLiteHelper mHelper;

    private String[] allColumns = {CHAT_WINDOW, MESSAGE, NAME, TYPE};

    public MessagesDataAccessor(Context context) {
        mHelper = SQLiteHelper.getInstance(context);
    }

    private ContentValues convertFromMessage(int window, Message message) {
        ContentValues cv = new ContentValues();
        cv.put(CHAT_WINDOW, window);
        cv.put(MESSAGE, message.getMessage());
        cv.put(NAME, message.getName());
        cv.put(TYPE, message.getType().toString());

        return cv;
    }

    private Message convertToMessage(Cursor cursor) {
        Message message = new Message();
        message.setMessage(cursor.getString(cursor.getColumnIndex(MESSAGE)));
        message.setName(cursor.getString(cursor.getColumnIndex(NAME)));
        message.setType(cursor.getString(cursor.getColumnIndex(TYPE)));
        return message;
    }

    public void insert(int window, Message message) {
        mDatabase = mHelper.getWritableDatabase();
        mDatabase.insert(TABLE_NAME, null, convertFromMessage(window, message));
    }

    public ArrayList<Message> getAllMessages(int window) {
        ArrayList<Message> allMessages = new ArrayList<>();
        mDatabase = mHelper.getReadableDatabase();
        String selection = CHAT_WINDOW + " =? ";
        String[] selectionArgs = new String[]{String.valueOf(window)};
        Cursor cursor = mDatabase.query(TABLE_NAME, allColumns, selection, selectionArgs, null, null, null);
        if (cursor != null) {
            while (cursor.moveToNext()) {
                Message message = convertToMessage(cursor);
                if (message != null) {
                    allMessages.add(message);
                }
            }
        }

        return allMessages;
    }
}
