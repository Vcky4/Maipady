package com.example.maipady

import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.navigation.NavigationView
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.drawerlayout.widget.DrawerLayout
import androidx.appcompat.app.AppCompatActivity
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_LOCKED_CLOSED
import androidx.drawerlayout.widget.DrawerLayout.LOCK_MODE_UNLOCKED
import androidx.lifecycle.ViewModelProvider
import com.example.maipady.databinding.ActivityMainBinding
import com.example.maipady.ui.authentication.AuthSharedViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.appBarMain.toolbar)

        val drawerLayout: DrawerLayout = binding.drawerLayout
        val navView: NavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        val authSharedViewModel = ViewModelProvider(this)[AuthSharedViewModel::class.java]
        val database: DatabaseReference = Firebase.database.reference

        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.nav_home
            ), drawerLayout
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)


        //hide side bar for login and sign up
        navController.addOnDestinationChangedListener { _, destination, _ ->
            if(destination.id == R.id.sign_up
                || destination.id == R.id.login
                || destination.id == R.id.payment
            ) {
                drawerLayout.setDrawerLockMode(LOCK_MODE_LOCKED_CLOSED)

            }
            else{
                drawerLayout.setDrawerLockMode(LOCK_MODE_UNLOCKED)
                navView.menu.findItem(R.id.nav_logout).setOnMenuItemClickListener {
                    logoutDialog()
                    true
                }
            }
        }

        authSharedViewModel.regNo.observe(this,{
            navView.getHeaderView(0).findViewById<TextView>(R.id.user_Reg_no).text = it
        })
        authSharedViewModel.email.observe(this,{
            navView.getHeaderView(0).findViewById<TextView>(R.id.user_email).text = it
        })

    }

    private fun logoutDialog() {
        Firebase.auth.signOut()
        findNavController(R.id.nav_host_fragment_content_main).navigate(R.id.action_global_login)
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
        menuInflater.inflate(R.menu.main, menu)
        return true
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration) || super.onSupportNavigateUp()
    }
}