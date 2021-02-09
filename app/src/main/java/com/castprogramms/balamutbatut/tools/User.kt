package com.castprogramms.balamutbatut.tools

import androidx.lifecycle.MutableLiveData
import com.castprogramms.balamutbatut.users.Student

object User {
    var id = ""

    var student : Student? = null

    val mutableLiveDataStudent = MutableLiveData(student)

    fun setValue(student: Student){
        this.student = student
        mutableLiveDataStudent.value = student
    }
}