package com.centralelille.sequence1

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.centralelille.sequence1.data.ListeToDo
import com.centralelille.sequence1.data.ProfilListeToDo
import com.google.gson.Gson


class ChoixListActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var refOkBtn : Button
    private lateinit var listOfList: ListView
    private lateinit var refTxtNewList: EditText

    private lateinit var prefs: SharedPreferences
    private lateinit var prefsListes: SharedPreferences

    private lateinit var pseudoRecu: String
    private lateinit var listeListeToDo: ArrayList<ListeToDo>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)
        var bundlePseudo: Bundle = this.getIntent().getExtras()

        pseudoRecu = bundlePseudo.getString("pseudo")
        prefs = PreferenceManager.getDefaultSharedPreferences(this)
        prefsListes = getSharedPreferences("DATA",0)
        Log.i("testchoixlistact",prefsListes.all.toString())

        refOkBtn = findViewById(R.id.buttonNewList)
        refTxtNewList = findViewById(R.id.editText)

        // Loads a list of recipe objects from a JSON asset
        //val recipeList = Recipe.getRecipesFromFile("recipes.json", this)
        // Creates an array of
        //val listItems = arrayOfNulls<String>(recipeList.size)

        refOkBtn.setOnClickListener(this)

        listeListeToDo = getLists(pseudoRecu)
    }

    //On crée une fonction qui accède à la liste des Liste si le pseudo est déjà enregistré
    //et qui crée un nouveau Profil et le stock dans les préférences si le pseudo rentré n'a jamais ete utilisé

    fun getLists(pseudo: String): ArrayList<ListeToDo>{
        var profil: String = prefsListes.getString(pseudo,"New")
        var gson = Gson()

        if (profil=="New"){
            //On crée un nouvel objet ProfilListeToDo qu'on stocke sous format JSON dans les préférences sous son pseudo
            var newProfil: ProfilListeToDo = ProfilListeToDo(pseudo)

            var newProfilJSON: String = gson.toJson(newProfil)

            Log.i("testchoixlistact","création d'un nouveau profil")

            var editor: SharedPreferences.Editor = prefsListes.edit()
            editor.putString(pseudo,newProfilJSON)
            editor.commit()

            return(newProfil.listesToDo)
        }
        else {
            var profilListeToDo: ProfilListeToDo = gson.fromJson(profil,ProfilListeToDo::class.java)
            Log.i("testchoixlistact","récupérations listes ancien profil "+profilListeToDo.toString()+profilListeToDo.listesToDo.toString())
            return(profilListeToDo.listesToDo)

        }
    }

    override fun onClick(v: View?) {
        var newListTitle = refTxtNewList.text.toString()
        var profil: String = prefsListes.getString(pseudoRecu,"New")
        var gson = Gson()
        var profilListeToDo: ProfilListeToDo = gson.fromJson(profil,ProfilListeToDo::class.java)

        when(v?.id){
            R.id.buttonNewList->{
                alert(pseudoRecu)

                var l = ListeToDo(newListTitle)
                profilListeToDo.listesToDo.add(l)
                Log.i("testchoixlistact",profilListeToDo.toString())
                Log.i("testchoixlistact","Listes "+profilListeToDo.listesToDo.toString())
            }
        }
        var newProfilJSON: String = gson.toJson(profilListeToDo)

        var editor: SharedPreferences.Editor = prefsListes.edit()
        editor.putString(pseudoRecu,newProfilJSON)
        editor.apply()

    }

    //Gestion deboguage
    fun alert(s: String){
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, s, duration)
        toast.show()
    }
}
