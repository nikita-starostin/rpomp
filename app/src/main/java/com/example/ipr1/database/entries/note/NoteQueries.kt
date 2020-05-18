package com.example.ipr1.database.entries.note

import android.database.sqlite.SQLiteOpenHelper
import com.example.ipr1.database.Contract
import com.example.ipr1.database.entries.chip.ChipModel
import com.example.ipr1.database.entries.chip.ChipQueries

object NoteQueries {
    fun queryNotes(openDatabaseHelper: SQLiteOpenHelper, title: String, chips: ArrayList<ChipModel>): ArrayList<NoteModel> {
        val projections =
            arrayOf(
                Contract.NoteEntry.COLUMN_NAME_NOTE_ID,
                Contract.NoteEntry.COLUMN_NAME_TITLE,
                Contract.NoteEntry.COLUMN_NAME_DESCRIPTION
            )
        var selection = "${Contract.NoteEntry.COLUMN_NAME_TITLE} like ?"
        val selectionArgs = mutableListOf(title)
        if (chips.isNotEmpty()) {
            val noteIds = queryNoteIdsForChipsIds(openDatabaseHelper, chips.map { it.id }.toTypedArray())
            selection += " AND ${Contract.NoteEntry.COLUMN_NAME_NOTE_ID} IN (${noteIds.map { "?" }.joinToString("," )})"
            selectionArgs.addAll(noteIds.map { it.toString() })
        }
        val cursor = openDatabaseHelper.writableDatabase.query(
            Contract.NoteEntry.TABLE_NAME,
            projections,
            selection,
            selectionArgs.toTypedArray(),
            null,
            null,
            Contract.NoteEntry.COLUMN_NAME_TITLE
        )
        val notes = ArrayList<NoteModel>()
        if (cursor.moveToFirst()) {
            do {
                val noteId = cursor.getInt(cursor.getColumnIndex(Contract.NoteEntry.COLUMN_NAME_NOTE_ID))
                val noteTitle = cursor.getString(cursor.getColumnIndex(Contract.NoteEntry.COLUMN_NAME_TITLE))
                val noteDescription =
                    cursor.getString(cursor.getColumnIndex(Contract.NoteEntry.COLUMN_NAME_DESCRIPTION))
                notes.add(
                    NoteModel(
                        noteId,
                        noteTitle,
                        noteDescription,
                        ChipQueries.queryChipsForNote(openDatabaseHelper, noteId)
                    )
                )
            } while (cursor.moveToNext())
        }

        return notes
    }

    private fun queryNoteIdsForChipsIds(openDatabaseHelper: SQLiteOpenHelper, chipIds: Array<Int>): ArrayList<Int> {
        val projections = arrayOf(Contract.NoteHasChipsEntry.COLUMN_NAME_NOTE_ID)
        var selection =
            "${Contract.NoteHasChipsEntry.COLUMN_NAME_CHIP_ID} IN (${chipIds.map { "?" }.joinToString(", ")})"
        val selectionArgs = chipIds.map { it.toString() }.toTypedArray()
        val cursor = openDatabaseHelper.writableDatabase.query(
            true,
            Contract.NoteHasChipsEntry.TABLE_NAME,
            projections,
            selection,
            selectionArgs,
            null,
            null,
            null,
            null
        )
        val noteIds = ArrayList<Int>()
        if (cursor.moveToFirst()) {
            do {
                with(cursor) {
                    noteIds.add(getInt(getColumnIndex(Contract.NoteHasChipsEntry.COLUMN_NAME_NOTE_ID)))
                }
            } while (cursor.moveToNext())
        }

        return noteIds
    }
}