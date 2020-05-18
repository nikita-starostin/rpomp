package com.example.ipr1.database.entries.note

import android.content.ContentValues
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.sqlite.transaction
import com.example.ipr1.database.Contract

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

    private fun updateNote(openDatabaseHelper: SQLiteOpenHelper, note: NoteModel): Long {
        val recordsUpdated = openDatabaseHelper.writableDatabase.update(
            Contract.NoteEntry.TABLE_NAME,
            note.getContentValues(),
            "${Contract.NoteEntry.COLUMN_NAME_NOTE_ID} = ?",
            arrayOf(note.id.toString())
        ).toLong()
        updateNoteChips(openDatabaseHelper, note)
        return recordsUpdated
    }

    private fun addNote(openDatabaseHelper: SQLiteOpenHelper, note: NoteModel): Long {
        val insertedId = openDatabaseHelper.writableDatabase.insert(Contract.NoteEntry.TABLE_NAME, null, note.getContentValues())
        note.id = insertedId.toInt()
        updateNoteChips(openDatabaseHelper, note)
        return insertedId
    }

    private fun updateNoteChips(
        openDatabaseHelper: SQLiteOpenHelper,
        note: NoteModel
    ) {
        openDatabaseHelper.writableDatabase.delete(
            Contract.NoteHasChipsEntry.TABLE_NAME,
            "${Contract.NoteHasChipsEntry.COLUMN_NAME_NOTE_ID} = ?",
            arrayOf(note.id.toString())
        )
        note.chips.forEach {
            val contentValues = ContentValues()
            contentValues.put(Contract.NoteHasChipsEntry.COLUMN_NAME_NOTE_ID, note.id)
            contentValues.put(Contract.NoteHasChipsEntry.COLUMN_NAME_CHIP_ID, it.id)
            openDatabaseHelper.writableDatabase.insert(Contract.NoteHasChipsEntry.TABLE_NAME, null, contentValues)
        }
    }
}