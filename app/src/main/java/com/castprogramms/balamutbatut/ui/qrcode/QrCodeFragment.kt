package com.castprogramms.balamutbatut.ui.qrcode

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidmads.library.qrgenearator.QRGContents
import androidmads.library.qrgenearator.QRGEncoder
import androidx.fragment.app.Fragment
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.FragmentQrCodeBinding
import com.castprogramms.balamutbatut.tools.User
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class QrCodeFragment: Fragment(R.layout.fragment_qr_code) {
    val viewModel: QrCodeViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        requireActivity().title = "QR-код для сканирования"
        val binding = FragmentQrCodeBinding.bind(view)
        val qrgEncoder = if (requireArguments().getString("type") == "scan") {
            binding.invite.visibility = View.GONE
            QRGEncoder(
                User.id,
                null,
                QRGContents.Type.TEXT,
                1000
            )
        }
        else {
            Log.e("data", viewModel.getReferralLink(User.id).toString())
            QRGEncoder(
                viewModel.getReferralLink(User.id).toString(),
                null,
                QRGContents.Type.TEXT,
                1000
            )
        }
        try {
            val bitmap = qrgEncoder.encodeAsBitmap()
            binding.qr.setImageBitmap(bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }

        binding.invite.setOnClickListener {
            val intent = Intent(Intent.ACTION_SEND)
            intent.type = "text/plain"
            intent.putExtra(Intent.EXTRA_TEXT, viewModel.getReferralLink(User.id).toString())
            startActivity(Intent.createChooser(intent, "Share Link"))
        }
    }
}