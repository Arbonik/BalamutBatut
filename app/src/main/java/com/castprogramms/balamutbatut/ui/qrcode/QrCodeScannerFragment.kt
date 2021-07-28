package com.castprogramms.balamutbatut.ui.qrcode

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.util.SparseArray
import android.view.LayoutInflater
import android.view.SurfaceHolder
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.FragmentQrCodeScannerBinding
import com.castprogramms.balamutbatut.databinding.SetQuantityBinding
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.ActionsWithCoins
import com.castprogramms.balamutbatut.tools.User
import com.google.android.gms.vision.CameraSource
import com.google.android.gms.vision.Detector
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.io.IOException

class QrCodeScannerFragment : Fragment(R.layout.fragment_qr_code_scanner) {
    val viewModel: QrCodeViewModel by viewModel()
    var quantity = 0

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 0){
            if (grantResults.getOrNull(0) != PackageManager.PERMISSION_GRANTED)
                findNavController().popBackStack()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentQrCodeScannerBinding.bind(view)
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.set_quantity, null)
        if (requireArguments().getString("type") == "trainer_scan") {
            val dialogBinding = SetQuantityBinding.bind(dialogView)
            val alertDialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setPositiveButton("Подтверить") { dialog, which ->
                    try {
                        quantity = dialogBinding.quantityBatutCoin.text.toString().toInt()
                        binding.camerapreview.visibility = View.VISIBLE
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), "Введите число", Toast.LENGTH_SHORT).show()
                        findNavController().popBackStack()
                    }
                }
                .setNegativeButton("Отмена") { dialog, _ ->
                    dialog.cancel()
                }
                .setOnCancelListener {
                    findNavController().popBackStack()
                }
            alertDialog.create().show()
        }

        binding.camerapreview.visibility = View.INVISIBLE
        val barcodeDetector = BarcodeDetector.Builder(requireContext())
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()

        val cameraSource = CameraSource.Builder(requireContext(), barcodeDetector)
            .setRequestedPreviewSize(640, 480)
            .build()
        binding.camerapreview.holder.addCallback(object : SurfaceHolder.Callback {
            @SuppressLint("MissingPermission")
            override fun surfaceCreated(holder: SurfaceHolder) {
                try {
                    val handler = Handler()
                    handler.postDelayed(
                        Runnable { cameraSource.start(holder) },
                        300
                    )
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }

            override fun surfaceChanged(p0: SurfaceHolder, p1: Int, p2: Int, p3: Int) {}

            override fun surfaceDestroyed(holder: SurfaceHolder) {
                cameraSource.stop()
            }
        })
        barcodeDetector.setProcessor(object : Detector.Processor<Barcode> {
            override fun release() {
            }

            override fun receiveDetections(detections: Detector.Detections<Barcode>) {
                val qrCodes: SparseArray<Barcode> = detections.detectedItems
                if (qrCodes.size() != 0) {
                    binding.textView.post {
                        val id = qrCodes.valueAt(0).displayValue.toString()
                        when (requireArguments().getString("action")) {
                            ActionsWithCoins.PAY.desc -> {
                                viewModel.writeOffCoin(id, quantity).observe(viewLifecycleOwner, {
                                    when (it) {
                                        is Resource.Error -> {
                                            binding.camerapreview.visibility = View.INVISIBLE
                                            binding.progressScan.visibility = View.GONE
                                            Toast.makeText(
                                                requireContext(),
                                                it.message.toString(),
                                                Toast.LENGTH_LONG
                                            ).show()
                                            findNavController().popBackStack()
                                        }
                                        is Resource.Loading -> {
                                            binding.camerapreview.visibility = View.INVISIBLE
                                            binding.progressScan.visibility = View.VISIBLE
                                        }
                                        is Resource.Success -> {
                                            binding.camerapreview.visibility = View.INVISIBLE
                                            binding.progressScan.visibility = View.GONE
                                            Toast.makeText(
                                                requireContext(),
                                                "У ученика успешно списаны средства",
                                                Toast.LENGTH_LONG
                                            ).show()
                                            findNavController().popBackStack()
                                        }
                                    }
                                })
                            }
                            ActionsWithCoins.GET.desc -> {
                                User.isScan = true
                                viewModel.addBatutCoin(id, 50)
                                findNavController().popBackStack()
                            }
                        }

                    }
                }
            }
        })
    }
}