package com.example.ipr1

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.ipr1.constants.Constants
import com.example.ipr1.database.Commands
import com.example.ipr1.database.DatabaseOpenHelper
import com.example.ipr1.database.models.Note
import kotlinx.android.synthetic.main.activity_add_or_update_note.*

class AddOrUpdateNoteActivity : AppCompatActivity() {
    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_or_update_note)
        try {
            val bundle: Bundle = intent.extras!!
            id = bundle.getInt(Constants.ID, 0)
            if (id != 0) {
                titleEt.setText(bundle.getString(Constants.TITLE))
                descEt.setText(bundle.getString(Constants.DESCRIPTION))
                addBtn.text = "Update"
            }
        } catch (ex: Exception) {
            addBtn.text = "Add"
        }
    }

    fun addFunc(view: View) {
        val idOrCount = Commands.addOrUpdateNote(
            DatabaseOpenHelper(this),
            Note(id, titleEt.text.toString(), descEt.text.toString())
        )

        if(idOrCount > 0) {
            val successMessage = if (id == 0) "Note has been added" else "Note has been updated"
            Toast.makeText(this, successMessage, Toast.LENGTH_SHORT).show()
            finish()
        } else {
            val errorMessage = if (id == 0) "Error adding note..." else "Error updating note"
            Toast.makeText(this, errorMessage, Toast.LENGTH_SHORT).show()
        }
    }
}
