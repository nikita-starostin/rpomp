package com.example.ipr1.database.entries.chip

import android.database.sqlite.SQLiteOpenHelper
import com.example.ipr1.database.Contract

object ChipQueries {

    fun queryChips(openDatabaseHelper: SQLiteOpenHelper, text: String = "%"): ArrayList<ChipModel> =
        getChipsFromCursor(
            openDatabaseHelper,
            "${Contract.ChipEntry.COLUMN_NAME_CHIP_TEXT} like ?",
            arrayOf(text)
        )

    fun queryChipsForNote(openDatabaseHelper: SQLiteOpenHelper, noteId: Int): ArrayList<ChipModel> {
        val chipIds = queryChipIdsForNote(openDatabaseHelper, noteId)
        return getChipsFromCursor(
            openDatabaseHelper,
            "${Contract.ChipEntry.COLUMN_NAME_CHIP_ID} in (${chipIds.map { "?" }.joinToString(", ")})",
            chipIds.map { it.toString() }.toTypedArray()
        )
    }

    private fun queryChipIdsForNote(openDatabaseHelper: SQLiteOpenHelper, noteId: Int): ArrayList<Int> {
        val projections = arrayOf(Contract.NoteHasChipsEntry.COLUMN_NAME_CHIP_ID)
        val selectionArgs = arrayOf(noteId.toString())
        val cursor = openDatabaseHelper.writableDatabase.query(
            Contract.NoteHasChipsEntry.TABLE_NAME,
            projections,
            "${Contract.NoteHasChipsEntry.COLUMN_NAME_NOTE_ID} = ?",
            selectionArgs,
            null,
            null,
            null,
            null
        )
        val chipIds = ArrayList<Int>()
        if (cursor.moveToFirst()) {
            do {
                with(cursor) {
                    chipIds.add(getInt(getColumnIndex(Contract.NoteHasChipsEntry.COLUMN_NAME_CHIP_ID)))
                }
            } while (cursor.moveToNext())
        }

        return chipIds
    }

    private fun getChipsFromCursor(
        openDatabaseHelper: SQLiteOpenHelper,
        selection: String,
        selectionArgs: Array<String>
    ): ArrayList<ChipModel> {
        val chips = ArrayList<ChipModel>()
        val cursor = openDatabaseHelper.writableDatabase.query(
            Contract.ChipEntry.TABLE_NAME,
            getChipColumnProjections(), selection, selectionArgs,
            null,
            null,
            Contract.ChipEntry.COLUMN_NAME_CHIP_TEXT
        )
        if (cursor.moveToFirst()) {
            do {
                val chipId = cursor.getInt(cursor.getColumnIndex(Contract.ChipEntry.COLUMN_NAME_CHIP_ID))
                val chipText = cursor.getString(cursor.getColumnIndex(Contract.ChipEntry.COLUMN_NAME_CHIP_TEXT))
                chips.add(ChipModel(chipId, chipText))
            } while (cursor.moveToNext())
        }

        return chips
    }

    private fun getChipColumnProjections(): Array<String> {
        return arrayOf(
            Contract.ChipEntry.COLUMN_NAME_CHIP_ID,
            Contract.ChipEntry.COLUMN_NAME_CHIP_TEXT
        )
    }

}