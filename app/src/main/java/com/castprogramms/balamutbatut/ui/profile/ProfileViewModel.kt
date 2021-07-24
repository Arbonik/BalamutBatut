package com.castprogramms.balamutbatut.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.network.Repository
import com.castprogramms.balamutbatut.network.Resource

class ProfileViewModel(private val repository: Repository) : ViewModel() {
    val lifeDataPlace = MutableLiveData<Resource<Int>>()

    fun getPlaceStudentInRating(studentId: String){
        repository.getPlaceStudentInRating(studentId).observeForever {
            lifeDataPlace.postValue(it)
        }
    }
}