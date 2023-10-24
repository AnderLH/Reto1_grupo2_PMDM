package com.grupo2.speakr

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.grupo2.speakr.ui.songs.all.SongListActivity
import com.grupo2.speakr.ui.users.login.LoginActivity

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        //Espera 4 segundos antes de cambiar de activity
        Handler(Looper.getMainLooper()).postDelayed({
            val intent = Intent(applicationContext, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }, 4000)
    }
}