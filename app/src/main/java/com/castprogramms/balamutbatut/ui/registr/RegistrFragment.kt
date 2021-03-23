package com.castprogramms.balamutbatut.ui.registr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.castprogramms.balamutbatut.MainActivity
import com.castprogramms.balamutbatut.MainActivityStudent
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.tools.Registration
import com.castprogramms.balamutbatut.tools.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseUser

class RegistrFragment: Fragment() {
    val registrViewModel: RegistrViewModel by viewModels()
    var sussesRegistr = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_regist, container, false)
        registrViewModel.initGoogleSign(requireContext())
        val button: SignInButton = view.findViewById(R.id.sign_in_button)
        button.setOnClickListener {
            signIn()
        }
        User.mutableLiveDataSuccess.observe(viewLifecycleOwner, Observer {
            if (sussesRegistr && it == true) {
                when (User.typeUser) {
                    "student" -> (requireActivity() as MainActivity).toStudent()
                    "trainer" -> (requireActivity() as MainActivity).toTrainer()
                }
            } else {
                if (it == false && User.id != "") {
                    findNavController()
                        .navigate(R.id.action_registrFragment2_to_insertDataUserFragment2)
                }
            }
        })

d
        return view
    }
    fun signIn(){
        Log.i(TAG, "Open google Intent")
        try {
            startActivityForResult(Intent(GoogleSignIn.getClient(requireActivity(),
                registrViewModel.gso).signInIntent),
                registrViewModel.SIGN_IN_CODE)
        }catch (e:Exception){
            Log.e("Test", e.message.toString())
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == registrViewModel.SIGN_IN_CODE){
            val task : Task<GoogleSignInAccount>
            = GoogleSignIn.getSignedInAccountFromIntent(data)
            task.addOnCompleteListener {
                if (it.isSuccessful){
                    sussesRegistr = registrViewModel.handleSignInResult(task)
                }
                else{
                    DataUserFirebase.printLog(it.exception?.message.toString())
                }
            }
        }
    }

    companion object{
        val TAG = "Register"
        val USER_UUID_TAG = "userUUID"
    }
}