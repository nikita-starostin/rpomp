package com.example.ipr1.database

import android.database.sqlite.SQLiteOpenHelper
import com.example.ipr1.database.models.Note

object Queries {
    fun queryNotes(openDatabaseHelper: SQLiteOpenHelper, title: String): ArrayList<Note> {
        var listNotes = ArrayList<Note>()
        val projections =
            arrayOf(Contract.NoteEntry.COLUMN_NAME_NOTE_ID, Contract.NoteEntry.COLUMN_NAME_TITLE, Contract.NoteEntry.COLUMN_NAME_DESCRIPTION)
        val selectionArgs = arrayOf(title)
        val cursor = openDatabaseHelper.writableDatabase.query(
            Contract.NoteEntry.TABLE_NAME,
            projections, "${Contract.NoteEntry.COLUMN_NAME_TITLE} like ?", selectionArgs,
            null,
            null,
            Contract.NoteEntry.COLUMN_NAME_TITLE
        )
        listNotes.clear()
        if (cursor.moveToFirst()) {
            do {
                val noteId = cursor.getInt(cursor.getColumnIndex(Contract.NoteEntry.COLUMN_NAME_NOTE_ID))
                val noteTitle = cursor.getString(cursor.getColumnIndex(Contract.NoteEntry.COLUMN_NAME_TITLE))
                val noteDescription =
                    cursor.getString(cursor.getColumnIndex(Contract.NoteEntry.COLUMN_NAME_DESCRIPTION))
                listNotes.add(Note(noteId, noteTitle, noteDescription))
            } while (cursor.moveToNext())
        }

        return listNotes
    }
}