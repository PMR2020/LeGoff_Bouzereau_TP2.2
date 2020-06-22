package com.centralelille.sequence1

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.centralelille.sequence1.adapters.ListeAdapter
import com.centralelille.sequence1.data.ListeToDo
import com.centralelille.sequence1.data.ProfilListeToDo
import com.google.gson.Gson

class ChoixListActivity : AppCompatActivity(), View.OnClickListener, ListeAdapter.OnListListener {

    private val adapter = newAdapter()

    private lateinit var refOkBtn: Button
    private lateinit var refTxtNewList: EditText
    private lateinit var refRecycler: RecyclerView

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
        refRecycler = findViewById(R.id.listOfList)
        refTxtNewList = findViewById(R.id.editText)

        listeListeToDo = getLists(pseudoRecu)

        adapter.showData(listeListeToDo)
        refRecycler.adapter = adapter
        refRecycler.layoutManager = LinearLayoutManager(this)

        // Click listener associé au boutton pour créer les listes et les sauvergarder
        refOkBtn.setOnClickListener(this)
    }

    //On crée une fonction qui accède à la liste des Liste si le pseudo est déjà enregistré
    //et qui crée un nouveau Profil et le stock dans les préférences si le pseudo rentré n'a jamais ete utilisé

    fun getLists(pseudo: String): ArrayList<ListeToDo> {
        val profil: String = prefsListes.getString(pseudo, "New")
        val gson = Gson()
        Log.i("testchoixlistact", pseudo)

        if (profil == "New") {
            //On crée un nouvel objet ProfilListeToDo qu'on stocke sous format JSON dans les préférences sous son pseudo
            val newProfil = ProfilListeToDo(pseudo)
            val newProfilJSON: String = gson.toJson(newProfil)
            Log.i("testchoixlistact", "création d'un nouveau profil")

            val editor: SharedPreferences.Editor = prefsListes.edit()
            editor.putString(pseudo, newProfilJSON)
            editor.apply()

            return newProfil.listesToDo
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
                alert("Nouvelle liste créée")
                val l = ListeToDo(newListTitle)
                profilListeToDo.listesToDo.add(l)
                Log.i("testchoixlistact", profilListeToDo.toString())
                Log.i("testchoixlistact", "Listes " + profilListeToDo.listesToDo.toString())
            }
        }

        val newProfilJSON: String = gson.toJson(profilListeToDo)

        // Edit shared preference (to put data)
        val editor: SharedPreferences.Editor = prefsListes.edit()
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
     * @param liste
     */
    override fun onListClicked(liste: ListeToDo) {
        Log.d("ChoixListActivity", "onListClicked $liste")
        Toast.makeText(this, liste.titreListeToDo, Toast.LENGTH_LONG).show()


        val titreListe = liste.titreListeToDo
        val bundleTitre = Bundle()
        bundleTitre.putString("titre", titreListe)
        bundleTitre.putString("pseudo", pseudoRecu)

        val afficherShowListActivity = Intent(this, ShowListActivity::class.java)
        afficherShowListActivity.putExtras(bundleTitre)
        startActivity(afficherShowListActivity)
    }
}
