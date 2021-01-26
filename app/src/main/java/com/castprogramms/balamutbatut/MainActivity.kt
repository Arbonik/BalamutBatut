package com.castprogramms.balamutbatut

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private val navView: BottomNavigationView by lazy { findViewById(R.id.nav_view) }
    private val navController: NavController by lazy { findNavController(R.id.nav_host_fragment) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        navView.visibility = View.GONE
    }
    fun toMainGraph(){
        navController.navigate(R.id.action_registrFragment_to_navigation)
        navView.visibility = View.VISIBLE

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_progress, R.id.navigation_action, R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }
}