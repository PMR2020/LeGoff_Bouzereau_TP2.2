package com.centralelille.sequence1

import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.Toast

/**
 * Commentaire
 *
 */
class MainActivity : AppCompatActivity(), View.OnClickListener {

    private lateinit var refBtnOk: View
    private lateinit var refEdtPseudo: EditText
    private lateinit var prefs: SharedPreferences
    private lateinit var pseudoPrefs: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_main)

        refBtnOk = findViewById(R.id.buttonNewItem)
        refEdtPseudo = findViewById(R.id.editPseudo)
        prefs = PreferenceManager.getDefaultSharedPreferences(this)


        refBtnOk.setOnClickListener(this)
        refEdtPseudo.setOnClickListener(this)
    }

    override fun onStart() {
        super.onStart()

        pseudoPrefs = prefs.getString("pseudo_prefs", "Name")
        //Log.i("test",prefs.getString("rien","Name"))


        refEdtPseudo.setText(pseudoPrefs)
    }

    override fun onClick(v: View?) {
        var pseudo = refEdtPseudo.text.toString()
        var bundlePseudo = Bundle()
        bundlePseudo.putString("pseudo", pseudo)

        when (v?.id) {
            R.id.buttonNewItem -> {
                var editor: SharedPreferences.Editor = prefs.edit()
                editor.clear()
                editor.putString("pseudo_prefs", pseudo)
                editor.apply()

                var afficherChoixListActivity: Intent = Intent(this, ChoixListActivity::class.java)
                afficherChoixListActivity.putExtras(bundlePseudo)
                startActivity(afficherChoixListActivity)
            }
        }
    }

    //Gestion menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        var id = item.itemId
        when (id) {
            R.id.menu_prefs -> {
                alert("appuis sur menu pref")
                var afficherPrefs: Intent = Intent(this, SettingsActivity::class.java)
                startActivity(afficherPrefs)
            }
        }

        return super.onOptionsItemSelected(item)
    }

    //Gestion deboguage
    fun alert(s: String) {
        val duration = Toast.LENGTH_SHORT
        val toast = Toast.makeText(applicationContext, s, duration)
        toast.show()
    }


}
