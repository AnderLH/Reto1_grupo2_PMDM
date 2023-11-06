package com.grupo2.speakr.ui.users.login

import DataManager
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.util.Log
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
import com.grupo2.speakr.ui.users.register.RegisterActivity
import com.grupo2.speakr.utils.Resource

private const val PREFS_FILENAME = "LoginPrefs"

class LoginActivity : AppCompatActivity() {
    private val userRepository = RemoteUserDataSource()
    private val viewModel: LoginViewModel by viewModels { LoginViewModelFactory(
        userRepository
    ) }
    val dataManager = DataManager(this) // Initialize the DataManager with your context

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        sharedPreferences = getSharedPreferences(PREFS_FILENAME, Context.MODE_PRIVATE)

        dataManager.open()
        val bundle : Bundle? = intent.extras
        val broughtEmail : String?
        val broughtPassword : String?
        var loginUser : LoginUser? =  dataManager.getLastLog()
        val userEmail = getUserEmail()
        val userPassword = getUserPassword()
        val isCheckBoxChecked = getBooleanValue("checkbox_checked", false)

        findViewById<Button>(R.id.buttonRegister).setOnClickListener{
            val intent = Intent(applicationContext, RegisterActivity::class.java)
            startActivity(intent)
            finish()
        }

        if (isCheckBoxChecked) {
            if(userEmail.isEmpty() or userPassword.isEmpty()) {
                findViewById<EditText>(R.id.emailAddres).setText(userEmail)
                findViewById<EditText>(R.id.password).setText(userPassword)
                findViewById<CheckBox>(R.id.checkBox).isChecked = true
            }
        }


        if (!bundle?.getStringArray("loginInfo").isNullOrEmpty()) {
            broughtEmail = bundle?.getStringArray("loginInfo")?.get(0)
            broughtPassword = bundle?.getStringArray("loginInfo")?.get(1)

            Log.i("infoCheck", broughtEmail.toString())
            Log.i("infoCheck", broughtPassword.toString())

            findViewById<EditText>(R.id.emailAddres).setText(broughtEmail)
            findViewById<EditText>(R.id.password).setText(broughtPassword)
        }

        findViewById<Button>(R.id.buttonAccept).setOnClickListener{

            val email : String = findViewById<EditText>(R.id.emailAddres).text.toString()
            val password : String = findViewById<EditText>(R.id.password).text.toString()
            Log.i("CheckLogInUser", email)
            Log.i("CheckLogInUser", password)
            loginUser = LoginUser( email, password)

            viewModel.loginOfUser(loginUser!!)

            if(findViewById<CheckBox>(R.id.checkBox).isChecked) {
                dataManager.insertLog(loginUser!!.email, loginUser!!.password)
//                saveUserEmail(loginUser.email)
//                saveUserPassword(loginUser.password)
                saveBooleanValue("checkbox_checked", true)
            }

            viewModel.loggedUser.observe(this) {
                    when (it.status) {
                        Resource.Status.SUCCESS -> {
                            it.data?.let { data ->
                                Speaker.userPreferences.saveAuthToken(data.accessToken)

                                val intent = Intent(applicationContext, SongListActivity::class.java)
                                startActivity(intent)
                                finish()
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
    fun saveBooleanValue(key: String, value: Boolean) {
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
    fun getBooleanValue(key: String, defaultValue: Boolean): Boolean {
        return sharedPreferences.getBoolean(key, defaultValue)
    }
}