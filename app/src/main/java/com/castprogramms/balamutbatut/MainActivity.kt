package com.castprogramms.balamutbatut

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity(){
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.nav_host_fragment_registr)
    }
    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()

    fun toStudent(){
        startActivity(Intent(this, MainActivityStudent::class.java))
        finish()
    }

    fun toTrainer(){
        startActivity(Intent(this, MainActivityTrainer::class.java))
        finish()
    }

}