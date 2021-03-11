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

    lateinit var registrViewModel: RegistrViewModel
    var sussesRegistr = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        registrViewModel = RegistrViewModel()
        val view = inflater.inflate(R.layout.fragment_regist, container, false)
        registrViewModel.initGoogleSign(requireContext())
        val button : SignInButton = view.findViewById(R.id.sign_in_button)
        button.setOnClickListener {
            signIn()
        }
        User.mutableLiveDataSuccess.observe(viewLifecycleOwner, Observer {
            if (sussesRegistr && it == true){
                when(User.typeUser){
                    "student" -> (requireActivity() as MainActivity).toStudent()
                    "trainer" -> (requireActivity() as MainActivity).toTrainer()
                }
            }
            else{
                if (it == false && User.id != ""){
                    findNavController()
                        .navigate(R.id.action_registrFragment2_to_insertDataUserFragment2)
                }
            }
        })

        val username = view.findViewById<EditText>(R.id.email)
        val password = view.findViewById<EditText>(R.id.password)
        val login = view.findViewById<Button>(R.id.login)

        registrViewModel = ViewModelProvider(this)
            .get(registrViewModel::class.java)

        registrViewModel.loginFormState.observe(requireActivity(), Observer {
            val loginState = it ?: return@Observer

            // disable login button unless both username / password is valid
            login.isEnabled = loginState.isDataValid

            if (loginState.usernameError != null) {
                username.error = getString(loginState.usernameError)
            }
            if (loginState.passwordError != null) {
                password.error = getString(loginState.passwordError)
            }
        })

        /*username.afterTextChanged {
            registrViewModel.loginDataChanged(
                username.text.toString(),
                password.text.toString()
            )
        }*/

        password.apply {
            /*afterTextChanged {
                registrViewModel.loginDataChanged(
                    username.text.toString(),
                    password.text.toString()
                )
            }*/

            login.setOnClickListener {
                val userLogin = username.text.toString()
                val userPass = password.text.toString()

                registrViewModel.auth.createUserWithEmailAndPassword(userLogin, userPass)
                    .addOnCompleteListener(requireActivity()) { task ->
                        if (task.isSuccessful) {
                            Toast.makeText(
                                requireContext(), context.getString(R.string.success_register),
                                Toast.LENGTH_SHORT
                            ).show()
                        } else {
                            if (task.exception != null)
                                Toast.makeText(
                                    requireContext(), task.exception!!.message.toString(),
                                    Toast.LENGTH_SHORT
                                ).show()
                        }
                    }
                    .addOnFailureListener { exception ->
                        registrViewModel.auth.signInWithEmailAndPassword(userLogin, userPass)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        requireContext(), resources.getString(R.string.welcome),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    successLogin(registrViewModel.auth.currentUser)
                                }
                            }
                    }
            }
        }
        return view
    }
    override fun onStart() {
        super.onStart()
//        val activityStudent = requireActivity() as MainActivity
//        if (activityStudent.intent.getBooleanExtra("susses", false))
//            activityStudent.toStudent()
    }

        fun successLogin(user: FirebaseUser?) {
//            val bundle = Bundle().apply {
//                putString(USER_UUID_TAG, user?.uid) }
            Registration().auth(user)
            Log.i(TAG, "login with email")
            (requireActivity() as MainActivity).toStudent()
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
//                    if (){
//                        when(User.typeUser){
//                            "student" -> (requireActivity() as MainActivity).toStudent()
//                            "trainer" -> (requireActivity() as MainActivity).toTrainer()
//                        }
//                    }
//                        (requireActivity() as MainActivity).toStudent()
//                    else
//                        this.findNavController()
//                            .navigate(R.id.action_registrFragment_to_insertDataUserFragment)
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