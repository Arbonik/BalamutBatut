package com.castprogramms.balamutbatut.tools

import androidx.lifecycle.MutableLiveData
import com.castprogramms.balamutbatut.users.Person
import com.castprogramms.balamutbatut.users.Student
import com.castprogramms.balamutbatut.users.Trainer

object User {
    var id = ""
    val testID = "UwsuyZ4DB4J8b1AbRbE9a"

    var student : Student? = null
    var trainer : Trainer? = null

    val mutableLiveDataStudent = MutableLiveData(student)
    val mutableLiveDataTrainer = MutableLiveData(trainer)

    fun setValueStudent(student: Student){
        this.student = student
        mutableLiveDataStudent.value = student
    }
    fun setValueTrainer(trainer: Trainer){
        this.trainer = trainer
        mutableLiveDataTrainer.value = trainer
    }

    val mutableLiveDataSuccess = MutableLiveData(false)
}