package com.castprogramms.balamutbatut.ui.qrcode

import android.graphics.Bitmap
import android.os.Bundle
import android.view.View
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.fragment.app.Fragment
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.FragmentQrCodeBinding
import com.castprogramms.balamutbatut.tools.User


class QrCodeFragment: Fragment(R.layout.fragment_qr_code) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentQrCodeBinding.bind(view)
        val qrgEncoder = QRGEncoder(User.id, null, QRGContents.Type.TEXT, 1000)
        try {
            val bitmap: Bitmap = qrgEncoder.encodeAsBitmap()
            binding.qr.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}