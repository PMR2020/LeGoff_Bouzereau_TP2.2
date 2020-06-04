package com.centralelille.sequence1

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.centralelille.sequence1.adapters.TaskAdapter
import com.centralelille.sequence1.data.ItemToDo
import com.centralelille.sequence1.data.ListeToDo
import com.google.gson.Gson

class ShowListActivity : AppCompatActivity(), View.OnClickListener, TaskAdapter.OnItemListener {

    private val adapter = newAdapter()

    private lateinit var refOkBtn: Button
    private lateinit var refTxtNewItem: EditText
    private lateinit var listOfTask: RecyclerView
    private lateinit var refCheckBox: CheckBox

    private lateinit var prefs: SharedPreferences
    private lateinit var prefsTasks: SharedPreferences

    private lateinit var listeRecue: String
    private lateinit var listeItemToDo: ArrayList<ItemToDo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)

        val bundleListeName: Bundle = this.getIntent().getExtras()

        listeRecue = bundleListeName.getString("")
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefsTasks = getSharedPreferences("", 0)

        refOkBtn = findViewById(R.id.buttonNewItem)
        listOfTask = findViewById(R.id.listOfItem)
        refTxtNewItem = findViewById(R.id.editTextItem)
        refCheckBox = findViewById(R.id.checkBox)

        listeItemToDo = getItems(listeRecue)
        val dataSet: MutableList<ItemToDo> = mutableListOf()

        repeat(listeItemToDo.size) {
            dataSet.add(listeItemToDo[it])
        }

        listOfTask.adapter = adapter
        listOfTask.layoutManager = LinearLayoutManager(this)

        // ClickListener associé au boutton pour créer les items
        refOkBtn.setOnClickListener(this)
    }

    private fun getItems(titreListe: String): ArrayList<ItemToDo> {
        val liste = prefsTasks.getString(titreListe, "New")
        val gson = Gson()
        Log.i("testshowlistact", liste)

        var toReturn = listOf<ItemToDo>() as ArrayList<ItemToDo>
        if (liste == "New") {
            // On renvoie une erreur et une liste vide
            Toast.makeText(this, "Création dans une liste inexistante", Toast.LENGTH_LONG).show()
        } else {
            val currentList: ListeToDo = gson.fromJson(liste, ListeToDo::class.java)
            Log.i(
                "testshowlistact",
                "récupération items ancienne liste" + currentList.toString() + currentList.itemsToDo.toString()
            )
            toReturn = currentList.itemsToDo
        }
        return(toReturn)
    }

    override fun onClick(v: View?) {
        val newItemDescription = refTxtNewItem.text.toString()
        val liste: String = prefsTasks.getString(listeRecue, "New")
        val gson = Gson()

        val currentList: ListeToDo = gson.fromJson(liste, ListeToDo::class.java)

        when (v?.id) {
            R.id.buttonNewItem -> {
                alert(listeRecue)

                // Par défaut l'item n'est pas fait
                val item = ItemToDo(newItemDescription, false)
                currentList.itemsToDo.add(item)
                Log.i("testshowlistact", currentList.toString())
                Log.i("testshowlistact", "Items " + currentList.itemsToDo.toString())
            }
        }
        val newListeJSON: String = gson.toJson(currentList)

        val editor: SharedPreferences.Editor = prefsTasks.edit()
        //editor.clear()
        editor.putString(listeRecue, newListeJSON)
        editor.apply()
    }

    //Gestion deboguage
    private fun alert(s: String) {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, s, duration)
        toast.show()
    }

    private fun newAdapter(): TaskAdapter {
        return TaskAdapter(onItemListener = this)
    }

    /**
     * Quand un item est cliqué l'utilisateur peut l'éditer
     *
     * @param item
     */
    override fun onItemClicked(item: ItemToDo) {
        Toast.makeText(this, item.description, Toast.LENGTH_LONG).show()
        TODO("Not yet implemented")
    }
}