package com.castprogramms.balamutbatut.network

import androidx.lifecycle.MutableLiveData
import com.castprogramms.balamutbatut.tools.Group
import com.castprogramms.balamutbatut.users.Student
import com.castprogramms.balamutbatut.users.Trainer
import com.google.firebase.firestore.Query

interface DataUserApi {
    fun addStudent(student: Student, studentID: String)

    fun addTrainer(trainer: Trainer, studentID: String)

    fun updateStudent(studentID: String, groupID: String)

    fun deleteStudent(student: Student)

    fun readAllStudent(group: Group):MutableList<Student>

    fun addGroup(group: Group)

    fun getGroups(): Query

    fun editNameStudent(first_name: String, studentID: String)

    fun editLastNameStudent(second_name: String, studentID: String)

    fun editIconStudent(icon: String, studentID: String)

    fun deleteStudentFromGroup(studentID: String, groupID: String)

    fun getTrueOrder(): MutableLiveData<Resource<List<String>>>

    fun addBatutCoin(quantity: Int, id: String)

    fun writeOffCoin(quantity: Int, id: String): MutableLiveData<Resource<Boolean>>
}