package com.castprogramms.balamutbatut.tools

import com.castprogramms.balamutbatut.Group
import com.castprogramms.balamutbatut.users.Student
import com.castprogramms.balamutbatut.users.Trainer
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

interface DataUserApi {
    fun addStudent(student: Student, studentID: String)

    fun addTrainer(trainer: Trainer, studentID: String)

    fun updateStudent(student: Student, studentID: String)

    fun deleteStudent(student: Student)

    fun readAllStudent(group: Group):MutableList<Student>

    fun addGroup(group: Group)

    fun getGroups(): Query

    fun editNameStudent(first_name: String, studentID: String)

    fun editLastNameStudent(second_name: String, studentID: String)

    fun editIconStudent(icon: String, studentID: String)
}