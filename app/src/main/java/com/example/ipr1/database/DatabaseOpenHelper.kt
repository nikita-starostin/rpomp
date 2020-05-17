package com.example.ipr1.database

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

class DatabaseOpenHelper(context: Context) : SQLiteOpenHelper(
    context,
    Contract.DATABASE_NAME, null,
    Contract.DATABASE_VERSION
) {
    override fun onCreate(database: SQLiteDatabase?) {
        database!!.execSQL(Contract.NoteEntry.SQL_CREATE_TABLE)
    }

    override fun onUpgrade(
        db: SQLiteDatabase?, oldVersion: Int, newVersion:
        Int
    ) {
        db!!.execSQL(Contract.NoteEntry.SQL_DROP_TABLE)
    }
}
