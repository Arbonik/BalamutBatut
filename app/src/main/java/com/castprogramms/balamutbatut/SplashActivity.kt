package com.castprogramms.balamutbatut

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.castprogramms.balamutbatut.tools.Registration
import com.castprogramms.balamutbatut.tools.TypesUser
import com.castprogramms.balamutbatut.tools.User
import com.google.android.gms.auth.api.signin.GoogleSignIn

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        supportActionBar?.hide()


        val googleAuth = GoogleSignIn.getLastSignedInAccount(this)
        Registration().auth(googleAuth)

        User.mutableLiveDataSuccess.observe(this, Observer{
            if (it != null) {
                if (it == true) {
                    when(User.typeUser){
                        TypesUser.TRAINER -> {
                            val intent = Intent(this, MainActivityTrainer::class.java)
                            startActivity(intent)
                        }
                        TypesUser.STUDENT ->{
                        val intent = Intent(this@SplashActivity, MainActivityStudent::class.java)
                        intent.putExtra("susses", it)
                        startActivity(intent)
                        finish()
                        }
                    }
                }
                else{
                    startActivity(
                        Intent(
                            this,
                            MainActivity::class.java
                        )
                    )
                }
            }
        })

    }
}