package com.example.ipr1

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.SearchView
import com.example.ipr1.adapters.NoteListItemAdapter
import com.example.ipr1.constants.Constants
import com.example.ipr1.database.DatabaseOpenHelper
import com.example.ipr1.database.entries.note.NoteCommands
import com.example.ipr1.database.entries.note.NoteModel
import com.example.ipr1.database.entries.note.NoteQueries
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {
    private lateinit var listNotes: ArrayList<NoteModel>
    private lateinit var dbOpenHelper: DatabaseOpenHelper
    private lateinit var autocompleteState: AutocompleteState
    private var filterTitle: String = "%"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        this.dbOpenHelper = DatabaseOpenHelper(this)
        autocompleteState = AutocompleteState(this,
            dbOpenHelper,
            chipInput,
            chipsGroup,
            mutableListOf(),
            { syncFromDatabase() })
        syncFromDatabase()
    }

    override fun onResume() {
        super.onResume()
        syncFromDatabase()
    }

    private fun syncFromDatabase() {
        this.listNotes = NoteQueries.queryNotes(this.dbOpenHelper, filterTitle, ArrayList(autocompleteState.getSelectedChips()))
        notesLv.adapter =
            NoteListItemAdapter(this, R.layout.row, listNotes)
        if (supportActionBar != null) {
            (supportActionBar as ActionBar).subtitle = "You have ${notesLv.count} note(s) in list..."
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        val searchView = menu!!.findItem(R.id.app_bar_search).actionView as
                SearchView
        val searchManager = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        searchView.setSearchableInfo(searchManager.getSearchableInfo(componentName))
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                filterTitle = "%$query%"
                syncFromDatabase()
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterTitle = "%$newText%"
                syncFromDatabase()
                return false
            }
        })
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        if (item != null) {
            when (item.itemId) {
                R.id.addNote -> {
                    startAddNote()
                }
            }
        }
        return super.onOptionsItemSelected(item!!)
    }

    fun startAddNote() {
        startActivity(Intent(this, AddOrUpdateNoteActivity::class.java))
    }

    fun deleteNote(note: NoteModel) {
        NoteCommands.deleteNote(dbOpenHelper, note.id!!)
        syncFromDatabase()
    }

    fun startEditNote(note: NoteModel) {
        val intent = Intent(this, AddOrUpdateNoteActivity::class.java)
        intent.putExtra(Constants.ID, note.id)
        intent.putExtra(Constants.TITLE, note.title)
        intent.putExtra(Constants.DESCRIPTION, note.description)
        startActivity(intent)
    }
}