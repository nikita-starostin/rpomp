package com.example.ipr1.database

import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import com.example.ipr1.database.models.Note

object Commands {
    fun deleteNote(openDatabaseHelper: SQLiteOpenHelper, noteId: Int) {
        openDatabaseHelper.writableDatabase.delete(
            Contract.NoteEntry.TABLE_NAME,
            "${BaseColumns._ID}=?",
            arrayOf(noteId.toString())
        )
    }

    fun addOrUpdateNote(openDatabaseHelper: SQLiteOpenHelper, note: Note): Long = if (note.id == 0) {
        addNote(openDatabaseHelper, note)
    } else {
        updateNote(openDatabaseHelper, note)
    }

    private fun updateNote(openDatabaseHelper: SQLiteOpenHelper, note: Note) =
        openDatabaseHelper.writableDatabase.update(
            Contract.NoteEntry.TABLE_NAME,
            note.getContentValues(),
            "${BaseColumns._ID} = ?",
            arrayOf(note.id.toString())
        ).toLong()

    private fun addNote(openDatabaseHelper: SQLiteOpenHelper, note: Note) =
        openDatabaseHelper.writableDatabase.insert(Contract.NoteEntry.TABLE_NAME, null, note.getContentValues())
}