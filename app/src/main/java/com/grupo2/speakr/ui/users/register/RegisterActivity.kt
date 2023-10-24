package com.grupo2.speakr.ui.users.register

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.grupo2.speakr.R
import com.grupo2.speakr.data.User
import com.grupo2.speakr.data.repository.remote.RemoteUserDataSource
import androidx.activity.viewModels
import com.grupo2.speakr.utils.Resource


class RegisterActivity : ComponentActivity() {
    private val userRepository = RemoteUserDataSource()
    private val viewModel: RegisterViewModel by viewModels { RegisterViewModelFactory(
        userRepository
    ) }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        findViewById<Button>(R.id.registerAcceptButton).setOnClickListener {
           val email :String = findViewById<EditText>(R.id.email).text.toString()
           val name :String = findViewById<EditText>(R.id.name).text.toString()
           val surname :String =  findViewById<EditText>(R.id.surname).text.toString()
           val password :String = findViewById<EditText>(R.id.firstPassword).text.toString()
           val passwordRepeated :String = findViewById<EditText>(R.id.secondPassword).text.toString()

            if (password == passwordRepeated) {
                Log.i("PasswordCheck", "passwords are the same")
                val user = User(0,name, surname, email, password)
                viewModel.registerUser(user)

                viewModel.created.observe(this) {
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                            Log.i("ConnectionCheck", it.message.toString())
                        }
                        Resource.Status.ERROR -> {
                            Toast.makeText(this, it.message, Toast.LENGTH_LONG).show()
                            Log.i("ConnectionCheck", it.message.toString())
                        }
                        Resource.Status.LOADING -> {
                            // for the moment
                        }
                    }
                }

            }else {
                Log.i("PasswordCheck", "passwords are NOT the same")
            }
        }

    }
}