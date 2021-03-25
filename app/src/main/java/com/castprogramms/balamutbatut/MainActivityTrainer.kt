package com.castprogramms.balamutbatut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivityTrainer : AppCompatActivity() {

    lateinit var navView: BottomNavigationView
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_trainer)
        navView = findViewById(R.id.nav_view_trainer)
        navController = findNavController(R.id.nav_host_fragment_trainer)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.group_Fragment, R.id.profile_Fragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    override fun onSupportNavigateUp() = navController.navigateUp()

    fun setTitleAppBar(string: String){
        title = string
    }
}