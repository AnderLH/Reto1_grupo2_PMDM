package com.grupo2.speakr.ui.users.login

import DataManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.grupo2.speakr.R
import com.grupo2.speakr.Speaker
import com.grupo2.speakr.data.LoginUser
import com.grupo2.speakr.data.repository.remote.RemoteUserDataSource
import com.grupo2.speakr.ui.songs.SongActivity
import com.grupo2.speakr.ui.users.register.RegisterActivity
import com.grupo2.speakr.utils.Resource

private const val PREFS_FILENAME = "LoginPrefs"

class LoginActivity : AppCompatActivity() {
    private val userRepository = RemoteUserDataSource()
    private val viewModel: LoginViewModel by viewModels { LoginViewModelFactory(
        userRepository
    ) }
    private val dataManager = DataManager(this) // Initialize the DataManager with your context

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

        val bundle: Bundle? = intent.extras
        val isCheckBoxChecked = getBooleanValue("checkbox_checked", false)
        val checkBox = findViewById<CheckBox>(R.id.checkBox)

        // Set the state of the checkbox accordingly
        checkBox.isChecked = isCheckBoxChecked

        if (isCheckBoxChecked) {
            dataManager.open()
            val rememberUser: LoginUser? = dataManager.getLastLog()
            dataManager.close()
            if (rememberUser != null) {
                // Populate email and password fields from the remembered user's data
                findViewById<EditText>(R.id.emailAddress).setText(rememberUser.email.toString())
                findViewById<EditText>(R.id.password).setText(rememberUser.password.toString())
            }
        } else {
            // If the checkbox is not checked, clear the email and password fields
            findViewById<EditText>(R.id.emailAddress).text.clear()
            findViewById<EditText>(R.id.password).text.clear()
        }

        findViewById<Button>(R.id.buttonRegister).setOnClickListener {
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        if (!bundle?.getStringArray("loginInfo").isNullOrEmpty()) {
            val broughtEmail = bundle?.getStringArray("loginInfo")?.get(0)
            val broughtPassword = bundle?.getStringArray("loginInfo")?.get(1)

            findViewById<EditText>(R.id.emailAddress).setText(broughtEmail)
            findViewById<EditText>(R.id.password).setText(broughtPassword)
        }

        findViewById<Button>(R.id.buttonAccept).setOnClickListener {
            val email: String = findViewById<EditText>(R.id.emailAddress).text.toString()
            val password: String = findViewById<EditText>(R.id.password).text.toString()

            // Create a LoginUser object with the email and password
            val loginUser = LoginUser(email, password)

            // Attempt to log in the user using the viewModel
            viewModel.loginOfUser(loginUser)

            // Observe the result of the login attempt
            viewModel.loggedUser.observe(this) { result ->
                when (result.status) {
                    Resource.Status.SUCCESS -> {
                        // Handle successful login
                        result.data?.let { data ->
                            Speaker.userPreferences.saveAuthToken(data.accessToken)
                            dataManager.open()
                            dataManager.insertLog(email, password)
                            dataManager.close()
                            saveBooleanValue("checkbox_checked", checkBox.isChecked)

                            val intent = Intent(applicationContext, SongActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                    Resource.Status.ERROR -> {
                        // Handle login error
                        Toast.makeText(this, "The login provided is not valid, please try again", Toast.LENGTH_SHORT).show()
                    }
                    Resource.Status.LOADING -> {
                        // Handle loading state (optional)
                        // You can show a loading indicator or perform other actions while waiting
                    }
                }
            }

        }



    }

    // Save the user's email to SharedPreferences
    fun saveUserEmail(userEmail: String) {
        val editor = sharedPreferences.edit()
        editor.putString("user_email", userEmail)
        editor.apply()
    }
    // Save the user's password to SharedPreferences
    fun saveUserPassword(userPassword: String) {
        val editor = sharedPreferences.edit()
        editor.putString("user_password", userPassword)
        editor.apply()
    }
    // Save if the checkbox has been checked
    private fun saveBooleanValue(key: String, value: Boolean) {
            val editor = sharedPreferences.edit()
            editor.putBoolean(key, value)
            editor.apply()
    }

    // Retrieve the user's name from SharedPreferences
    fun getUserEmail(): String {
        return sharedPreferences.getString("user_name", "") ?: ""
    }
    // Retrieve the user's name from SharedPreferences
    fun getUserPassword(): String {
        return sharedPreferences.getString("user_password", "") ?: ""
    }

    // Retrieve the checkbox status
    private fun getBooleanValue(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }
}