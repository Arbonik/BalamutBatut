package com.castprogramms.balamutbatut

import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivityStudent : AppCompatActivity() {
    lateinit var navView: BottomNavigationView
    lateinit var navController: NavController

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        super.onCreateOptionsMenu(menu)
        menuInflater.inflate(R.menu.app_bar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.item__bar_settings -> {
                navController.navigate(R.id.action_profileFragment_to_settingsFragment)
            }
        }
        return true
    }

    fun hideOptionsMenu() {
        //menuInflater.inflate(R.menu.app_bar_menu, menu)

        fun onCreateOptionsMenu(menu: Menu?): Boolean {
            super.onCreateOptionsMenu(menu)
            menuInflater.inflate(R.menu.app_bar_menu, menu)
            return true
        }

        fun onOptionsItemSelected(item: MenuItem): Boolean {
            when (item.itemId) {
                R.id.item__bar_settings -> {
                    navController.navigate(R.id.action_profileFragment_to_settingsFragment)
                }
            }
            return true
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_student)
        navView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.progressFragment, R.id.profileFragment
            )
        )
        //val topAppBar = findViewById<MaterialToolbar>(R.id.topAppBar)

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

//    override fun onOptionsItemSelected(item: MenuItem): Boolean {
//        findNavController(R.id.nav_host_fragment).navigateUp()
//        return super.onOptionsItemSelected(item)
//    }
    override fun onSupportNavigateUp() = navController.navigateUp()

    override fun onBackPressed() {

    }
}