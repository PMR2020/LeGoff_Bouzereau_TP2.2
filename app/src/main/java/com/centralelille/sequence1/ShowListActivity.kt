package com.centralelille.sequence1

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity

class ShowListActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var refOkBtn : Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_show_list)

        refOkBtn = findViewById(R.id.buttonNewItem)

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