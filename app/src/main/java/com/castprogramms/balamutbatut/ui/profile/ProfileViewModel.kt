package com.castprogramms.balamutbatut.ui.profile

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.network.Repository
import com.castprogramms.balamutbatut.network.Resource

class ProfileViewModel(private val repository: Repository) : ViewModel() {
    val liveDataNameGroup = MutableLiveData("")
    var liveDataRang = MutableLiveData<Resource<String>>()
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
    fun getRang(id: String){
        liveDataRang = repository.getRang(id)
    }
}