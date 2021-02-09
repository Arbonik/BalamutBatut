package com.castprogramms.balamutbatut.ui.registr

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import com.castprogramms.balamutbatut.MainActivity
import com.castprogramms.balamutbatut.MainActivityStudent
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.users.Student
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.gson.GsonBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

val USER_UUID_TAG = "userUUID"

class RegistrFragment: Fragment() {

    private lateinit var auth: FirebaseAuth

    lateinit var registrViewModel: RegistrViewModel

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

        auth = FirebaseAuth.getInstance()

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

                auth.createUserWithEmailAndPassword(userLogin, userPass)
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
                        auth.signInWithEmailAndPassword(userLogin, userPass)
                            .addOnCompleteListener { task ->
                                if (task.isSuccessful) {
                                    Toast.makeText(
                                        requireContext(), resources.getString(R.string.welcome),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    successLogin(auth.currentUser)
                                }
                            }
                    }
            }
        }
        return view
    }
    override fun onStart() {
        super.onStart()
        if (auth.currentUser != null)
                successLogin(auth.currentUser)
        }

        fun successLogin(user: FirebaseUser?) {
            val bundle = Bundle().apply {
                putString(USER_UUID_TAG, user?.uid)
        }
            DataUserFirebase().getStudent("UwsuyZ4DB4J8b1AbRbE9").addOnCompleteListener {
                if (it.isSuccessful) {
                    User.setValue(GsonBuilder().create().fromJson(it.result?.data.toString(), Student::class.java))
                    Log.e("Data", User.student.toString())
                }
                else
                    Log.e("Data", it.exception?.message.toString())
            }
            Log.i(TAG, "login with email")
            (requireActivity() as MainActivityStudent).toMainGraph()
    }

    fun signIn(){
        Log.i(TAG, "Open google Intent")
        try {
            startActivityForResult(Intent(GoogleSignIn.getClient(requireActivity(), registrViewModel.gso).signInIntent),
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
            User.id = isSignedIn.id.toString()
            DataUserFirebase().getStudent("UwsuyZ4DB4J8b1AbRbE9").addOnCompleteListener {
                if (it.isSuccessful) {
                    User.setValue(GsonBuilder().create().fromJson(it.result?.data.toString(), Student::class.java))
                    Log.e("Data", User.student.toString())
                }
                else
                    Log.e("Data", it.exception?.message.toString())
            }
            (requireActivity() as MainActivityStudent).toMainGraph()
        }
        else
            DataUserFirebase.printLog("updateUI ERROR")
    }
    companion object{
        var TAG = "Register"
    }
}

fun EditText.afterTextChanged(afterTextChanged: (String) -> Unit) {
    this.addTextChangedListener(object : TextWatcher {
        override fun afterTextChanged(editable: Editable?) {
            afterTextChanged.invoke(editable.toString())
        }

        override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
        override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {}
    })
}
