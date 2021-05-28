package com.castprogramms.balamutbatut.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.Repository

class ProfileViewModel(private val repository: Repository) : ViewModel() {
    val liveDataNameGroup = MutableLiveData("")
    fun getGroupStudent(id: String){
        if (liveDataNameGroup.value == "") {
            repository.getNameGroup(id).addOnCompleteListener {
                if (it.isSuccessful) {
                    if (it.result.get("name") != null)
                        liveDataNameGroup.postValue(it.result.get("name").toString())
                }
            }
        }
    }
}