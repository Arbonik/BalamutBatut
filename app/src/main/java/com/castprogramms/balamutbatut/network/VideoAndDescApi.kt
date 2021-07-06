package com.castprogramms.balamutbatut.network

import android.net.Uri
import androidx.lifecycle.MutableLiveData

interface VideoAndDescApi {
    fun downloadVideo(idVideo: String): MutableLiveData<Resource<Uri>>
    fun downloadDesc(idVideo: String): MutableLiveData<Resource<String>>
    fun loadVideo(video: Uri, idVideo: String): MutableLiveData<Resource<String>>
    fun loadDesc(desc: String, idVideo: String): MutableLiveData<Resource<String>>
}