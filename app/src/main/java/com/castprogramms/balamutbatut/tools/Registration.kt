package com.castprogramms.balamutbatut.tools

import android.content.Context
import android.content.Intent
import android.util.Log
import com.castprogramms.balamutbatut.users.Student
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.DocumentSnapshot
import com.google.gson.GsonBuilder
import org.w3c.dom.Document

class Registration {
    var account : GoogleSignInAccount? = null
    val auth = FirebaseAuth.getInstance()
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail().build()
    fun login(context: Context, data: Intent){
        val googleSignInClient = GoogleSignIn.getClient(context, gso)
        val task = GoogleSignIn.getSignedInAccountFromIntent(data)
        try {
            account = task.getResult(ApiException::class.java)
        }catch (e: ApiException){
            Log.e("Data", e.message.toString())
        }
        if (account != null){
            Log.e("Data", "Susses")
        }
    }

    fun login(email: String, password: String){

    }

    fun auth(account : GoogleSignInAccount): Boolean {
        Log.e("Data",account.id.toString())
        return updateUI(account)
    }

    fun auth(user : FirebaseUser?){
        if (user != null){
            User.id = user.uid
            DataUserFirebase().getStudent(User.id).addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    User.setValue(GsonBuilder().create().fromJson(it.result?.data.toString(), Student::class.java))
                    Log.e("Data", User.student.toString())
                }
                else
                    Log.e("Data", it.exception?.message.toString())
            }.continueWith {
                try {
                    DataUserFirebase().getStudentsGroup(User.id).addOnSuccessListener {
                        Log.e("Data", it.documents.first().data.toString())
                        User.setValue(User.student?.apply {
                            nameGroup = it.documents.first().getString("name").toString()
                        }!!)
                    }
                }catch (e:Exception){
                    Log.e("Data", e.message.toString())
                }
            }
        }
    }

    fun updateUI(isSignedIn: GoogleSignInAccount?): Boolean {
        if (isSignedIn != null) {
            User.id = isSignedIn.id.toString()
//          loadDate()
            DataUserFirebase().getStudent(User.id).addOnSuccessListener {
                if (it.data != null) {
                    User.mutableLiveDataSuccess.postValue(true)
                    Log.e("Data", it.data.toString())
                    if (it.getString("type").toString() == "student")
                        User.setValue(
                            GsonBuilder().create().fromJson(it.data.toString(), Student::class.java)
                        )
                    else{
                        if (it.getString("type").toString() == "trainer"){

                        }
                    }
                    Log.e("Data", User.student.toString())
                }
            }.continueWith {
                if (User.student != null) {
                    DataUserFirebase().getStudentsGroup(User.id).addOnSuccessListener {
                        Log.e("Data", it.documents.first().data.toString())
                        User.setValue(User.student?.apply {
                            nameGroup = it.documents.first().getString("name").toString()
                        }!!)
                    }
                }
            }
            if (User.student == null) {
                User.id = isSignedIn.id.toString()
                return false
            }
            return true
        }
        else {
            DataUserFirebase.printLog("updateUI ERROR")
            return false
        }
    }

    fun loadDate(){
        DataUserFirebase().getStudent(User.id).addOnSuccessListener {
            if (it.data != null) {
                User.setValue(
                    GsonBuilder().create().fromJson(it.data.toString(), Student::class.java)
                )
                Log.e("Data", User.student.toString())
            }
        }
        if (User.student != null) {
            DataUserFirebase().getStudentsGroup(User.id).addOnSuccessListener {
                Log.e("Data", it.documents.first().data.toString())
                User.setValue(User.student?.apply {
                    nameGroup = it.documents.first().getString("name").toString()
                }!!)
            }
        }
    }
}