package com.castprogramms.balamutbatut

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
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
        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        ) // проверка на наличие разрешений
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 101)
            }
    }
    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()
    override fun onBackPressed() {
        navController.popBackStack()
    }

    fun toStudent(){
        startActivity(Intent(this, MainActivityStudent::class.java))
    }

    fun toTrainer(){
        startActivity(Intent(this, MainActivityTrainer::class.java))
    }

}