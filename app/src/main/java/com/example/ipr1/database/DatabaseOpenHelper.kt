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
        database.execSQL(Contract.ChipEntry.SQL_CREATE_TABLE)
        database.execSQL(Contract.NoteHasChipsEntry.SQL_CREATE_TABLE)
        Contract.ChipEntry.getSqlFotGeneratingSeed().forEach { database.execSQL(it) }
    }

    override fun onUpgrade(
        db: SQLiteDatabase?, oldVersion: Int, newVersion:
        Int
    ) {
        db!!.execSQL(Contract.NoteEntry.SQL_DROP_TABLE)
        db.execSQL(Contract.ChipEntry.SQL_DROP_TABLE)
        db.execSQL(Contract.NoteHasChipsEntry.SQL_DROP_TABLE)
    }

    override fun onDowngrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db!!.execSQL(Contract.NoteEntry.SQL_DROP_TABLE)
        db.execSQL(Contract.ChipEntry.SQL_DROP_TABLE)
        db.execSQL(Contract.NoteHasChipsEntry.SQL_DROP_TABLE)
    }
}
