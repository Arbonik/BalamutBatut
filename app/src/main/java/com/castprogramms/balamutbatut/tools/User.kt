package com.castprogramms.balamutbatut.tools

import androidx.lifecycle.MutableLiveData
import com.castprogramms.balamutbatut.users.Student
import com.castprogramms.balamutbatut.users.Trainer

object User {
    var referId = ""
    var isScan: Boolean = false
    var id = ""
    var img = ""
    var typeUser = TypesUser.NOTHING

    var student: Student? = null
    var trainer: Trainer? = null

    val mutableLiveDataStudent = MutableLiveData(student)
    val mutableLiveDataTrainer = MutableLiveData(trainer)

    fun setValueStudent(student: Student?) {
        this.student = student
        mutableLiveDataStudent.postValue(student)
    }

    fun setValueTrainer(trainer: Trainer?) {
        this.trainer = trainer
        mutableLiveDataTrainer.postValue(trainer)
    }

    val mutableLiveDataSuccess = MutableLiveData<Boolean?>(null)

    fun clearAll() {
        when (typeUser) {
            TypesUser.STUDENT -> setValueStudent(null)
            TypesUser.TRAINER -> setValueTrainer(null)
            TypesUser.NOTHING -> {
            }
        }
        mutableLiveDataStudent.postValue(null)
        typeUser = TypesUser.NOTHING
        id = ""
        img = ""
    }
}