package com.grupo2.speakr.ui.users.register

import DataManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.viewModels
import com.grupo2.speakr.R
import com.grupo2.speakr.data.User
import com.grupo2.speakr.data.repository.remote.RemoteUserDataSource
import com.grupo2.speakr.ui.users.login.LoginActivity
import com.grupo2.speakr.utils.Resource


class RegisterActivity : ComponentActivity() {
    private val userRepository = RemoteUserDataSource()
    private val viewModel: RegisterViewModel by viewModels { RegisterViewModelFactory(
        userRepository
    ) }

    private val dataManager = DataManager(this)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        dataManager.open()

        var registerFilled:Boolean


        findViewById<Button>(R.id.returnButton).setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        findViewById<Button>(R.id.registerAcceptButton).setOnClickListener {
           val email :String = findViewById<EditText>(R.id.email).text.toString()
           val name :String = findViewById<EditText>(R.id.name).text.toString()
           val surname :String =  findViewById<EditText>(R.id.surname).text.toString()
           val password :String = findViewById<EditText>(R.id.firstPassword).text.toString()
           val passwordRepeated :String = findViewById<EditText>(R.id.secondPassword).text.toString()

            registerFilled = true

            if(email.isBlank() or name.isBlank() or surname.isBlank() or password.isBlank() or passwordRepeated.isBlank()) {
                registerFilled = false
                Toast.makeText(this, "The information provided is not valid, make sure to fill everything", Toast.LENGTH_SHORT).show()
                Log.i("RegisterCheck", "register invalid")
            }

            if (registerFilled and (password == passwordRepeated)) {
                Log.i("RegisterCheck", "The Register is valid")
                val user = User(0,name, surname, email, password)
                viewModel.registerUser(user)

                viewModel.created.observe(this) {
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                            Log.i("ConnectionCheck", it.message.toString())

                            dataManager.insertLog(user.email, user.password)

                            val intent = Intent(applicationContext, LoginActivity::class.java)
                            intent.putExtra("loginInfo", arrayOf(email,password))
                            startActivity(intent)
                            finish()
                        }
                        Resource.Status.ERROR -> {
                            Toast.makeText(this, "The login you have provided is invalid,make sure to fill everything", Toast.LENGTH_LONG).show()
                            Log.i("ConnectionCheck", it.message.toString())
                        }
                        Resource.Status.LOADING -> {
                            // for the moment
                        }
                    }
                }

            }else {
                Toast.makeText(this, "The information provided invalid, make sure that the passwords match", Toast.LENGTH_SHORT).show()
            }
        }

    }
}