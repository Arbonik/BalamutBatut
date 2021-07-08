package com.castprogramms.balamutbatut.ui.editelement

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.network.Repository

class EditElementViewModel(private val repository: Repository): ViewModel() {
    fun loadVideo(video: Uri, idVideo: String) = repository.loadVideo(video, idVideo)
    fun loadDesc(desc: String, idVideo: String) = repository.loadDesc(desc, idVideo)

    fun downloadVideo(idVideo: String) = repository.downloadVideo(idVideo)
    fun downloadDesc(idVideo: String) = repository.downloadDesc(idVideo)

    fun loadVideoAndDesc(desc: String, video: Uri, idVideo: String) =
        repository.loadVideoAndDesc(desc, video, idVideo)
}