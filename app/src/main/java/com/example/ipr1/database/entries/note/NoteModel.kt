package com.example.ipr1.database.entries.note

import android.content.ContentValues
import com.example.ipr1.database.Contract
import com.example.ipr1.database.entries.chip.ChipModel

class NoteModel(var id: Int, var title: String, var description: String, var chips: ArrayList<ChipModel>) {
    fun getContentValues(): ContentValues {
        val values = ContentValues()
        values.put(Contract.NoteEntry.COLUMN_NAME_TITLE, title)
        values.put(Contract.NoteEntry.COLUMN_NAME_DESCRIPTION, description)

        return values
    }
}
