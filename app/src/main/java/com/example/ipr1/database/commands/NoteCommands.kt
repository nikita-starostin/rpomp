package com.example.ipr1.database.commands

import android.database.sqlite.SQLiteOpenHelper
import com.example.ipr1.database.Contract
import com.example.ipr1.database.models.NoteModel

object NoteCommands {
    fun deleteNote(openDatabaseHelper: SQLiteOpenHelper, noteId: Int) {
        openDatabaseHelper.writableDatabase.delete(
            Contract.NoteEntry.TABLE_NAME,
            "${Contract.NoteEntry.COLUMN_NAME_NOTE_ID}=?",
            arrayOf(noteId.toString())
        )
    }

    fun addOrUpdateNote(openDatabaseHelper: SQLiteOpenHelper, note: NoteModel): Long = if (note.id == 0) {
        addNote(openDatabaseHelper, note)
    } else {
        updateNote(openDatabaseHelper, note)
    }

    private fun updateNote(openDatabaseHelper: SQLiteOpenHelper, note: NoteModel) =
        openDatabaseHelper.writableDatabase.update(
            Contract.NoteEntry.TABLE_NAME,
            note.getContentValues(),
            "${Contract.NoteEntry.COLUMN_NAME_NOTE_ID} = ?",
            arrayOf(note.id.toString())
        ).toLong()

    private fun addNote(openDatabaseHelper: SQLiteOpenHelper, note: NoteModel) =
        openDatabaseHelper.writableDatabase.insert(Contract.NoteEntry.TABLE_NAME, null, note.getContentValues())
}