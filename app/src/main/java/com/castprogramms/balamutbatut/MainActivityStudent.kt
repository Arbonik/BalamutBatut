package com.castprogramms.balamutbatut

import android.os.Bundle
import android.view.View
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.castprogramms.balamutbatut.tools.Registration
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivityStudent : MainActivity() {
    lateinit var navView: BottomNavigationView
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_student)
        val googleAuth = GoogleSignIn.getLastSignedInAccount(this)
        if (googleAuth != null)
             Registration().auth(googleAuth)
        navView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
        navView.visibility = View.GONE
    }
    fun toMainGraph(registration: Boolean = false){
        if (registration)
            navController.navigate(R.id.action_insertDataUserFragment_to_navigation)
        else
            navController.navigate(R.id.action_registrFragment_to_navigation)
        navView.visibility = View.VISIBLE

        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.progressFragment, R.id.profileFragment
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)

    }
    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()

    override fun onBackPressed() {

    }
}