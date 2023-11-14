package com.grupo2.speakr.ui.songs

import android.content.Intent
import com.grupo2.speakr.ui.songs.all.HomeFragment
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentTransaction
import com.google.android.material.navigation.NavigationView
import com.grupo2.speakr.R
import com.grupo2.speakr.databinding.ActivitySongBinding
import com.grupo2.speakr.ui.songs.favourite.FavouriteFragment
import com.grupo2.speakr.ui.users.email.EmailFragment
import com.grupo2.speakr.ui.users.login.LoginActivity
import com.grupo2.speakr.ui.users.password.PasswordFragment

class SongActivity : AppCompatActivity(), NavigationView.OnNavigationItemSelectedListener {

    private lateinit var fragmentManager: FragmentManager
    private lateinit var binding: ActivitySongBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySongBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val toolbar: androidx.appcompat.widget.Toolbar = binding.toolbar
        title = getString(R.string.login)
        setSupportActionBar(binding.toolbar)

        val toggle = ActionBarDrawerToggle(this, binding.drawerLayout, binding.toolbar, R.string.nav_open, R.string.nav_close)
        binding.drawerLayout.addDrawerListener(toggle)
        toggle.syncState()

        binding.navigationDrawer.setNavigationItemSelectedListener(this)

        binding.bottomNavigation.background = null
        binding.bottomNavigation.setOnItemSelectedListener { item ->
            when(item.itemId){
                //R.id.bottom_profile -> openFragment()
                R.id.bottom_home -> {
                    openFragment(HomeFragment())
                    supportActionBar?.title = getString(R.string.home)
                }
                R.id.bottom_favorite -> {
                    openFragment(FavouriteFragment())
                    supportActionBar?.title = getString(R.string.favorite)
                }
            }
            true
        }
        fragmentManager = supportFragmentManager
        openFragment(HomeFragment())

    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
       when(item.itemId){
           R.id.bottom_change_email -> {
               openFragment(EmailFragment())
               supportActionBar?.title = getString(R.string.email_change)
           }
           R.id.bottom_change_password -> {
               openFragment(PasswordFragment())
               supportActionBar?.title = getString(R.string.pass_change)
           }
           R.id.bottom_logout -> {
               val intent = Intent(this, LoginActivity::class.java)
               intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
               startActivity(intent)
           }
       }
       binding.drawerLayout.closeDrawer(GravityCompat.START)
       return true
    }

    override fun onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)){
            binding.drawerLayout.closeDrawer(GravityCompat.START)
        }else{
            super.onBackPressedDispatcher.onBackPressed()
        }
    }

    private fun openFragment(fragment: Fragment){
        val fragmentTransaction : FragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.fragment_container, fragment)
        fragmentTransaction.commit()
    }
}