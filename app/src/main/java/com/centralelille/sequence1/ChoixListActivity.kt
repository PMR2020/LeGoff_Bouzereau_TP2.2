package com.centralelille.sequence1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.centralelille.sequence1.adapters.ListeAdapter
import com.centralelille.sequence1.data.ListeToDo
import com.centralelille.sequence1.data.ProfilListeToDo
import com.google.gson.Gson

class ChoixListActivity() : AppCompatActivity(), View.OnClickListener, ListeAdapter.OnListListener {

    private val adapter = newAdapter()

    private lateinit var refOkBtn: Button
    private lateinit var refTxtNewList: EditText
    private lateinit var listOfList: RecyclerView

    private lateinit var prefs: SharedPreferences
    private lateinit var prefsListes: SharedPreferences

    private lateinit var pseudoRecu: String
    private lateinit var listeListeToDo: ArrayList<ListeToDo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)

        val bundlePseudo: Bundle = this.getIntent().getExtras()

        pseudoRecu = bundlePseudo.getString("pseudo")
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefsListes = getSharedPreferences("DATA", 0)

        refOkBtn = findViewById(R.id.buttonNewList)
        listOfList = findViewById(R.id.listOfList)
        refTxtNewList = findViewById(R.id.editText)

        listeListeToDo = getLists(pseudoRecu)
        val dataSet: MutableList<String> = mutableListOf()

        repeat(listeListeToDo.size) {
            dataSet.add(listeListeToDo[it].titreListeToDo)
        }

        //val adapter = ItemAdapter(dataSet)
        listOfList.adapter = adapter
        listOfList.layoutManager = LinearLayoutManager(this)

        // Click listener associé au boutton pour créer les listes
        refOkBtn.setOnClickListener(this)
    }

    //On crée une fonction qui accède à la liste des Liste si le pseudo est déjà enregistré
    //et qui crée un nouveau Profil et le stock dans les préférences si le pseudo rentré n'a jamais ete utilisé

    fun getLists(pseudo: String): ArrayList<ListeToDo> {
        var profil: String = prefsListes.getString(pseudo, "New")
        val gson = Gson()
        Log.i("testchoixlistact", pseudo)

        if (profil == "New") {
            //On crée un nouvel objet ProfilListeToDo qu'on stocke sous format JSON dans les préférences sous son pseudo
            val newProfil: ProfilListeToDo = ProfilListeToDo(pseudo)
            val newProfilJSON: String = gson.toJson(newProfil)
            Log.i("testchoixlistact", "création d'un nouveau profil")

            val editor: SharedPreferences.Editor = prefsListes.edit()
            //editor.clear()
            editor.putString(pseudo, newProfilJSON)
            editor.apply()

            return (newProfil.listesToDo)
        } else {
            val profilListeToDo: ProfilListeToDo =
                gson.fromJson(profil, ProfilListeToDo::class.java)
            Log.i(
                "testchoixlistact",
                "récupérations listes ancien profil " + profilListeToDo.toString() + profilListeToDo.listesToDo.toString()
            )
            return (profilListeToDo.listesToDo)

        }
    }

    override fun onClick(v: View?) {
        val newListTitle = refTxtNewList.text.toString()
        val profil: String = prefsListes.getString(pseudoRecu, "New")
        val gson = Gson()

        val profilListeToDo: ProfilListeToDo = gson.fromJson(profil, ProfilListeToDo::class.java)

        when (v?.id) {
            R.id.buttonNewList -> {
                alert(pseudoRecu)

                val l = ListeToDo(newListTitle)
                profilListeToDo.listesToDo.add(l)
                Log.i("testchoixlistact", profilListeToDo.toString())
                Log.i("testchoixlistact", "Listes " + profilListeToDo.listesToDo.toString())
            }
        }
        val newProfilJSON: String = gson.toJson(profilListeToDo)

        var editor: SharedPreferences.Editor = prefsListes.edit()
        //editor.clear()
        editor.putString(pseudoRecu, newProfilJSON)
        editor.apply()
    }

    //Gestion deboguage
    private fun alert(s: String) {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, s, duration)
        toast.show()
    }

    private fun newAdapter(): ListeAdapter {
        return ListeAdapter(onListListener = this)
    }

    /**
     * Quand une liste est cliquée on change d'activité pout aller sur l'activité ShowListActivity
     *
     * @param list
     */
    override fun onListClicked(list: ListeToDo) {
        Log.d("ChoixListActivity", "onListClicked $list")
        Toast.makeText(this, list.titreListeToDo, Toast.LENGTH_LONG).show()

        /*
        Intent intent = new Intent(this, ShowListActivity.class)
        startActivity()
         */

    }
}
