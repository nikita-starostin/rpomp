package com.example.ipr1.database

import android.provider.BaseColumns

object Contract {
    const val DATABASE_NAME = "NotesDB"
    const val DATABASE_VERSION = 2

    object NoteEntry : BaseColumns {
        const val TABLE_NAME = "Notes"
        const val COLUMN_NAME_TITLE = "Title"
        const val COLUMN_NAME_DESCRIPTION = "Description"

        const val SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME (${BaseColumns._ID} INTEGER PRIMARY KEY, $COLUMN_NAME_TITLE TEXT, $COLUMN_NAME_DESCRIPTION TEXT);"
        const val SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS $TABLE_NAME"

    }
}