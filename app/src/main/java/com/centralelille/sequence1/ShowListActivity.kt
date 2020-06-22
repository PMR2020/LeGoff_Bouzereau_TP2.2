package com.centralelille.sequence1

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.centralelille.sequence1.adapters.TaskAdapter
import com.centralelille.sequence1.data.ItemToDo
import com.centralelille.sequence1.data.ListeToDo
import com.centralelille.sequence1.data.ProfilListeToDo
import com.google.gson.Gson

class ShowListActivity : AppCompatActivity(), View.OnClickListener, TaskAdapter.OnItemListener{

    private val adapter = newAdapter()

    private lateinit var refOkBtn: Button
    private lateinit var refTxtNewItem: EditText
    private lateinit var listOfTask: RecyclerView
    private lateinit var refCheckBox: CheckBox

    private lateinit var prefs: SharedPreferences
    private lateinit var prefsTasks: SharedPreferences

    private lateinit var pseudoRecu: String
    private lateinit var listeRecue: String
    private lateinit var listeItemToDo: ArrayList<ItemToDo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)

        val bundleData: Bundle = this.getIntent().getExtras()

        pseudoRecu = bundleData.getString("pseudo")
        listeRecue = bundleData.getString("titre")

        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefsTasks = getSharedPreferences("DATA", 0)

        refOkBtn = findViewById(R.id.buttonNewItem)
        listOfTask = findViewById(R.id.listOfItem)
        refTxtNewItem = findViewById(R.id.editTextItem)
        //refCheckBox = findViewById(R.id.checkBox)

        listeItemToDo = getItems(pseudoRecu,listeRecue)
        val dataSet: MutableList<ItemToDo> = mutableListOf()

        repeat(listeItemToDo.size) {
            dataSet.add(listeItemToDo[it])
        }

        adapter.showData(listeItemToDo)
        listOfTask.adapter = adapter
        listOfTask.layoutManager = LinearLayoutManager(this)

        // ClickListener associé au boutton pour créer les items
        refOkBtn.setOnClickListener(this)
        Log.i("ShowListActivity", "dataSet : "+dataSet.toString())
    }

    private fun getItems(pseudo: String, titreListe: String): ArrayList<ItemToDo> {
        val liste = prefsTasks.getString(pseudo, "New")
        val gson = Gson()

        var toReturn: ArrayList<ItemToDo> = arrayListOf<ItemToDo>()

        if (liste == "New") {
            // On renvoie une erreur et une liste vide
            Toast.makeText(this, "Création dans une liste inexistante", Toast.LENGTH_LONG).show()
        } else {
            Log.i("ChoixListActivity", "else")
            val currentProfile: ProfilListeToDo = gson.fromJson(liste, ProfilListeToDo::class.java)
            for(liste in currentProfile.listesToDo) {
                if (liste.titreListeToDo == titreListe) {
                    toReturn = liste.itemsToDo
                }
            }
        }
        Log.i("ChoixListActivity", "getItems fini")
        Log.i("ChoixListActivity", toReturn.toString())
        return(toReturn)
    }

    override fun onClick(v: View?) {
        val newItemDescription = refTxtNewItem.text.toString()
        val liste: String = prefsTasks.getString(pseudoRecu, "New")
        val gson = Gson()

        val currentProfile: ProfilListeToDo = gson.fromJson(liste, ProfilListeToDo::class.java)

        var currentList: ListeToDo = currentProfile.listesToDo.get(currentProfile.getIdList(listeRecue))

        when (v?.id) {
            R.id.buttonNewItem -> {
                // Par défaut l'item n'est pas fait
                alert(currentList.titreListeToDo)
                val item = ItemToDo(newItemDescription, false)
                currentList.itemsToDo.add(item)
                Log.i("testshowlistact", currentList.toString())
                Log.i("testshowlistact", "Items " + currentList.itemsToDo.toString())
            }
        }
        val newProfileJSON: String = gson.toJson(currentProfile)

        val editor: SharedPreferences.Editor = prefsTasks.edit()
        //editor.clear()
        editor.putString(pseudoRecu, newProfileJSON)
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