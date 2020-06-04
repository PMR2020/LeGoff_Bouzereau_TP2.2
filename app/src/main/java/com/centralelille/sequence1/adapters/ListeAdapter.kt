package com.centralelille.sequence1.adapters;

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.centralelille.sequence1.R
import com.centralelille.sequence1.data.ListeToDo

class ListeAdapter(private val onListListener: OnListListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataSet: MutableList<ListeToDo> = mutableListOf()

    override fun getItemCount(): Int = dataSet.size

    fun showData(newDataSet: List<ListeToDo>) {
        dataSet.clear()
        dataSet.addAll(newDataSet)
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ListViewHolder(inflater.inflate(R.layout.liste, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d("ItemAdapter", "onBindViewHolder $position")
        when (holder) {
            is ListViewHolder -> holder.bind(dataSet[position])
        }
    }

    inner class ListViewHolder(listView: View) : RecyclerView.ViewHolder(listView) {
        private val title: TextView = listView.findViewById(R.id.title)

        init {
            listView.setOnClickListener {
                val listPosition = adapterPosition
                if (listPosition != RecyclerView.NO_POSITION) {
                    val clickedList = dataSet[listPosition]
                    onListListener.onListClicked(clickedList)
                }
            }

        }

        fun bind(list: ListeToDo) {
            title.text = list.titreListeToDo
        }
    }

    interface OnListListener {
        fun onListClicked(list: ListeToDo)
    }
}
