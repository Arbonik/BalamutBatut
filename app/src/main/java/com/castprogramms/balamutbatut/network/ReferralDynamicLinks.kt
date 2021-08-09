package com.castprogramms.balamutbatut.network

import android.content.Intent
import android.net.Uri
import androidx.lifecycle.MutableLiveData
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks

class ReferralDynamicLinks : ReferralApi {
    private val firebaseLinks = FirebaseDynamicLinks.getInstance()

    override fun getUidInReferralLink(intent: Intent): MutableLiveData<Resource<String>> {
        val mutableLiveData = MutableLiveData<Resource<String>>(Resource.Loading())
        firebaseLinks.getDynamicLink(intent)
            .addOnSuccessListener { pendingDynamicLinkData ->
                var deepLink: Uri? = null
                if (pendingDynamicLinkData != null) {
                    deepLink = pendingDynamicLinkData.link
                }

                if (deepLink != null) {
                    if (deepLink.getBooleanQueryParameter("invitedby", false)) {
                        val referrerUid = deepLink.getQueryParameter("invitedby")
                        mutableLiveData.postValue(Resource.Success(referrerUid.toString()))
                    } else {
                        mutableLiveData.postValue(Resource.Error("Ссылка не содержит uid"))
                    }
                } else {
                    mutableLiveData.postValue(Resource.Error("Воу, ссылки нет"))
                }
            }
            .addOnFailureListener()
            {
                mutableLiveData.postValue(Resource.Error(it.message))
            }
        return mutableLiveData
    }

    override fun getReferralLink(uid: String): Uri {

        return firebaseLinks.createDynamicLink()
            .setLink(Uri.parse(link + uid))
            .setDomainUriPrefix(domainUri)
            .buildDynamicLink().uri
    }

    companion object {
        const val link = "https://balamutbatut.page.link/app?invitedby="
        const val domainUri = "https://balamutbatut.page.link"
    }
}