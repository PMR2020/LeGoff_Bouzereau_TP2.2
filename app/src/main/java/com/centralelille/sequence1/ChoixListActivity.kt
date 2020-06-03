package com.centralelille.sequence1

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
import com.centralelille.sequence1.data.ListeToDo
import com.centralelille.sequence1.data.ProfilListeToDo
import com.google.gson.Gson

class ChoixListActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var refOkBtn : Button
    private lateinit var refTxtNewList: EditText
    private lateinit var listOfList: RecyclerView

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

        refOkBtn = findViewById(R.id.buttonNewList)
        listOfList = findViewById(R.id.listOfList)
        refTxtNewList = findViewById(R.id.editText)

        listeListeToDo = getLists(pseudoRecu)

        val dataSet : MutableList<String> = mutableListOf()

        repeat(listeListeToDo.size) {
            dataSet.add(listeListeToDo[it].titreListeToDo)
        }
        val adapter = ItemAdapter(dataSet)
        listOfList.adapter = adapter
        listOfList.layoutManager = LinearLayoutManager(this)

        refOkBtn.setOnClickListener(this)
    }

    //On crée une fonction qui accède à la liste des Liste si le pseudo est déjà enregistré
    //et qui crée un nouveau Profil et le stock dans les préférences si le pseudo rentré n'a jamais ete utilisé

    fun getLists(pseudo: String): ArrayList<ListeToDo>{
        var profil: String = prefsListes.getString(pseudo,"New")
        var gson = Gson()
        Log.i("testchoixlistact",pseudo)

        if (profil=="New"){
            //On crée un nouvel objet ProfilListeToDo qu'on stocke sous format JSON dans les préférences sous son pseudo
            var newProfil: ProfilListeToDo = ProfilListeToDo(pseudo)

            var newProfilJSON: String = gson.toJson(newProfil)

            Log.i("testchoixlistact","création d'un nouveau profil")

            var editor: SharedPreferences.Editor = prefsListes.edit()
            editor.clear()
            editor.putString(pseudo,newProfilJSON)
            editor.apply()

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
        editor.clear()
        editor.putString(pseudoRecu,newProfilJSON)
        editor.apply()

    }

    //Gestion deboguage
    fun alert(s: String){
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
