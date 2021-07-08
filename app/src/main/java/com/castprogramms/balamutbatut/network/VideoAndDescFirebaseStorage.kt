package com.castprogramms.balamutbatut.network

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.castprogramms.balamutbatut.BuildConfig
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage

class VideoAndDescFirebaseStorage: VideoAndDescApi {
    private val storage = FirebaseStorage.getInstance(BuildConfig.STORAGE_BUCKET)
    val ref = storage.reference

    val settings = FirebaseFirestoreSettings.Builder()
        .setPersistenceEnabled(true)
        .build()
    val fireStore = FirebaseFirestore.getInstance().apply {
        firestoreSettings = settings
    }

    override fun downloadVideo(idVideo: String): MutableLiveData<Resource<Uri>> {
        val mutableLiveData = MutableLiveData<Resource<Uri>>(Resource.Loading())
        ref.child(videoTag + idVideo).downloadUrl.addOnCompleteListener {
            if (it.isSuccessful)
                mutableLiveData.postValue(Resource.Success(it.result))
            else
                if (it.isCanceled)
                    mutableLiveData.postValue(Resource.Error(it.exception?.message))
        }
        return mutableLiveData
    }

    override fun downloadDesc(idVideo: String): MutableLiveData<Resource<String>> {
        val mutableLiveData = MutableLiveData<Resource<String>>(Resource.Loading())
        fireStore.collection(elementsDescTag)
            .document(idVideo)
            .addSnapshotListener { value, error ->
                if (value != null)
                    mutableLiveData.postValue(Resource.Success(value.getString(descTag).toString()))
                else
                    mutableLiveData.postValue(Resource.Error(error?.message))
            }
        return mutableLiveData
    }

    override fun loadVideo(video: Uri, idVideo: String): MutableLiveData<Resource<String>> {
        val mutableLiveData = MutableLiveData<Resource<String>>(Resource.Loading())
        ref.child(videoTag + idVideo).putFile(video).addOnCompleteListener{
            if (it.isSuccessful)
                mutableLiveData.postValue(Resource.Success(videoTag + idVideo))
            else
                if (it.isCanceled)
                    mutableLiveData.postValue(Resource.Error(it.exception?.message))
        }
        return mutableLiveData
    }

    override fun loadDesc(desc: String, idVideo: String): MutableLiveData<Resource<String>> {
        val mutableLiveData = MutableLiveData<Resource<String>>(Resource.Loading())
        fireStore.collection(elementsDescTag)
            .document(idVideo)
            .set(hashMapOf(descTag to desc)).addOnCompleteListener {
                if (it.isSuccessful)
                    mutableLiveData.postValue(Resource.Success("Успех"))
                else
                    mutableLiveData.postValue(Resource.Error(it.exception?.message))
            }
        return mutableLiveData
    }

    override fun loadVideoAndDecs(
        video: Uri,
        desc: String,
        idVideo: String
    ): MutableLiveData<Resource<String>> {
        val mutableLiveData = MutableLiveData<Resource<String>>(Resource.Loading())
        fireStore.collection(elementsDescTag)
            .document("idVideo")
            .set(hashMapOf("desc" to desc))
            .continueWith {
                if (it.isSuccessful) {
                    storage.reference.child(videoTag + idVideo).putFile(video)
                        .addOnSuccessListener {
                            mutableLiveData.postValue(Resource.Success("Данные успешно загружены"))
                        }.addOnFailureListener {
                            mutableLiveData.postValue(Resource.Error(it.message))
                        }
                }
                else
                    mutableLiveData.postValue(Resource.Error(it.exception?.message))
            }
        return mutableLiveData
    }

    companion object {
        const val elementsDescTag = "elements_desc"
        const val descTag = "desc"
        const val videoTag = "video/"
    }
}