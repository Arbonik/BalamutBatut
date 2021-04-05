package com.castprogramms.balamutbatut.ui.registr

import android.content.Context
import android.util.Log
import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.Repository
import com.castprogramms.balamutbatut.tools.DataLoader
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class RegistrViewModel(private val repository: Repository) : ViewModel() {
    val SIGN_IN_CODE = 7

    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail().build()
    val registration = DataLoader()

    lateinit var googleSignInClient: GoogleSignInClient
    var account: GoogleSignInAccount? = null

    fun initGoogleSign(context: Context) {
        googleSignInClient = GoogleSignIn.getClient(context, gso)
    }

    fun signOut() {
        googleSignInClient.signOut()//.addOnSuccessListener {   }
    }

    fun getUser() = repository.user

    fun handleSignInResult(task: Task<GoogleSignInAccount>): Boolean {
        try {
            account = task.getResult(ApiException::class.java)
            return repository.loadUserData(account)
        } catch (e: ApiException) {
            return repository.loadUserData(null)
        }
    }
}