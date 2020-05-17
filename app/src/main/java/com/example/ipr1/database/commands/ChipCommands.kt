package com.example.ipr1.database.commands

import android.database.sqlite.SQLiteOpenHelper
import com.example.ipr1.database.Contract
import com.example.ipr1.database.models.ChipModel

object ChipCommands {
    fun deleteChip(openDatabaseHelper: SQLiteOpenHelper, chipId: Int) {
        openDatabaseHelper.writableDatabase.delete(
            Contract.ChipEntry.TABLE_NAME,
            "${Contract.ChipEntry.COLUMN_NAME_CHIP_ID}=?",
            arrayOf(chipId.toString())
        )
    }

    fun addChip(openDatabaseHelper: SQLiteOpenHelper, chip: ChipModel) =
        openDatabaseHelper.writableDatabase.insert(Contract.ChipEntry.TABLE_NAME, null, chip.getContentValues())
}