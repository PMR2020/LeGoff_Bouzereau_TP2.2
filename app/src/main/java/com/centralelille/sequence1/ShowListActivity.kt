package com.centralelille.sequence1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.centralelille.sequence1.adapters.TaskAdapter
import com.centralelille.sequence1.dataAPI.DataProvider
import com.centralelille.sequence1.dataAPI.model.Items
import kotlinx.coroutines.*

class ShowListActivity : AppCompatActivity(), View.OnClickListener, TaskAdapter.OnItemListener, TaskAdapter.OnCheckbBoxListener{

    private val adapter = newAdapter()

    private lateinit var refOkBtn: Button
    private lateinit var refTxtNewItem: EditText
    private lateinit var listOfTask: RecyclerView
    private lateinit var refCheckBox: CheckBox

    private lateinit var prefs: SharedPreferences
    private lateinit var prefsTasks: SharedPreferences

    private lateinit var titreListeRecue: String
    private lateinit var listeRecueId: String
    private lateinit var currentUserHash: String


    private val activityScope = CoroutineScope(
        SupervisorJob()
                + Dispatchers.Main
                + CoroutineExceptionHandler { _, throwable ->
            Log.e("MainActivity", "CoroutineExceptionHandler : ${throwable.message}")
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)

        //On met le nom de la liste en titre
        val bundleData: Bundle = this.getIntent().getExtras()
        titreListeRecue = bundleData.getString("titre")
        setTitle(titreListeRecue)

        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefsTasks = getSharedPreferences("DATA", 0)

        refOkBtn = findViewById(R.id.buttonNewItem)
        listOfTask = findViewById(R.id.listOfItem)
        refTxtNewItem = findViewById(R.id.editTextItem)

        activityScope.launch {
            listeRecueId = prefs.getString("currentListId", "no Id")
            currentUserHash = prefs.getString("currentUserHash","no User")

            val response = DataProvider.getItemsFromListById(listeRecueId.toInt(),currentUserHash)
            if (response.success){
                adapter.showData(response.items)
            }

        }

        listOfTask.adapter = adapter
        listOfTask.layoutManager = LinearLayoutManager(this)

        // ClickListener associé au boutton pour créer les items
        refOkBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        val newItemDescription = refTxtNewItem.text.toString()
        val listeId: String = prefs.getString("currentListId", "No Id")
        currentUserHash = prefs.getString("currentUserHash","no User")

        when (v?.id) {
            R.id.buttonNewItem -> {
                activityScope.launch {
                    val response = DataProvider.insertNewItem(listeId.toInt(),newItemDescription,currentUserHash)
                    if (response.success){
                        startActivity( Intent(this@ShowListActivity, ShowListActivity::class.java))
                    }
                }
            }
        }
    }

    private fun newAdapter(): TaskAdapter {
        return TaskAdapter(onItemListener = this, onCheckBoxListener = this)
    }

    /**
     * Quand un item est cliqué l'utilisateur peut l'éditer
     *
     * @param item
     */
    override fun onItemClicked(item: Items) {
        Toast.makeText(this, item.label, Toast.LENGTH_LONG).show()
        TODO("Not yet implemented")
    }

    override fun onCheckBoxClicked(checkBox: CheckBox, item: Items) {
        activityScope.launch {
            listeRecueId = prefs.getString("currentListId", "no Id")
            currentUserHash = prefs.getString("currentUserHash","no User")
            if (checkBox.isChecked) {
                val response = DataProvider.setItemState(listeRecueId.toInt(),item.id,1,currentUserHash)
            }
            else {
                val response = DataProvider.setItemState(listeRecueId.toInt(),item.id,0,currentUserHash)
            }
        }
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(Intent(this, ChoixListActivity::class.java))
            false
        } else super.onKeyDown(keyCode, event)
    }
}