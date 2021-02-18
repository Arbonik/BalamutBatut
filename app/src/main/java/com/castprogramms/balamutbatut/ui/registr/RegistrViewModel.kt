package com.castprogramms.balamutbatut.ui.registr

import android.content.Context
import android.util.Log
import android.util.Patterns
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.tools.Registration
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.users.Student
import com.castprogramms.balamutbatut.graph.Node
import com.castprogramms.balamutbatut.tools.NodeData
import com.google.android.gms.auth.api.signin.*
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.gson.GsonBuilder

class RegistrViewModel: ViewModel() {
    val SIGN_IN_CODE = 7
    val auth: FirebaseAuth = FirebaseAuth.getInstance()
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail().build()
    val registration = Registration()

    lateinit var googleSignInClient : GoogleSignInClient
    var account : GoogleSignInAccount? = null

    fun initGoogleSign(context: Context){
        googleSignInClient = GoogleSignIn.getClient(context, gso)
        Log.e("Test", googleSignInClient.toString())
    }

    private val _loginForm = MutableLiveData<LoginFormState>()
    val loginFormState: LiveData<LoginFormState> = _loginForm

    fun loginDataChanged(username: String, password: String) {
        if (!isUserNameValid(username)) {
            _loginForm.value = LoginFormState(usernameError = R.string.invalid_username)
        } else if (!isPasswordValid(password)) {
            _loginForm.value = LoginFormState(passwordError = R.string.invalid_password)
        } else {
            _loginForm.value = LoginFormState(isDataValid = true)
        }
    }

    private fun isUserNameValid(username: String): Boolean {
        return if (username.contains('@')) {
            Patterns.EMAIL_ADDRESS.matcher(username).matches()
        } else {
            username.isNotBlank()
        }
    }

    private fun isPasswordValid(password: String): Boolean {
        return password.length > 5
    }

    fun handleSignInResult(task: Task<GoogleSignInAccount>): Boolean {
        DataUserFirebase.printLog("handle: "+ task.isSuccessful)
        try {
            account = task.getResult(ApiException::class.java)
            return registration.updateUI(account)
        }catch (e:ApiException){
            DataUserFirebase.printLog("signInResult:failed code=" + e.statusCode)
            return registration.updateUI(null)
        }
    }

    private fun updateUI(isSignedIn: GoogleSignInAccount?): Boolean {
        if (isSignedIn != null) {
            User.id = auth.currentUser?.uid.toString()
//            DataUserFirebase().addStudent(
//                Student("Александр", "Звездаков", "23-10-2003",
//                List(4) { Node(if (it != 4 - 1) mutableListOf(it + 1) else mutableListOf(),
//                NodeData(mutableListOf("qwerty"))) }, ),
//                User.id)
            DataUserFirebase().getStudent(User.testID).addOnCompleteListener {
                if (it.isSuccessful) {
                    User.setValue(GsonBuilder().create().fromJson(it.result?.data.toString(), Student::class.java))
                    Log.e("Data", User.student.toString())
                }
                else
                    Log.e("Data", it.exception?.message.toString())
            }
            return true
        }
        else{
            DataUserFirebase.printLog("updateUI ERROR")
            return false
        }
    }

}