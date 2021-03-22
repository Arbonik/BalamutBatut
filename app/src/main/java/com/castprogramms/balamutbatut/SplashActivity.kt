package com.castprogramms.balamutbatut

import android.content.Intent
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.DataLoader
import com.castprogramms.balamutbatut.tools.TypesUser
import com.castprogramms.balamutbatut.tools.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashActivity : AppCompatActivity() {
    private val repository = BalamutApplication.repository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()


        val googleAuth = GoogleSignIn.getLastSignedInAccount(this)

        if (googleAuth != null) {
            DataLoader().loadUserData(googleAuth)
            repository.loadUserData(googleAuth)
        } else
            User.mutableLiveDataSuccess.postValue(false)


        repository.user.observe(this) {
            when (it) {
                is Resource.Error -> {  startActivity( Intent(
                            this,
                            MainActivity::class.java
                        )
                    )
                }
                is Resource.Loading -> {
                    MainScope().launch {
                        while (true) {
                            findViewById<ImageView>(R.id.screensplashbackground).rotation +=1
                            delay(10)
                        }
                    }

                }
                is Resource.Success -> {
                    when (it.data!!.type) {
                        TypesUser.TRAINER.desc -> {
                            val intent = Intent(this, MainActivityTrainer::class.java)
                            startActivity(intent)
                            finish()
                        }
                        TypesUser.STUDENT.desc -> {
                            val intent = Intent(this, MainActivityStudent::class.java)
                            startActivity(intent)
                            finish()
                        }
                    }
                }
            }
        }
    }
}