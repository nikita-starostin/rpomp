package com.example.ipr1.database

import android.database.sqlite.SQLiteOpenHelper
import com.example.ipr1.database.models.ChipModel
import com.example.ipr1.database.models.NoteModel

object Queries {
    fun queryNotes(openDatabaseHelper: SQLiteOpenHelper, title: String): ArrayList<NoteModel> {
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
        val notes = ArrayList<NoteModel>()
        if (cursor.moveToFirst()) {
            do {
                val noteId = cursor.getInt(cursor.getColumnIndex(Contract.NoteEntry.COLUMN_NAME_NOTE_ID))
                val noteTitle = cursor.getString(cursor.getColumnIndex(Contract.NoteEntry.COLUMN_NAME_TITLE))
                val noteDescription =
                    cursor.getString(cursor.getColumnIndex(Contract.NoteEntry.COLUMN_NAME_DESCRIPTION))
                notes.add(NoteModel(noteId, noteTitle, noteDescription))
            } while (cursor.moveToNext())
        }

        return notes
    }

    fun queryChips(openDatabaseHelper: SQLiteOpenHelper, text: String = "%"): ArrayList<ChipModel> {
        val projections =
            arrayOf(Contract.ChipEntry.COLUMN_NAME_CHIP_ID, Contract.ChipEntry.COLUMN_NAME_CHIP_TEXT)
        val selectionArgs = arrayOf(text)
        val cursor = openDatabaseHelper.writableDatabase.query(
            Contract.ChipEntry.TABLE_NAME,
            projections, "${Contract.ChipEntry.COLUMN_NAME_CHIP_TEXT} like ?", selectionArgs,
            null,
            null,
            Contract.ChipEntry.COLUMN_NAME_CHIP_TEXT
        )
        val chips = ArrayList<ChipModel>()
        if (cursor.moveToFirst()) {
            do {
                val chipId = cursor.getInt(cursor.getColumnIndex(Contract.ChipEntry.COLUMN_NAME_CHIP_ID))
                val chipText = cursor.getString(cursor.getColumnIndex(Contract.ChipEntry.COLUMN_NAME_CHIP_TEXT))
                chips.add(ChipModel(chipId, chipText))
            } while (cursor.moveToNext())
        }

        return chips
    }
}