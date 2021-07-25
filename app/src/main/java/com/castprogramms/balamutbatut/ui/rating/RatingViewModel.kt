package com.castprogramms.balamutbatut.ui.rating

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.network.Repository
import com.castprogramms.balamutbatut.network.Resource

class RatingViewModel(private val repository: Repository) : ViewModel() {
    val lifeDataNameTrainer = MutableLiveData<Resource<String>>()

    fun getAllStudent() = repository.getAllStudents()
    fun getStudentGroups(groupId: String) = repository.getStudentsOfGroup(groupId)
    fun getRang(id: String) = repository.getRang(id)
    fun getGroup(idGroup: String) = repository.getGroup(idGroup)
    fun getFullNameTrainer(trainerID: String) {
        repository.getFullNameTrainer(trainerID).observeForever {
            lifeDataNameTrainer.postValue(it)
        }
    }
}
