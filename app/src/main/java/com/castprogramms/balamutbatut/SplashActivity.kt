package com.castprogramms.balamutbatut

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import androidx.lifecycle.Observer
import com.castprogramms.balamutbatut.tools.Registration
import com.castprogramms.balamutbatut.tools.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.*

class SplashActivity : AppCompatActivity() {

    val scope = CoroutineScope(Job() + Dispatchers.Main)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()
        val googleAuth = GoogleSignIn.getLastSignedInAccount(this)
        if (googleAuth != null)
            Registration().auth(googleAuth)
        User.mutableLiveDataSuccess.observe(this, Observer{
            if (it != null) {
                val intent = Intent(this@SplashActivity, MainActivityStudent::class.java)
                startActivity(intent)
                finish()
            }
        })

    }
}