package com.example.ipr1

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ipr1.constants.Constants
import com.example.ipr1.database.DatabaseOpenHelper
import com.example.ipr1.database.entries.chip.ChipModel
import com.example.ipr1.database.entries.chip.ChipQueries
import com.example.ipr1.database.entries.note.NoteCommands
import com.example.ipr1.database.entries.note.NoteModel
import kotlinx.android.synthetic.main.activity_add_or_update_note.*

class AddOrUpdateNoteActivity : AppCompatActivity() {
    var id = 0
    private val databaseOpenHelper = DatabaseOpenHelper(this)
    private lateinit var autocompleteState: AutocompleteState

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_or_update_note)
        val selectedChips = mutableListOf<ChipModel>()
        try {
            val bundle: Bundle = intent.extras!!
            id = bundle.getInt(Constants.ID, 0)
            if (id != 0) {
                titleEt.setText(bundle.getString(Constants.TITLE))
                descEt.setText(bundle.getString(Constants.DESCRIPTION))
                submitBtn.text = resources.getString(R.string.update)
                selectedChips.addAll(ChipQueries.queryChipsForNote(databaseOpenHelper, id))
            }
        } catch (ex: Exception) {
            submitBtn.text = resources.getString(R.string.add)
        }
        autocompleteState = AutocompleteState(this, databaseOpenHelper, chipInput, chipsGroup, selectedChips)
    }

    fun submit(view: View) {
        val idOrCount = NoteCommands.addOrUpdateNote(
            databaseOpenHelper,
            NoteModel(
                id,
                titleEt.text.toString(),
                descEt.text.toString(),
                ArrayList(autocompleteState.getSelectedChips())
            )
        )

        if (idOrCount > 0) {
            val successMessage = if (id == 0) "Note has been added" else "Note has been updated"
            Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show()
            finish()
        } else {
            val errorMessage = if (id == 0) "Error adding note..." else "Error updating note"
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }

    fun cancel(view: View) {
        finish()
    }
}
