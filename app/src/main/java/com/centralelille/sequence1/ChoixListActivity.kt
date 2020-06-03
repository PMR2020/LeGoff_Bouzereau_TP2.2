package com.centralelille.sequence1

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class ChoixListActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var refOkBtn: Button
    private lateinit var prefs: SharedPreferences
    private lateinit var listOfList: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)
        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        refOkBtn = findViewById(R.id.buttonNewList)
        listOfList = findViewById(R.id.listOfList)
        val dataSet : MutableList<String> = mutableListOf()

        repeat(100) {
            dataSet.add("Liste $it")
        }

        val adapter = ItemAdapter(dataSet)
        listOfList.adapter = adapter
        listOfList.layoutManager = LinearLayoutManager(this)

        refOkBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val bundlePseudo: Bundle = this.getIntent().getExtras()
        val pseudo = bundlePseudo.getString("pseudo")
        when (v?.id) {
            R.id.buttonNewList -> alert(pseudo)
        }
    }

    //Gestion deboguage
    fun alert(s: String) {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, s, duration)
        toast.show()
    }

    class ItemAdapter(val dataSet: List<String>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun getItemCount(): Int = dataSet.size

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val inflater: LayoutInflater = LayoutInflater.from(parent.context)
            val itemView = inflater.inflate(R.layout.liste, parent, false)
            return ItemViewHolder(itemView)
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            val title: String = dataSet[position]
            (holder as ItemViewHolder).bind(title)
        }

        class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
            private val textView: TextView = itemView.findViewById(R.id.title)

            fun bind(title: String) {
                textView.text = title
            }
        }
    }
}
