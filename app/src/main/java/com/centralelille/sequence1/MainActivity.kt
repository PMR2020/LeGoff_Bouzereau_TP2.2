package com.centralelille.sequence1

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.centralelille.sequence1.dataAPI.DataProvider
import kotlinx.coroutines.*
import java.lang.Exception

/**
 * Commentaire
 *
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var refBtnOk: View
    private lateinit var refEdtPseudo: EditText
    private lateinit var refEdtPass: EditText

    private lateinit var prefs: SharedPreferences
    private lateinit var pseudoPrefs: String

    private val activityScope = CoroutineScope(
        SupervisorJob()
                + Dispatchers.Main
                + CoroutineExceptionHandler { _, throwable ->
            Log.e("MainActivity", "CoroutineExceptionHandler : ${throwable.message}")
        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        refBtnOk = findViewById(R.id.buttonNewItem)
        refEdtPseudo = findViewById(R.id.editPseudo)
        refEdtPass = findViewById(R.id.editPass)
        prefs = PreferenceManager.getDefaultSharedPreferences(this)

        getAdminHash()

        refBtnOk.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()

        pseudoPrefs = prefs.getString("pseudo_prefs", "Name")
        //Log.i("test",prefs.getString("rien","Name"))

        refEdtPseudo.setText(pseudoPrefs)
    }

    override fun onClick(v: View?) {
        val pseudo = refEdtPseudo.text.toString()
        val pass = refEdtPass.text.toString()

        when (v?.id) {
            R.id.buttonNewItem -> {
                activityScope.launch {
                    try {
                        val reponse = DataProvider.authenticate(pseudo, pass)
                        val editor: SharedPreferences.Editor = prefs.edit()
                        editor.clear()
                        editor.putString("pseudo_prefs", pseudo)
                        editor.putString("currentUserHash", reponse.hash)
                        editor.apply()
                        val afficherChoixListActivity =
                            Intent(this@MainActivity, ChoixListActivity::class.java)
                        startActivity(afficherChoixListActivity)
                    } catch(e: Exception) {
                        Log.i("testRetrofit", "authenticate à pas marché")
                        //Tentative de création d'un nouvel utilisateur
                        try {
                            val adminHash = prefs.getString("adminHash", "no Hash")
                            val newUserResponse =
                                DataProvider.insertNewUser(pseudo, pass, adminHash)
                            if (newUserResponse.success) {
                                Log.i("testRetrofit", "appel à servicepassé, pb dans main")
                                var hash = DataProvider.authenticate(pseudo, pass).hash
                                val editor: SharedPreferences.Editor = prefs.edit()
                                editor.clear()
                                editor.putString("pseudo_prefs", pseudo)
                                editor.putString("currentUserHash", hash)
                                editor.apply()
                                val afficherChoixListActivity =
                                    Intent(this@MainActivity, ChoixListActivity::class.java)
                                startActivity(afficherChoixListActivity)
                            }
                        }
                        catch(e: Exception){
                            alert("Mot de passe incorrect ou pseudo déjà utilisé /n Veuillez rééssayer")
                        }
                    }

                }
            }
        }
    }

    //Gestion menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        when (id) {
            R.id.menu_prefs -> {
                this.alert("appuis sur menu pref")
                val afficherPrefs = Intent(this, SettingsActivity::class.java)
                startActivity(afficherPrefs)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    fun getAdminHash(){
        activityScope.launch {
            val reponse = DataProvider.authenticate("tom", "web")
            if (reponse.success){
                val editor: SharedPreferences.Editor = prefs.edit()
                editor.putString("adminHash", reponse.hash)
                Log.i("testRetrofit",reponse.hash)
                editor.apply()
            }
        }
    }

    //Gestion deboguage
    private fun alert(s: String) {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, s, duration)
        toast.show()
    }
}
