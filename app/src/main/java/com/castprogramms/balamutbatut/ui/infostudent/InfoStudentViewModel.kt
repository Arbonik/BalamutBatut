package com.castprogramms.balamutbatut.ui.infostudent

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.Repository
import com.castprogramms.balamutbatut.tools.Element
import com.castprogramms.balamutbatut.users.Student

class InfoStudentViewModel(private val repository : Repository) : ViewModel() {

    val mutableLiveDataStudent = MutableLiveData<Student>()
    var mutableLiveElements = MutableLiveData<MutableList<Element>>()

    fun loadUserData(idStudent : String){
        repository.getStudent(idStudent).addSnapshotListener { value, error ->
            if (value != null){
                mutableLiveDataStudent.value = value.toObject(Student::class.java)
            }
        }
    }

    fun loadElementsData(IDs : List<Int>, nameGroupElement: String){
        mutableLiveElements = repository.getElement(IDs, nameGroupElement)
    }
    fun getTrueOrder() = repository.getTrueOrder()

    fun getGroupName(groupID: String) = repository.getNameGroup(groupID)
}