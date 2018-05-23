package com.example.android.noteappsqliteinsert.db;

import android.provider.BaseColumns;

/**
 * Contract class that defines the table and columns
 * for Note database.
 */
public final class NoteContract {
    /**
     * Private constructor prevents
     * accidentally instantiating NoteContract class.
     */
    private NoteContract() {
    }

    public static class NoteEntry implements BaseColumns {
        public static final String TABLE_NAME = "notes";
        public static final String NOTE_TITLE = "title";
        public static final String NOTE_DESCRIPTION = "description";
    }
}
