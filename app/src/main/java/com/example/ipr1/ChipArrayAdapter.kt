package com.example.ipr1

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Filter
import com.example.ipr1.database.models.ChipModel
import kotlinx.android.synthetic.main.chip_autocomplete_item.view.*
import java.util.*


class ChipArrayAdapter(
    private val onClick: (chip: ChipModel, position: Int, selectedChips: MutableList<ChipModel>) -> Unit,
    context: Context,
    private val layoutResId: Int,
    private val mOriginalValues: MutableList<ChipModel>
) :
    ArrayAdapter<ChipModel>(context, layoutResId, mOriginalValues) {

    private val immutableOriginalValues = ArrayList(mOriginalValues)
    private val mFilter = ArrayFilter()
    private val mLock = Object()

    private val selectedChips = mutableListOf<ChipModel>()

    fun removeChipFromSelected(chipText: String) {
        with(selectedChips) {
            removeAt(indexOfFirst { it.text == chipText })
        }
    }

    override fun getView(
        position: Int, convertView: View?, parent: ViewGroup
    ): View {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val rowView = inflater.inflate(layoutResId, parent, false)
        val chip = getItem(position)!!
        rowView.chipText.text = chip.text
        rowView.setOnClickListener { onClick(chip, position, selectedChips) }

        return rowView
    }

    override fun getFilter(): Filter = mFilter!!

    private inner class ArrayFilter : Filter() {
        override fun performFiltering(prefix: CharSequence?): FilterResults {
            val results = FilterResults()
            if (prefix == null || prefix.isEmpty()) {
                results.values = immutableOriginalValues
                results.count = immutableOriginalValues.size
            } else {
                val prefixString = prefix.toString().toLowerCase()
                val count = immutableOriginalValues.size
                val newValues: ArrayList<ChipModel> = ArrayList()
                for (i in 0 until count) {
                    val value: ChipModel = immutableOriginalValues[i]
                    val valueText: String = value.toString().toLowerCase()
                    if (valueText.startsWith(prefixString)) {
                        newValues.add(value)
                    }
                }
                results.values = newValues
                results.count = newValues.size
            }
            return results
        }

        override fun publishResults(constraint: CharSequence?, results: FilterResults) {
            if (results.count > 0) {
                clear()
                val selectedItems = selectedChips.map { it.text }
                results.values = (results.values as List<ChipModel>)
                    .filter { chipModel -> !selectedItems.any { it.toLowerCase() == chipModel.text.toLowerCase() } }
                addAll(results.values as List<ChipModel>)
                notifyDataSetChanged()
            } else {
                notifyDataSetInvalidated()
            }
        }
    }
}