package com.castprogramms.balamutbatut.ui.qrcode

import android.Manifest
import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.FragmentQrCodeScannerBinding
import com.castprogramms.balamutbatut.databinding.SetQuantityBinding
import com.castprogramms.balamutbatut.graph.CustomAlertDialog
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.ActionsWithCoins
import com.castprogramms.balamutbatut.tools.User
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException
import java.lang.Thread.sleep

class QrCodeScannerFragment: Fragment(R.layout.fragment_qr_code_scanner) {
    val viewModel : QrCodeViewModel by viewModel()
    var quantity = 0
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.set_quantity, null)
        val dialogBinding = SetQuantityBinding.bind(dialogView)
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(dialogView)
            .setPositiveButton("Подтверить") { dialog, which ->
                quantity = dialogBinding.quantityBatutCoin.text.toString().toInt()
            }
            .setNegativeButton("Отмена"){ dialog, _ ->
                dialog.cancel()
            }
            .setOnCancelListener {
                findNavController().popBackStack()
            }
        alertDialog.create().show()
        val binding = FragmentQrCodeScannerBinding.bind(view)
        val barcodeDetector = BarcodeDetector.Builder(requireContext())
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()

        val cameraSource = CameraSource.Builder(requireContext(), barcodeDetector)
            .setRequestedPreviewSize(640,480)
            .build()
        binding.camerapreview.holder.addCallback(object: SurfaceHolder.Callback{
            override fun surfaceCreated(holder:SurfaceHolder) {
                if (ActivityCompat.checkSelfPermission(
                        requireActivity().applicationContext,
                        Manifest.permission.CAMERA
                    ) != PackageManager.PERMISSION_GRANTED
                ) {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), 101)
                    return
                }
                try{
                    cameraSource.start(holder)
                } catch (e : IOException){
                    e.printStackTrace()
                }
            }
            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {}

            override fun surfaceDestroyed(holder:SurfaceHolder) {
                cameraSource.stop()
            }
        })
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode>{
            override fun release() {
            }
            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val qrCodes: SparseArray<Barcode> = detections.detectedItems
                if(qrCodes.size() != 0){
                    binding.textView.post {
                        val id = qrCodes.valueAt(0).displayValue.toString()
                        when(requireArguments().getString("action")){
                           ActionsWithCoins.PAY.desc -> {
                               viewModel.writeOffCoin(id, quantity).observe(viewLifecycleOwner, {
                                   when(it){
                                       is Resource.Error -> {
                                           binding.camerapreview.visibility = View.INVISIBLE
                                           binding.progressScan.visibility = View.GONE
                                           Toast.makeText(requireContext(), it.message.toString(), Toast.LENGTH_LONG).show()
                                           findNavController().popBackStack()
                                       }
                                       is Resource.Loading -> {
                                           binding.camerapreview.visibility = View.INVISIBLE
                                           binding.progressScan.visibility = View.VISIBLE
                                       }
                                       is Resource.Success -> {
                                           binding.camerapreview.visibility = View.INVISIBLE
                                           binding.progressScan.visibility = View.GONE
                                           Toast.makeText(requireContext(), "У ученика успешно списаны средства", Toast.LENGTH_LONG).show()
                                           findNavController().popBackStack()
                                       }
                                   }
                               })
                           }
                            ActionsWithCoins.GET.desc -> {
                                User.isScan = true
                                viewModel.addBatutCoin(id, 50)
                                requireActivity().onBackPressed()
                            }
                        }

                    }
                }
            }
        })
    }
}