package com.castprogramms.balamutbatut.network

import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.castprogramms.balamutbatut.BuildConfig
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreSettings
import com.google.firebase.storage.FirebaseStorage

class VideoAndDescFirebaseStorage : VideoAndDescApi {
    private val storage = FirebaseStorage.getInstance(BuildConfig.STORAGE_BUCKET)
    val ref = storage.reference

    val settings = FirebaseFirestoreSettings.Builder()
        .setPersistenceEnabled(true)
        .build()
    val fireStore = FirebaseFirestore.getInstance(FirebaseApp.getInstance("test")).apply {
        firestoreSettings = settings
    }

    override fun downloadVideo(titleElement: String, nameElement: String): MutableLiveData<Resource<Uri>> {
        val mutableLiveData = MutableLiveData<Resource<Uri>>(Resource.Loading())
        ref.child("$videoTag$titleElement/$nameElement").downloadUrl.addOnCompleteListener {
            if (it.isSuccessful)
                mutableLiveData.postValue(Resource.Success(it.result))
            else
                mutableLiveData.postValue(Resource.Error(it.exception?.message))
        }
        return mutableLiveData
    }

    override fun downloadDescAndLevel(title: String, name: String): MutableLiveData<Resource<Pair<String, String>>> {
        val mutableLiveData = MutableLiveData<Resource<Pair<String, String>>>(Resource.Loading())
        fireStore.collection(elementsDescTag)
            .document(title)
            .addSnapshotListener { value, error ->
                if (value != null){
                    val desc = value.getString("$name.desc").toString()
                    val level = value.getString("$name.level").toString()
                    mutableLiveData.postValue(Resource.Success(desc to level))
                }
                else{
                    mutableLiveData.postValue(Resource.Error(error?.message))
                }
            }
        return mutableLiveData
    }

    override fun loadVideo(video: Uri, idVideo: String): MutableLiveData<Resource<String>> {
        val mutableLiveData = MutableLiveData<Resource<String>>(Resource.Loading())
        ref.child(videoTag + idVideo).putFile(video).addOnCompleteListener {
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

    override fun loadVideoNameDecs(
        titleElement: String, nameElement: String, video: Uri,
        desc: String, level: String): MutableLiveData<Resource<String>> {
        val mutableLiveData = MutableLiveData<Resource<String>>(Resource.Loading())
        fireStore.runTransaction {
            fireStore.collection(elementsDescTag)
                .document(titleElement)
                .set(hashMapOf(nameElement to hashMapOf("desc" to desc, "level" to level)))

            storage.reference.child("$videoTag$titleElement/$nameElement").putFile(video)
        }.addOnSuccessListener {
            mutableLiveData.postValue(Resource.Success("Данные успешно загружены"))
        }.addOnFailureListener {
            mutableLiveData.postValue(Resource.Error(it.message))
        }
        return mutableLiveData
    }

    override fun haveVideo(title: String, name: String): MutableLiveData<Resource<Boolean>> {
        val mutableLiveData = MutableLiveData<Resource<Boolean>>(Resource.Loading())
        storage.reference.child("$videoTag$title/$name").downloadUrl.addOnCompleteListener{
            if (it.isSuccessful)
                if (it.result != null)
                    mutableLiveData.postValue(Resource.Success(true))
                else
                    mutableLiveData.postValue(Resource.Success(true))
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