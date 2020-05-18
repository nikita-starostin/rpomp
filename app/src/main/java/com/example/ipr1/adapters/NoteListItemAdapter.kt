package com.example.ipr1.adapters

import android.content.Intent
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.example.ipr1.MainActivity
import com.example.ipr1.database.entries.note.NoteModel
import com.example.ipr1.helpers.Helpers
import kotlinx.android.synthetic.main.row.view.*

class NoteListItemAdapter(private val mainActivity: MainActivity, private val layoutRes: Int, objects: List<NoteModel>) :
    ArrayAdapter<NoteModel>(mainActivity, layoutRes, objects) {

    override fun getView(
        position: Int, convertView: View?, parent: ViewGroup
    ): View {
        val view = mainActivity.layoutInflater.inflate(layoutRes, parent, false)
        val note = getItem(position)!!
        view.titleTv.text = note.title
        view.descTv.text = note.description
        view.deleteBtn.setOnClickListener { mainActivity.deleteNote(note) }
        view.setOnClickListener { mainActivity.startEditNote(note) }
        view.copyBtn.setOnClickListener {
            Helpers.copyTextToClipboard(
                mainActivity,
                view.titleTv.text.toString() + "\n" + view.descTv.text.toString()
            )
        }
        view.shareBtn.setOnClickListener {
            val noteText = view.titleTv.text.toString() + "\n" + view.descTv.text.toString()
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.type = "text/plain"
            shareIntent.putExtra(Intent.EXTRA_TEXT, noteText)
            mainActivity.startActivity(Intent.createChooser(shareIntent, noteText))
        }
        return view
    }
}
