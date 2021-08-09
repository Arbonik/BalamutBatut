package com.castprogramms.balamutbatut.network

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks
import com.google.firebase.dynamiclinks.ktx.dynamicLinks
import com.google.firebase.ktx.Firebase

interface ReferralApi {
    fun getUidInReferralLink(intent: Intent) : MutableLiveData<Resource<String>>

    fun getReferralLink(uid: String): Uri
}