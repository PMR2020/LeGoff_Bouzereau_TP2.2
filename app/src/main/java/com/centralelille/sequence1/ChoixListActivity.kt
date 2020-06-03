package com.centralelille.sequence1

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.View
import android.widget.Button
import android.widget.ListView
import android.widget.Toast

class ChoixListActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var refOkBtn : Button
    private lateinit var prefs: SharedPreferences
    private lateinit var listOfList: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)
        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        refOkBtn = findViewById(R.id.buttonNewList)
        listOfList = findViewById(R.id.ListOfList)

        // Loads a list of recipe objects from a JSON asset
        //val recipeList = Recipe.getRecipesFromFile("recipes.json", this)
        // Creates an array of
        //val listItems = arrayOfNulls<String>(recipeList.size)

        refOkBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        var bundlePseudo: Bundle = this.getIntent().getExtras()
        var pseudo = bundlePseudo.getString("pseudo")
        when(v?.id){
            R.id.buttonNewList->alert(pseudo)
        }
    }

    //Gestion deboguage
    fun alert(s: String){
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, s, duration)
        toast.show()
    }
}
