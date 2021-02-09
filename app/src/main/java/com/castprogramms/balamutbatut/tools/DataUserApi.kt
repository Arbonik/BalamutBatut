package com.castprogramms.balamutbatut.tools

import com.castprogramms.balamutbatut.Group
import com.castprogramms.balamutbatut.users.Student

interface DataUserApi {
    fun addStudent(student: Student, group: Group, studentID: String)

    fun updateStudent(student: Student)

    fun deleteStudent(student: Student)

    fun readAllStudent(group: Group):MutableList<Student>

    fun addGroup(group: Group)

    fun getGroups()
}