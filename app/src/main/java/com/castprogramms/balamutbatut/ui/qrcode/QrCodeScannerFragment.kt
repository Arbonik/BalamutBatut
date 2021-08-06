package com.castprogramms.balamutbatut.ui.qrcode

import android.app.AlertDialog
import android.content.pm.PackageManager
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.Toast
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.budiyev.android.codescanner.AutoFocusMode
import com.budiyev.android.codescanner.CodeScanner
import com.budiyev.android.codescanner.DecodeCallback
import com.budiyev.android.codescanner.ScanMode
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.FragmentQrCodeScannerBinding
import com.castprogramms.balamutbatut.databinding.SetQuantityBinding
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.ActionsWithCoins
import com.castprogramms.balamutbatut.tools.User
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class QrCodeScannerFragment : Fragment(R.layout.fragment_qr_code_scanner) {
    val viewModel: QrCodeViewModel by viewModel()
    var quantity = 0
    lateinit var codeScanner: CodeScanner

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 0) {
            if (grantResults.getOrNull(0) != PackageManager.PERMISSION_GRANTED) {
                findNavController().popBackStack()
                val snackbar = Snackbar.make(
                    requireView(),
                    "Дайте разрешение на использование камеры в настройках приложения",
                    Snackbar.LENGTH_SHORT
                )
                snackbar.setAction("Закрыть") {
                    snackbar.dismiss()
                }
                snackbar.show()
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentQrCodeScannerBinding.bind(view)
        requireActivity().title = "Сканирование QR-кода"
        codeScanner = CodeScanner(requireContext(), binding.codeScanner)
        val dialogView = LayoutInflater.from(requireContext())
            .inflate(R.layout.set_quantity, null)
        if (requireArguments().getString("type") == "trainer_scan") {
            binding.codeScanner.visibility = View.GONE
            val dialogBinding = SetQuantityBinding.bind(dialogView)
            val alertDialog = AlertDialog.Builder(requireContext())
                .setView(dialogView)
                .setOnCancelListener {
                    findNavController().popBackStack()
                }.create()

            dialogBinding.button.setOnClickListener {
                try {
                    quantity = dialogBinding.quantityBatutCoin.text.toString().toInt()
                    binding.codeScanner.visibility = View.VISIBLE
                    alertDialog.dismiss()
                } catch (e: Exception) {
                    Toast.makeText(requireContext(), "Введите число", Toast.LENGTH_SHORT).show()
                    findNavController().popBackStack()
                }
            }

            if (alertDialog.window != null)
                alertDialog.window!!.setBackgroundDrawable(ColorDrawable(0))
            alertDialog.show()
            codeScanner(binding)
        }
        else
            codeScanner(binding)
    }

    private fun codeScanner(binding: FragmentQrCodeScannerBinding) {

        codeScanner.apply {
            camera = CodeScanner.CAMERA_BACK
            formats = CodeScanner.ALL_FORMATS

            autoFocusMode = AutoFocusMode.SAFE
            scanMode = ScanMode.CONTINUOUS
            isAutoFocusEnabled = true
            isFlashEnabled = false

            decodeCallback = DecodeCallback {
                if (it.text.isNotEmpty()) {
                    binding.textView.post {
                        val id = it.text
                        codeScanner.stopPreview()
                        when (requireArguments().getString("action")) {
                            ActionsWithCoins.PAY.desc -> {
                                viewModel.writeOffCoin(id, quantity).observe(viewLifecycleOwner, {
                                    when (it) {
                                        is Resource.Error -> {
                                            binding.codeScanner.visibility = View.GONE
                                            binding.progressScan.visibility = View.GONE
                                            Toast.makeText(
                                                requireContext(),
                                                it.message.toString(),
                                                Toast.LENGTH_LONG
                                            ).show()
                                            findNavController().popBackStack()
                                        }
                                        is Resource.Loading -> {
                                            binding.codeScanner.visibility = View.INVISIBLE
                                            binding.progressScan.visibility = View.VISIBLE
                                        }
                                        is Resource.Success -> {
                                            binding.codeScanner.visibility = View.GONE
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
                                Toast.makeText(
                                    requireContext(),
                                    "Успех",
                                    Toast.LENGTH_LONG
                                ).show()
                                findNavController().popBackStack()
                            }
                        }

                    }
                }
            }
            codeScanner.startPreview()
        }
    }

    override fun onPause() {
        super.onPause()
        codeScanner.releaseResources()
    }
}