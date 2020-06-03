package com.centralelille.sequence1

import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.centralelille.sequence1.data.ListeToDo
import com.centralelille.sequence1.data.ProfilListeToDo

class ChoixListActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var refOkBtn : Button
    private lateinit var prefs: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_choix_list)
        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        refOkBtn = findViewById(R.id.buttonNewList)

        refOkBtn.setOnClickListener(this)
    }

    override fun onClick(v: View?) {
        var bundlePseudo: Bundle = this.getIntent().getExtras()
        var pseudo = bundlePseudo.getString("pseudo")
        when(v?.id){
            R.id.buttonNewList->{
                alert(pseudo)

                var p = ProfilListeToDo(pseudo)
                var l = ListeToDo("Liste1")
                p.listesToDo.add(l)
                Log.i("objet",p.toString()) ;
            }



        }
    }

    //Gestion deboguage
    fun alert(s: String){
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, s, duration)
        toast.show()
    }
}
