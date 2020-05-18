package com.example.ipr1.database

object Contract {
    const val DATABASE_NAME = "ipr1Notes"
    const val DATABASE_VERSION = 1

    object NoteEntry {
        const val TABLE_NAME = "Notes"
        const val COLUMN_NAME_NOTE_ID = "noteID"
        const val COLUMN_NAME_TITLE = "title"
        const val COLUMN_NAME_DESCRIPTION = "description"

        const val SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($COLUMN_NAME_NOTE_ID INTEGER PRIMARY KEY, $COLUMN_NAME_TITLE TEXT, $COLUMN_NAME_DESCRIPTION TEXT);"
        const val SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS $TABLE_NAME;"
    }

    object ChipEntry {
        const val TABLE_NAME = "Chips"
        const val COLUMN_NAME_CHIP_ID = "chipId"
        const val COLUMN_NAME_CHIP_TEXT = "chipText"

        const val SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($COLUMN_NAME_CHIP_ID INTEGER PRIMARY KEY, $COLUMN_NAME_CHIP_TEXT TEXT);"
        const val SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS $TABLE_NAME;"

        private fun getSqlAddChipIfNotExists(chipText: String) =
            "INSERT INTO $TABLE_NAME($COLUMN_NAME_CHIP_TEXT) SELECT '$chipText' WHERE NOT EXISTS(SELECT $COLUMN_NAME_CHIP_TEXT FROM $TABLE_NAME WHERE $COLUMN_NAME_CHIP_TEXT = '$chipText');"

        fun getSqlFotGeneratingSeed() = arrayOf("sport", "music")
            .map { getSqlAddChipIfNotExists(it) }
    }

    object NoteHasChipsEntry {
        const val TABLE_NAME = "Note_Has_Chips"
        const val COLUMN_NAME_CHIP_ID = ChipEntry.COLUMN_NAME_CHIP_ID
        const val COLUMN_NAME_NOTE_ID = NoteEntry.COLUMN_NAME_NOTE_ID

        const val SQL_CREATE_TABLE =
            "CREATE TABLE IF NOT EXISTS $TABLE_NAME ($COLUMN_NAME_CHIP_ID INTEGER, $COLUMN_NAME_NOTE_ID INTEGER);"
        const val SQL_DROP_TABLE =
            "DROP TABLE IF EXISTS $TABLE_NAME"
    }
}