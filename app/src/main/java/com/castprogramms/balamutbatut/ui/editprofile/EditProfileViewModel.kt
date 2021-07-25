package com.castprogramms.balamutbatut.ui.editprofile

import android.net.Uri
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.network.Repository
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.TypesUser
import com.castprogramms.balamutbatut.tools.User

class EditProfileViewModel(private val repository: Repository): ViewModel() {
    val lifeDataLoadPhoto = MutableLiveData<Resource<String>>()

    fun loadPhotoUser(uri: Uri, userId: String) {
        repository.loadPhotoUser(uri, userId).observeForever {
            lifeDataLoadPhoto.postValue(it)
        }
    }

    fun updateUserFirstName(firstName: String, id: String) = repository.updateUserFirstName(firstName, id)
    fun updateUserSecondName(secondName: String, id: String) = repository.updateUserSecondName(secondName, id)

}