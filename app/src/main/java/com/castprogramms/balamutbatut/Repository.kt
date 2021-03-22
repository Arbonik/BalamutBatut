package com.castprogramms.balamutbatut

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.tools.TypesUser
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.users.Person
import com.castprogramms.balamutbatut.users.Student
import com.castprogramms.balamutbatut.users.Trainer
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.gson.GsonBuilder

class Repository {

    private val _userData = MutableLiveData<Resource<out Person>>(null)
    val user: LiveData<Resource<out Person>> = _userData

    fun loadUserData(account: GoogleSignInAccount?) {
        _userData.postValue(Resource.Loading())

        if (account != null) {
            var person = Person()
            val id = account.id.toString()
            User.id = id

            DataUserFirebase().getUser(id).get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val data = it.result
                    if (data != null && data.data != null) {
                        person.type = data.getString("type").toString()

                        User.mutableLiveDataSuccess.postValue(true)
                        when (User.typeUser) {
                            TypesUser.STUDENT ->
                                person = data.toObject(Student::class.java)!!

                            TypesUser.TRAINER ->
                                person = data.toObject(Trainer::class.java)!!
                        }

                        person.img = account.photoUrl.toString()
                        _userData.postValue(Resource.Success(person))

                    }
                } else {
                    _userData.postValue(Resource.Error(""))
                }
            }.continueWith {
                if (User.student != null) {
                    DataUserFirebase().getStudentsGroup(User.id).addOnSuccessListener {
                        if (it.documents.isNotEmpty()) {
                            User.setValueStudent(User.student?.apply {
                                groupID = it.documents.first().getString("name").toString()
                            }!!)
                        }
                    }
                }
            }
                .addOnFailureListener {

                    _userData.postValue(Resource.Error("Вы не зарегестрированы"))
                }
        }


    }
}