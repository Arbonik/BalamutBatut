package com.castprogramms.balamutbatut.ui.registr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.castprogramms.balamutbatut.MainActivity
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.network.DataUserFirebase
import com.castprogramms.balamutbatut.tools.TypesUser
import com.castprogramms.balamutbatut.tools.User
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import com.google.android.gms.tasks.Task
import org.koin.androidx.viewmodel.ext.android.viewModel

class RegistrFragment: Fragment() {
    val registrViewModel: RegistrViewModel by viewModel()
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
            Log.d("OPA", it.toString())
            if (sussesRegistr && it == true) {
                when (User.typeUser) {
                    TypesUser.STUDENT -> (requireActivity() as MainActivity).toStudent()
                    TypesUser.TRAINER -> (requireActivity() as MainActivity).toTrainer()
                    TypesUser.NOTHING -> {}
                }
            } else {
                if (it == false && User.id != "" && !sussesRegistr) {
                    findNavController()
                        .navigate(R.id.action_registrFragment2_to_insertDataUserFragment2)
                }
            }
        })

        return view
    }
    fun signIn(){
        try {
            startActivityForResult(Intent(
                GoogleSignIn.getClient(requireActivity(),
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
                    registrViewModel.handleSignInResult(task)
                    registrViewModel.getUser().observe(viewLifecycleOwner, {
                        when(it){
                            is Resource.Error -> {
                                sussesRegistr = false
                            }
                            is Resource.Loading -> {}
                            is Resource.Success -> {
                                if (!sussesRegistr) {
                                    sussesRegistr = true
                                    User.mutableLiveDataSuccess.postValue(User.mutableLiveDataSuccess.value)
                                }
                            }
                        }
                    })
                }
                else{
                    DataUserFirebase.printLog(it.exception?.message.toString())
                }
            }
        }
    }

}