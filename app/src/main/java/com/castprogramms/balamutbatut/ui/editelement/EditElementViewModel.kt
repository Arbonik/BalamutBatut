package com.castprogramms.balamutbatut.ui.editelement

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.network.Repository
import com.castprogramms.balamutbatut.network.Resource

class EditElementViewModel(private val repository: Repository): ViewModel() {
    val lifeDataDescAndLevel = MutableLiveData<Resource<Pair<String, String>>>()
    val liveDataHaveVideo = MutableLiveData<Resource<Boolean>>()

    fun downloadDescAndLevel(title: String, name: String) {
        repository.downloadDescAndLevel(title, name).observeForever {
            lifeDataDescAndLevel.postValue(it)
        }
    }
    fun checkHaveVideo(title: String, name: String){
        repository.checkHaveVideo(title, name).observeForever {
            liveDataHaveVideo.postValue(it)
        }
    }


    fun loadVideoNameDecs(titleElement: String, nameElement: String, video: Uri,
                          desc: String, level: String) = repository
        .loadVideoNameDecs(titleElement, nameElement, video, desc, level)
}