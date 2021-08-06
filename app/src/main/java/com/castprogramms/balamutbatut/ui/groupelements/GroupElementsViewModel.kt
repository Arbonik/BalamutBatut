package com.castprogramms.balamutbatut.ui.groupelements

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.network.Repository
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.users.Student

class GroupElementsViewModel(private val repository: Repository): ViewModel() {
    fun getSortedStudentTitleElementsWithColor(idElements: Map<String, List<Int>>) =
        repository.getAddStudentTitleElementsWithColor(idElements)
    var lifeData = MutableLiveData<Resource<MutableList<Pair<String, Int>>>>()
    fun getStudent(idStudent: String){
        repository.getStudent(idStudent)
            .addSnapshotListener { value, _ ->
                if (value != null)
                    getSortedStudentTitleElementsWithColor((value.toObject(Student::class.java))!!.element)
                        .observeForever {
                            lifeData.postValue(it)
                        }
            }
    }

    fun getAddElementsStudent(idStudent: String){
        repository.getStudent(idStudent)
            .addSnapshotListener { value, _ ->
                if (value != null)
                    repository.getStudentTitleElementsWithColor((value.toObject(Student::class.java))!!.element)
                        .observeForever {
                            lifeData.postValue(it)
                        }
            }
    }
}