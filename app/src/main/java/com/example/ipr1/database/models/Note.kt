package com.example.ipr1.database.models

import android.content.ContentValues
import com.example.ipr1.database.Contract

class Note(var id: Int, var title: String, var description: String) {
    fun getContentValues(): ContentValues {
        val values = ContentValues()
        values.put(Contract.NoteEntry.COLUMN_NAME_TITLE, title)
        values.put(Contract.NoteEntry.COLUMN_NAME_DESCRIPTION, description)

        return values
    }
}
