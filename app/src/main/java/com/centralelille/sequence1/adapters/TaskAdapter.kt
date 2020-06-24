package com.centralelille.sequence1.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.CompoundButton
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.centralelille.sequence1.R
import com.centralelille.sequence1.dataAPI.model.Items
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

private val activityScope = CoroutineScope(
    SupervisorJob()
            + Dispatchers.Main
            + CoroutineExceptionHandler { _, throwable ->
        Log.e("MainActivity", "CoroutineExceptionHandler : ${throwable.message}")
    }
)

class TaskAdapter(private val onItemListener: OnItemListener, private val onCheckBoxListener: OnCheckbBoxListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataSet: MutableList<Items> = mutableListOf()

    override fun getItemCount(): Int = dataSet.size

    fun showData(newDataSet: List<Items>) {
        dataSet.clear()
        dataSet.addAll(newDataSet)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ItemViewHolder(inflater.inflate(R.layout.item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("ItemAdapter", "onBindViewHolder $position")
        when (holder) {
            is ItemViewHolder -> holder.bind(dataSet[position])
        }
    }

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val title: TextView = itemView.findViewById(R.id.title)
        private val checkButton: CheckBox = itemView.findViewById(R.id.checkBox)

        init {
            itemView.setOnClickListener {
                val itemPosition = adapterPosition
                if (itemPosition != RecyclerView.NO_POSITION) {
                    val clickedItem = dataSet[itemPosition]
                    onItemListener.onItemClicked(clickedItem)
                }
            }
            checkButton.setOnClickListener{
                val itemPosition = adapterPosition
                if (itemPosition != RecyclerView.NO_POSITION) {
                    val clickedItem = dataSet[itemPosition]
                    onCheckBoxListener.onCheckBoxClicked(checkButton,clickedItem)
                }

            }
        }

        fun bind(item: Items) {
            title.text = item.label
            checkButton.isChecked = (item.checked==1)
        }
    }

    interface OnItemListener {
        fun onItemClicked(item: Items)
    }

    interface OnCheckbBoxListener {
        fun onCheckBoxClicked(checkBox: CheckBox, item: Items)
    }

}