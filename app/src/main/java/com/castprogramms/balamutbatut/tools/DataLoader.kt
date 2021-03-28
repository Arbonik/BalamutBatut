package com.castprogramms.balamutbatut.toolsimport com.castprogramms.balamutbatut.users.Studentimport com.castprogramms.balamutbatut.users.Trainerimport com.google.android.gms.auth.api.signin.GoogleSignInAccountimport com.google.gson.GsonBuilderclass DataLoader {    fun loadUserData(isSignedIn: GoogleSignInAccount?): Boolean {        if (isSignedIn != null) {            User.id = isSignedIn.id.toString()            User.img = isSignedIn.photoUrl.toString()            DataUserFirebase().getUser(User.id).get().addOnCompleteListener {                if (it.isSuccessful) {                    val data = it.result                    if (data != null && data.data != null) {                        when (data.getString("type").toString()) {                            TypesUser.STUDENT.desc -> User.typeUser = TypesUser.STUDENT                            TypesUser.TRAINER.desc -> User.typeUser = TypesUser.TRAINER                        }                        User.mutableLiveDataSuccess.postValue(true)                        when (User.typeUser) {                            TypesUser.STUDENT -> User.setValueStudent(                                data.toObject(Student::class.java)!!                            )                            TypesUser.TRAINER -> User.setValueTrainer(                                data.toObject(Trainer::class.java)!!                            )                            TypesUser.NOTHING -> {}                        }                    } else {                        User.mutableLiveDataSuccess.postValue(false)                    }                } else {                    User.mutableLiveDataSuccess.postValue(false)                }            }.continueWith {                if (User.student != null) {                    DataUserFirebase().getStudentsGroup(User.id).addOnSuccessListener {                        if (it.documents.isNotEmpty()) {                            User.setValueStudent(User.student?.apply {                                groupID = it.documents.first().getString("name").toString()                            }!!)                        }                    }                }            }            return true        }        else {            User.mutableLiveDataSuccess.postValue(false)            return false        }    }}