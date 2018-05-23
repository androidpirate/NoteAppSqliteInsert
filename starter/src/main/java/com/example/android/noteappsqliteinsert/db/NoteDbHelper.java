package com.example.android.noteappsqliteinsert.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.example.android.noteappsqliteinsert.db.NoteContract.NoteEntry;
import com.example.android.noteappsqliteinsert.model.Note;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

/**
 * NoteDbHelper class that is used to create, update notes database.
 * Also used to perform CRUD functions on the database.
 */
public class NoteDbHelper extends SQLiteOpenHelper {
    private static final String TAG = "NoteDbHelper";
    private static final String DATABASE_NAME = "notes.db";
    private static final int DATABASE_VERSION = 1;
    private static final String COMMA_SEPARATOR = ",";
    // SQL command to create notes table
    private static final String SQL_CREATE_TABLE = "CREATE TABLE " + NoteEntry.TABLE_NAME + "(" +
            NoteEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEPARATOR +
            NoteEntry.NOTE_TITLE + " TEXT NOT NULL" + COMMA_SEPARATOR +
            NoteEntry.NOTE_DESCRIPTION + " TEXT NOT NULL " + ");";
    // SQL command to drop a table
    private static final String SQL_DROP_TABLE = "DROP TABLE IF EXISTS " + NoteEntry.TABLE_NAME;
    // Static instance
    private static NoteDbHelper sInstance;

    public static NoteDbHelper getInstance(Context context) {
        if(sInstance == null) {
            sInstance = new NoteDbHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private NoteDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DROP_TABLE);
        onCreate(db);
    }

    public List<Note> getAllNotes() {
        List<Note> notes = new ArrayList<>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.query(NoteEntry.TABLE_NAME,
                                null,
                                null,
                                null,
                                null,
                                null,
                                null);
        try {
            if(cursor.moveToFirst()) {
                do {
                    Note note = new Note();
                    note.set_id(cursor.getInt(cursor.getColumnIndex(NoteEntry._ID)));
                    note.setTitle(cursor.getString(cursor.getColumnIndex(NoteEntry.NOTE_TITLE)));
                    note.setDescription(cursor.getString(cursor.getColumnIndex(NoteEntry.NOTE_DESCRIPTION)));
                    notes.add(note);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.d(TAG, "Error fetching notes from database.");
        } finally {
            if(cursor != null && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return notes;
    }

    public void insertNote(Note note) {
        SQLiteDatabase db = getWritableDatabase();
        db.beginTransaction();
        try {
            ContentValues values = new ContentValues();
            values.put(NoteEntry.NOTE_TITLE, note.getTitle());
            values.put(NoteEntry.NOTE_DESCRIPTION, note.getDescription());
             // Note id is needed to insert a note to database since
             // SQLite automatically increments the id numbers
            db.insertOrThrow(NoteEntry.TABLE_NAME, null, values);
            db.setTransactionSuccessful();
        } catch (Exception e){
            Log.d(TAG, "Problem inserting note into database.");
        } finally {
            db.endTransaction();
        }
    }
}
