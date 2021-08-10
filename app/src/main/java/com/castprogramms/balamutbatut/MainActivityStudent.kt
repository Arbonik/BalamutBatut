package com.castprogramms.balamutbatut

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import androidx.preference.PreferenceManager
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivityStudent : AppCompatActivity() {
    lateinit var navView: BottomNavigationView
    lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_student)
        navView = findViewById(R.id.nav_view)
        navController = findNavController(R.id.nav_host_fragment)
        if (PreferenceManager.getDefaultSharedPreferences(this).getBoolean("isFirst", true)) {
            createNotification()
            PreferenceManager.getDefaultSharedPreferences(this).edit()
                .putBoolean("isFirst", false)
                .apply()
        }

        val appBarConfiguration = AppBarConfiguration(
            setOf(R.id.ratingFragment, R.id.allElementListFragment, R.id.profileFragment)
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
        } // проверка на наличие разрешений
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            requestPermissions(arrayOf(Manifest.permission.CAMERA), 101)
        }

        if (if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                Log.e(
                    "test",
                    checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE).toString()
                )
                checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
            } else {
                TODO("VERSION.SDK_INT < M")
            }
        ) // проверка на наличие разрешений
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE), 101)
            }
    }
    override fun onSupportNavigateUp() = navController.navigateUp()

    private fun createNotification() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val title = "Напоминание"
        val text = "Если вы добавите свои контакты (сделать это можно, дважды нажав на картинку " +
                "в профиле), то люди смогут узнать, кем гордиться и к кому можно обратиться за советом"

        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                MainActivity.channelId, "My channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "My channel description"
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(false)
            notificationManager.createNotificationChannel(channel)

            NotificationCompat.Builder(this, MainActivity.channelId)
                .setSmallIcon(R.drawable.architecture)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))

        } else {
            NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.architecture)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))
        }

        with(NotificationManagerCompat.from(this)) {
            notify(0, builder.build()) // посылаем уведомление
        }
    }

}