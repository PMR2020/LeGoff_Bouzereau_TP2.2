package com.centralelille.sequence1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.centralelille.sequence1.adapters.ListeAdapter
import com.centralelille.sequence1.dataAPI.DataProvider
import com.centralelille.sequence1.dataAPI.model.Lists
import com.google.gson.Gson
import kotlinx.coroutines.*

class ChoixListActivity : AppCompatActivity(), View.OnClickListener, ListeAdapter.OnListListener {

    private val adapter = newAdapter()

    private lateinit var refOkBtn: Button
    private lateinit var refTxtNewList: EditText
    private lateinit var refRecycler: RecyclerView

    private lateinit var prefs: SharedPreferences
    private lateinit var prefsListes: SharedPreferences

    private lateinit var pseudoRecu: String
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
        setContentView(R.layout.activity_choix_list)


        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefsListes = getSharedPreferences("DATA", 0)
        pseudoRecu = prefs.getString("pseudo_prefs","")
        currentUserHash = prefs.getString("currentUserHash","no User")

        refOkBtn = findViewById(R.id.buttonNewList)
        refRecycler = findViewById(R.id.listOfList)
        refTxtNewList = findViewById(R.id.editText)

        activityScope.launch {
            val response = DataProvider.getCurrentUserLists(currentUserHash)
            if (response.success){
                adapter.showData(response.lists)
            }
        }
        refRecycler.adapter = adapter
        refRecycler.layoutManager = LinearLayoutManager(this)

        // Click listener associé au boutton pour créer les listes et les sauvergarder
        refOkBtn.setOnClickListener(this)
        Log.i("testRetrofit", prefs.getString("currentUserHash","no User"))

    }


    override fun onClick(v: View?) {
        val newListTitle = refTxtNewList.text.toString()
        val hash =  prefs.getString("currentUserHash","no User")


        when (v?.id) {
            R.id.buttonNewList -> {
                activityScope.launch {
                    val response = DataProvider.insertListForCurrentUser(newListTitle, hash)
                    if (response.success){
                        startActivity( Intent(this@ChoixListActivity, ChoixListActivity::class.java))
                    }
                }
            }
        }
    }

    private fun newAdapter(): ListeAdapter {
        return ListeAdapter(onListListener = this)
    }

    /**
     * Quand une liste est cliquée on change d'activité pout aller sur l'activité ShowListActivity
     *
     * @param liste
     */
    override fun onListClicked(liste: Lists) {
        Log.d("ChoixListActivity", "onListClicked $liste")
        Toast.makeText(this, liste.label, Toast.LENGTH_LONG).show()


        val titreListe = liste.label
        val bundleTitre = Bundle()
        bundleTitre.putString("titre", titreListe)

        val editor: SharedPreferences.Editor = prefs.edit()
        editor.putString("currentListId", liste.id.toString())
        editor.apply()

        val afficherShowListActivity = Intent(this, ShowListActivity::class.java)
        afficherShowListActivity.putExtras(bundleTitre)
        startActivity(afficherShowListActivity)
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK) {
            startActivity(Intent(this, MainActivity::class.java))
            false
        } else super.onKeyDown(keyCode, event)
    }
}
