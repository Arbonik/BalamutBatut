package com.castprogramms.balamutbatut

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import com.castprogramms.balamutbatut.network.Repository
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.User
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject


class MainActivity : AppCompatActivity() {
    private val repository: Repository by inject()
    lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        setContentView(R.layout.activity_main)
        navController = findNavController(R.id.nav_host_fragment_registr)

        repository.getUidInReferralLink(intent).observe(this, {
            when (it) {
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    if (it.data != null) {
                        createNotificationReferral()
                        User.referId = it.data
                    }
                }
            }
        })

    }
    override fun onSupportNavigateUp() = findNavController(R.id.nav_host_fragment).navigateUp()

    fun toStudent() {
        startActivity(Intent(this, MainActivityStudent::class.java))
        finish()
    }

    fun toTrainer() {
        startActivity(Intent(this, MainActivityTrainer::class.java))
        finish()
    }

    private fun createNotificationReferral() {
        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val title = "Приглашение"
        val text = "Вы были приглашены другим пользователем," +
                    " приглашайте новых пользователей и получите бонус"

        val builder = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId, "My channel",
                NotificationManager.IMPORTANCE_HIGH
            )
            channel.description = "My channel description"
            channel.enableLights(true)
            channel.lightColor = Color.RED
            channel.enableVibration(false)
            notificationManager.createNotificationChannel(channel)

            NotificationCompat.Builder(this, channelId)
                .setSmallIcon(R.drawable.add)
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setStyle(NotificationCompat.BigTextStyle().bigText(text))

        } else {
            NotificationCompat.Builder(this)
                .setSmallIcon(R.drawable.add)
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

    companion object {
        const val channelId = "balamut"
    }
}