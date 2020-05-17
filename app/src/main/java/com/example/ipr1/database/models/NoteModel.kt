package com.example.ipr1.database.models

import android.content.ContentValues
import com.example.ipr1.database.Contract
import com.google.android.material.chip.Chip

class NoteModel(var id: Int, var title: String, var description: String, var chips: ArrayList<Chip>) {
    fun getContentValues(): ContentValues {
        val values = ContentValues()
        values.put(Contract.NoteEntry.COLUMN_NAME_TITLE, title)
        values.put(Contract.NoteEntry.COLUMN_NAME_DESCRIPTION, description)

        return values
    }
}
