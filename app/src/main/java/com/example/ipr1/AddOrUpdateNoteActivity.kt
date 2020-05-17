package com.example.ipr1

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.example.ipr1.constants.Constants
import com.example.ipr1.database.DatabaseOpenHelper
import com.example.ipr1.database.commands.NoteCommands
import com.example.ipr1.database.models.ChipModel
import com.example.ipr1.database.models.NoteModel
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip
import kotlinx.android.synthetic.main.activity_add_or_update_note.*

class AddOrUpdateNoteActivity : AppCompatActivity() {
    var id = 0
    private lateinit var chipListAdapter: ChipArrayAdapter
    private val databaseOpenHelper = DatabaseOpenHelper(this)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_or_update_note)
        try {
            val bundle: Bundle = intent.extras!!
            id = bundle.getInt(Constants.ID, 0)
            if (id != 0) {
                titleEt.setText(bundle.getString(Constants.TITLE))
                descEt.setText(bundle.getString(Constants.DESCRIPTION))
                submitBtn.text = resources.getString(R.string.update)
            }
        } catch (ex: Exception) {
            submitBtn.text = resources.getString(R.string.add)
        }
        addNewChip("sport", chipsGroup)
        addNewChip("music", chipsGroup)
        chipListAdapter = ChipArrayAdapter(
            { chipModel, _, selectedChips ->
                addNewChip(chipModel.text, chipsGroup)
                selectedChips.add(chipModel)
                chipInput.dismissDropDown()
                chipInput.setText("")
            }, this,
            R.layout.chip_autocomplete_item,
            mutableListOf(
                ChipModel(1, "Hello"),
                ChipModel(2, "World")
            )
        )
        chipInput.setAdapter(chipListAdapter)
        chipInput.setOnClickListener {
            chipInput.showDropDown()
        }
    }

    fun submit(view: View) {
        val idOrCount = NoteCommands.addOrUpdateNote(
            databaseOpenHelper,
            NoteModel(id, titleEt.text.toString(), descEt.text.toString())
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

    private fun addNewChip(chipText: String, chipGroup: FlexboxLayout) {
        val chip = Chip(this)
        chip.text = chipText
        chip.chipIcon = ContextCompat.getDrawable(this, R.mipmap.ic_launcher_round)
        chip.isCloseIconEnabled = true
        chip.isClickable = true
        chip.isCheckable = false
        chipGroup.addView(chip as View, chipGroup.childCount - 1)
        chip.setOnCloseIconClickListener {
            chipListAdapter.removeChipFromSelected(chipText)
            chipGroup.removeView(chip as View)
        }
    }
}
