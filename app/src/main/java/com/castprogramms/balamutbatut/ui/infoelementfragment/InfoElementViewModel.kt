package com.castprogramms.balamutbatut.ui.infoelementfragment

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.network.Repository
import com.castprogramms.balamutbatut.network.Resource

class InfoElementViewModel(private val repository: Repository): ViewModel() {
    val lifeDataVideo = MutableLiveData<Resource<Uri>>()
    val lifeDataDescAndLevel = MutableLiveData<Resource<Pair<String, String>>>()

    fun downloadVideo(title: String, name: String){
        repository.downloadVideo(title, name).observeForever {
            lifeDataVideo.postValue(it)
        }
    }

    fun downloadDescAndLevel(title: String, name: String) {
        repository.downloadDescAndLevel(title, name).observeForever {
            lifeDataDescAndLevel.postValue(it)
        }
    }
}