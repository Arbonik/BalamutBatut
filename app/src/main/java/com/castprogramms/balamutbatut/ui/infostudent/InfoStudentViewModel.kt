package com.castprogramms.balamutbatut.ui.infostudent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.Repository
import com.castprogramms.balamutbatut.users.Student

class InfoStudentViewModel(private val repository : Repository) : ViewModel() {

    val mutableLiveDataStudent = MutableLiveData<Student>()

    fun loadData(idStudent : String){
        repository.getStudent(idStudent).addSnapshotListener { value, error ->
            if (value != null){
                mutableLiveDataStudent.value = value.toObject(Student::class.java)
            }
        }
    }

}