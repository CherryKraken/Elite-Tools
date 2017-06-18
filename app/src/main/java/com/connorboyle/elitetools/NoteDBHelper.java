package com.connorboyle.elitetools;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by Connor Boyle on 2017-06-16.
 */

public class NoteDBHelper extends SQLiteOpenHelper {
    private static final String DB_NAME = "notebook.db";
    private static final String TABLE_NAME = "notes";
    private static final int DB_VERSION = 1;
    private static final String ID = "_id";
    private static final String TITLE = "title";
    private static final String TEXT = "text";
    private static final String MODIFIED = "modified";

    private SQLiteDatabase sqlDB;

    public NoteDBHelper(Context context) { super(context, DB_NAME, null, DB_VERSION); }

    void open() throws SQLException { sqlDB = getWritableDatabase(); }

    public void close() { sqlDB.close(); }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String table = "CREATE TABLE " + TABLE_NAME + " (" + ID + " integer primary key " +
                TITLE + " text not null " +
                TEXT + " text " +
                MODIFIED + " integer not null);";
        db.execSQL(table);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    public Cursor getNotes() { return getNotes(null); }

    public Cursor getNotes(String order_by) {
        String [] fields = { ID, TITLE, TEXT, MODIFIED };
        return sqlDB.query(TABLE_NAME, fields, null, null, null, null, order_by);
    }

    public boolean deleteNote(NoteEntry note) {
        return sqlDB.delete(TABLE_NAME, ID + "='" + note.id + "'", null) > 0;
    }

    public long createNote(NoteEntry note) {
        ContentValues cvs = new ContentValues();
        cvs.put(TITLE, note.title);
        cvs.put(TEXT, note.text);
        cvs.put(MODIFIED, note.modified);
        return note.id = sqlDB.insert(TABLE_NAME, null, cvs);
    }

    public boolean updateNote(NoteEntry note) {
        if (note.id < 0) return false;
        ContentValues cvs = new ContentValues();
        cvs.put(TITLE, note.title);
        cvs.put(TEXT, note.text);
        cvs.put(MODIFIED, note.modified);
        return sqlDB.update(TABLE_NAME, cvs, ID + "='" + note.id + "'", null) > 0;
    }
}
