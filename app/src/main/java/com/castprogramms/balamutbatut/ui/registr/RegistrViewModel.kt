package com.castprogramms.balamutbatut.ui.registr

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.Repository
import com.castprogramms.balamutbatut.tools.DataLoader
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class RegistrViewModel: ViewModel() {
    val SIGN_IN_CODE = 7

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail().build()
    lateinit var repository: Repository
    fun init(repository: Repository){
        this.repository = repository
    }
    val registration = DataLoader(repository)

    lateinit var googleSignInClient : GoogleSignInClient
    var account : GoogleSignInAccount? = null

    fun initGoogleSign(context: Context){
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun signOut(){
        googleSignInClient.signOut().addOnSuccessListener {
            Log.d("as", "asas")
        }
    }

    fun handleSignInResult(task: Task<GoogleSignInAccount>): Boolean {
        try {
            account = task.getResult(ApiException::class.java)
            return registration.loadUserData(account)
        }catch (e:ApiException){
            return registration.loadUserData(null)
        }
    }
}