package com.example.ipr1

import android.content.Context
import android.database.sqlite.SQLiteOpenHelper
import android.view.View
import android.widget.AutoCompleteTextView
import androidx.core.content.ContextCompat
import com.example.ipr1.adapters.ChipArrayAdapter
import com.example.ipr1.database.entries.chip.ChipModel
import com.example.ipr1.database.entries.chip.ChipQueries
import com.google.android.flexbox.FlexboxLayout
import com.google.android.material.chip.Chip

class AutocompleteState(
    private val context: Context,
    databaseOpenHelper: SQLiteOpenHelper,
    private val chipInput: AutoCompleteTextView,
    private val chipsGroup: FlexboxLayout,
    initialSelectedChips: MutableList<ChipModel>,
    private val onSelectedItemsChanged: (selectedItems: MutableList<ChipModel>) -> Unit = { }
) {
    private val chipListAdapter = ChipArrayAdapter(
        { chipModel, _, selectedChips ->
            addNewChip(chipModel.text, chipsGroup)
            selectedChips.add(chipModel)
            chipInput.dismissDropDown()
            chipInput.setText("")
            onSelectedItemsChanged(selectedChips)
        },
        context,
        R.layout.chip_autocomplete_item,
        ChipQueries.queryChips(databaseOpenHelper).toMutableList(),
        initialSelectedChips
    )
    init {
        chipInput.setAdapter(chipListAdapter)
        chipInput.setOnClickListener {
            chipInput.showDropDown()
        }
        initialSelectedChips.forEach { addNewChip(it.text, chipsGroup) }
    }

    fun getSelectedChips() = chipListAdapter.getSelectedChips()

    private fun addNewChip(chipText: String, chipGroup: FlexboxLayout) {
        val chip = Chip(context)
        chip.text = chipText
        chip.chipIcon = ContextCompat.getDrawable(context, R.mipmap.ic_launcher_round)
        chip.isCloseIconEnabled = true
        chip.isClickable = true
        chip.isCheckable = false
        chipGroup.addView(chip as View, chipGroup.childCount - 1)
        chip.setOnCloseIconClickListener {
            chipListAdapter.removeChipFromSelected(chipText)
            onSelectedItemsChanged(getSelectedChips())
            chipGroup.removeView(chip as View)
        }
    }
}