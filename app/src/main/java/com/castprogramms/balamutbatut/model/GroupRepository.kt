package com.castprogramms.balamutbatut.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.castprogramms.balamutbatut.network.Resource

class GroupRepository {
    // place to firebaseInstance
    fun getGroup(id : Long? = null) : LiveData<Resource<Group>>{
        val mutableLiveData = MutableLiveData<Resource<Group>>(null)

        mutableLiveData.postValue(Resource.Loading<Group>())
        // here post Resource.Success or Resource.Error

        return mutableLiveData
    }
}
