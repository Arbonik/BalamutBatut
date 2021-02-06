package com.castprogramms.balamutbatut.ui.registr

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.castprogramms.balamutbatut.MainActivityStudent
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.users.Student
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task

class RegistrFragment: Fragment() {
    lateinit var registrViewModel: RegistrViewModel

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registrViewModel = RegistrViewModel()
        val view = inflater.inflate(R.layout.fragment_regist_google, container, false)
        registrViewModel.initGoogleSign(requireContext())
        val button : SignInButton = view.findViewById(R.id.sign_in_button)
        button.setOnClickListener {
            signIn()
        }
        return view
    }
    fun signIn(){
        startActivityForResult(Intent(registrViewModel.googleSignInClient.signInIntent), registrViewModel.SIGN_IN_CODE)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == registrViewModel.SIGN_IN_CODE){
            val task : Task<GoogleSignInAccount>
            = GoogleSignIn.getSignedInAccountFromIntent(data)
            task.addOnCompleteListener {
                if (it.isSuccessful){
                    handleSignInResult(task)
                }
                else{
                    DataUserFirebase.printLog(it.exception?.message.toString())
                }
            }
        }
    }

    fun handleSignInResult(task: Task<GoogleSignInAccount>){
        DataUserFirebase.printLog("handle: "+ task.isSuccessful)
        try {
            registrViewModel.account = task.getResult(ApiException::class.java)
            updateUI(registrViewModel.account)
        }catch (e:ApiException){
            DataUserFirebase.printLog("signInResult:failed code=" + e.statusCode)
            updateUI(null)
        }
    }
    private fun updateUI(isSignedIn: GoogleSignInAccount?){
        if (isSignedIn != null) {
            DataUserFirebase.printLog(isSignedIn.email.toString())
            (requireActivity() as MainActivityStudent).toMainGraph()
        }
        else
            DataUserFirebase.printLog("updateUI ERROR")
    }
}