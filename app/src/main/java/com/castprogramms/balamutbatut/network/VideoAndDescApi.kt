package com.castprogramms.balamutbatut.network

import android.net.Uri
import androidx.lifecycle.MutableLiveData

interface VideoAndDescApi {
    fun downloadVideo(titleElement: String, nameElement: String): MutableLiveData<Resource<Uri>>
    fun downloadDescAndLevel(title: String, name: String): MutableLiveData<Resource<Pair<String, String>>>
    fun loadVideo(video: Uri, idVideo: String): MutableLiveData<Resource<String>>
    fun loadDesc(desc: String, idVideo: String): MutableLiveData<Resource<String>>
    fun haveVideo(title: String, name: String): MutableLiveData<Resource<Boolean>>
    fun loadVideoNameDecs(titleElement: String, nameElement: String, video: Uri, desc: String,
                          level: String): MutableLiveData<Resource<String>>
}