package com.grupo2.speakr.ui.users.login

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import com.grupo2.speakr.R
import com.grupo2.speakr.data.LoginUser
import com.grupo2.speakr.data.repository.remote.RemoteUserDataSource
import com.grupo2.speakr.ui.users.register.RegisterActivity
import com.grupo2.speakr.ui.songs.all.SongListActivity
import com.grupo2.speakr.utils.Resource

class LoginActivity : AppCompatActivity() {
    private val userRepository = RemoteUserDataSource()
    private val viewModel: LoginViewModel by viewModels { LoginViewModelFactory(
        userRepository
    ) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val bundle : Bundle? = intent.extras
        var broughtEmail : String?
        var broughtPassword : String?
        var loginuser : LoginUser


        findViewById<Button>(R.id.buttonRegister).setOnClickListener{
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.buttonAccept).setOnClickListener{
            if (!bundle?.getStringArray("loginInfo").isNullOrEmpty()) {
                broughtEmail = bundle?.getStringArray("loginInfo")?.get(0)
                broughtPassword = bundle?.getStringArray("loginInfo")?.get(1)

                //Log.i("infoCheck", broughtEmail.toString())
                //Log.i("infoCheck", broughtPassword.toString())

                findViewById<EditText>(R.id.emailAddres).setText(broughtEmail)
                findViewById<EditText>(R.id.password).setText(broughtPassword)
                loginuser = LoginUser( broughtEmail.toString(), broughtPassword.toString())
            }else {
                val email : String = findViewById<EditText>(R.id.emailAddres).text.toString()
                val password : String = findViewById<EditText>(R.id.password).text.toString()
                //Log.i("CheckLogInUser", email)
                //Log.i("CheckLogInUser", password)
                loginuser = LoginUser( email, password)
            }
            viewModel.loginOfUser(loginuser)

            viewModel.loggedUser.observe(this) {
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                            Log.i("PruebasValorData",it.data.toString())
                            if (it.data != 0) {
                                val intent = Intent(applicationContext, SongListActivity::class.java)
                                intent.putExtra("loggedUserID", it.data)
                                startActivity(intent)
                                finish()
                            }else{
                                Toast.makeText(this, "The Login provided is not valid, please try again", Toast.LENGTH_SHORT).show()
                            }
                        }
                        Resource.Status.ERROR -> {
                            Toast.makeText(this, "The Login provided is not valid, please try again", Toast.LENGTH_SHORT).show()
                            Log.i("ConnectionCheck", it.message.toString())
                        }
                        Resource.Status.LOADING -> {
                            // for the moment
                        }
                    }
            }
        }

    }
}