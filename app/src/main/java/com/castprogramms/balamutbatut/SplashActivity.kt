package com.castprogramms.balamutbatut

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.observe
import com.castprogramms.balamutbatut.network.Repository
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.TypesUser
import com.castprogramms.balamutbatut.tools.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.koin.android.ext.android.inject

class SplashActivity : AppCompatActivity() {
    private val repository : Repository by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()
        val backgrounds = listOf(R.drawable.splactwp0, R.drawable.splactwp1, R.drawable.splactwp2, R.drawable.splactwp3, R.drawable.splactwp4, R.drawable.splactwp5, R.drawable.splactwp6, R.drawable.splactwp7, R.drawable.splactwp8, R.drawable.splactwp9, R.drawable.splactwp10)
        var view = findViewById<ConstraintLayout>(R.id.layout)
        view.setBackgroundResource(backgrounds.random())                  //рандомные фотографии на фон ставятся + значок баламута
        val googleAuth = GoogleSignIn.getLastSignedInAccount(this)
        Log.e("Data", googleAuth?.idToken.toString())
        if (googleAuth != null) {
            repository.loadUserData(googleAuth)
        } else{
            startActivity( Intent(
                this,
                MainActivity::class.java)
            )
            finish()
            User.mutableLiveDataSuccess.postValue(false)
        }
        repository.user.observe(this) {
            when (it) {
                is Resource.Error -> {
                    startActivity(Intent(this, MainActivity::class.java))
                    finish()
                }
                is Resource.Loading -> {
                    MainScope().launch {
                        while (true) {
                            findViewById<ImageView>(R.id.batut).apply {
                                rotation += 10
                                rotationY += 10
                                rotationX -= 10
                            }
                            delay(15)
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