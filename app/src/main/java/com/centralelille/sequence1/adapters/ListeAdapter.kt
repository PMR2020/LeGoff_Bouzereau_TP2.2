package com.centralelille.sequence1.adapters;

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.centralelille.sequence1.R
import com.centralelille.sequence1.dataAPI.model.Lists

/**
 * Un adapter a besoin d'un ViewHolder
 *
 * @property onListListener
 */
class ListeAdapter(private val onListListener: OnListListener) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val dataSet: MutableList<Lists> = mutableListOf()

    // 1. Appelée par le RecyclerView pour avoir une idée du nombre d'items à afficher
    override fun getItemCount(): Int = dataSet.size

    fun showData(newDataSet: List<Lists>) {
        dataSet.clear()
        dataSet.addAll(newDataSet)
        // Should refresh the view
        notifyDataSetChanged()
    }

    // 2. Le RecyclerView demande ensuite la vue dans une certaine position
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return ListViewHolder(inflater.inflate(R.layout.liste, parent, false))
    }

    // 3. Pour mettre les données dans le ViewHolder
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

        fun bind(list: Lists) {
            title.text = list.label
        }
    }

    interface OnListListener {
        fun onListClicked(list: Lists)
    }
}
