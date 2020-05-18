package com.example.ipr1.database.entries.chip

import android.content.ContentValues
import com.example.ipr1.database.Contract

class ChipModel(var id: Int, var text: String) {
    fun getContentValues(): ContentValues {
        val values = ContentValues()
        values.put(Contract.ChipEntry.COLUMN_NAME_CHIP_TEXT, text)

        return values
    }

    override fun toString(): String = text
}