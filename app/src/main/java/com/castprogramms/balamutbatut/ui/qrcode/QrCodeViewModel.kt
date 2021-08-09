package com.castprogramms.balamutbatut.ui.qrcode

import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.network.Repository

class QrCodeViewModel(private val repository: Repository): ViewModel() {
    fun addBatutCoin(id:String, quantity: Int) = repository.addBatutCoin(id, quantity)
    fun writeOffCoin(id: String, quantity: Int) = repository.writeOffCoin(id, quantity)
    var date = ""
    var isScan = false

    fun getReferralLink(uid: String) = repository.getReferralLink(uid)
}