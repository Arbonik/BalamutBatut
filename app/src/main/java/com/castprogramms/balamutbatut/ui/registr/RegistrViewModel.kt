package com.castprogramms.balamutbatut.ui.registr

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth

class RegistrViewModel: ViewModel() {
    val SIGN_IN_CODE = 7
    val firebaseAuth: FirebaseAuth = FirebaseAuth.getInstance()
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail().build()
    lateinit var googleSignInClient : GoogleSignInClient
    var account : GoogleSignInAccount? = null
    fun initGoogleSign(context: Context){
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

}