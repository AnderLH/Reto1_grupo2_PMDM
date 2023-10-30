package com.grupo2.speakr.ui.users.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import com.grupo2.speakr.R
import com.grupo2.speakr.ui.users.register.RegisterActivity
import com.grupo2.speakr.ui.songs.all.SongListActivity
import com.grupo2.speakr.ui.songs.favourite.FavouriteListActivity

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        findViewById<Button>(R.id.buttonRegister).setOnClickListener{
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.buttonAccept).setOnClickListener{
            //Habrá que verificar que el Login es correcto antes de dejarle pasar a la lista de canciones
            if (true) {
                Log.i("recorrido", "1")
                val intent = Intent(applicationContext, FavouriteListActivity::class.java)
                startActivity(intent)
                finish()
            }else {
                //Transformaremos esto en un toast más adelante
                print("The login you have provided is invalid, verify everything is in order and please try again")
            }
        }
    }
}