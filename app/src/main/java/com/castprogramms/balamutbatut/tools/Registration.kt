package com.castprogramms.balamutbatut.tools

import android.content.Context
import android.content.Intent
import android.util.Log
import com.castprogramms.balamutbatut.users.Student
import com.castprogramms.balamutbatut.users.Trainer
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.gson.GsonBuilder

class Registration {
    var account : GoogleSignInAccount? = null
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

    fun auth(account : GoogleSignInAccount?): Boolean {
        Log.e("Data",account?.id.toString())
        Log.e("Data",account?.photoUrl.toString())
        return updateUI(account)
    }

    fun auth(user : FirebaseUser?){
        if (user != null){
            User.id = user.uid
            User.img = user.photoUrl.toString()
            DataUserFirebase().getStudent(User.id).addOnCompleteListener {
                if (it.isSuccessful && it.result != null) {
                    User.setValueStudent(GsonBuilder().create().fromJson(it.result?.data.toString(), Student::class.java))
                    Log.e("Data", User.student.toString())
                }
                else
                    Log.e("Data", it.exception?.message.toString())
            }.continueWith {
                try {
                    DataUserFirebase().getStudentsGroup(User.id).addOnSuccessListener {
                        Log.e("Data", it.documents.first().data.toString())
                        User.setValueStudent(User.student?.apply {
                            groupID = it.documents.first().getString("name").toString()
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
            User.img = isSignedIn.photoUrl.toString()
            DataUserFirebase().getStudent(User.id).addOnCompleteListener {
                if (it.isSuccessful) {
                    val data = it.result
                    if (data != null && data.data != null) {
                        User.typeUser = data.getString("type").toString()
                        Log.e("Data", "Type: ${User.typeUser}")
                        Log.e("Data", data.data.toString())
                        User.mutableLiveDataSuccess.postValue(true)
                        when (User.typeUser) {
                            "student" -> User.setValueStudent(
                                GsonBuilder().create().fromJson(
                                    data.data.toString(),
                                    Student::class.java
                                )
                            )
                            "trainer" -> User.setValueTrainer(
                                GsonBuilder().create()
                                    .fromJson(data.data.toString(), Trainer::class.java)
                            )
                        }
                        Log.e("Data", User.student.toString())
                        Log.e("Data", User.trainer.toString())
                    }
                    else{
                        Log.e("Data", it.exception?.message.toString())
                        User.mutableLiveDataSuccess.postValue(false)
                    }
                }
                else{
                    Log.e("Data", it.exception?.message.toString())
                    User.mutableLiveDataSuccess.postValue(false)
                }
            }.continueWith {
                if (User.student != null) {
                    DataUserFirebase().getStudentsGroup(User.id).addOnSuccessListener {
                        Log.e("Data", it.documents.first().data.toString())
                        User.setValueStudent(User.student?.apply {
                            groupID = it.documents.first().getString("name").toString()
                        }!!)
                    }
                }
            }
            return true
        }
        else {
            User.mutableLiveDataSuccess.postValue(false)
            DataUserFirebase.printLog("updateUI ERROR")
            return false
        }
    }

    fun loadDate(){
        DataUserFirebase().getStudent(User.id).addOnSuccessListener {
            if (it.data != null) {
                User.setValueStudent(
                    GsonBuilder().create().fromJson(it.data.toString(), Student::class.java)
                )
                Log.e("Data", User.student.toString())
            }
        }
        if (User.student != null) {
            DataUserFirebase().getStudentsGroup(User.id).addOnSuccessListener {
                Log.e("Data", it.documents.first().data.toString())
                User.setValueStudent(User.student?.apply {
                    groupID = it.documents.first().getString("name").toString()
                }!!)
            }
        }
    }
}