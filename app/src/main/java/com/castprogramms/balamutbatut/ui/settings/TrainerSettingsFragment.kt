package com.castprogramms.balamutbatut.ui.settings

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.SplashActivity
import com.castprogramms.balamutbatut.databinding.FragmentTrainerSettingsBinding
import com.castprogramms.balamutbatut.tools.ActionsWithCoins
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.ui.registr.RegistrViewModel
import com.google.android.gms.common.api.internal.GoogleApiManager
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel

class TrainerSettingsFragment: Fragment(R.layout.fragment_trainer_settings) {
    val registerViewModel : RegistrViewModel by viewModel()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentTrainerSettingsBinding.bind(view)
        requireActivity().setTitle(R.string.item_settings)
        binding.scan.setOnClickListener {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    checkSelfPermission(requireContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                } // проверка на наличие разрешений
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    requestPermissions(arrayOf(Manifest.permission.CAMERA), 101)
                }
            findNavController()
                .navigate(R.id.action_settingsFragment2_to_qrCodeScannerFragment2,
                    Bundle().apply{
                        putString("action", ActionsWithCoins.PAY.desc)
                        putString("type", "trainer_scan")
                    })
        }
        binding.exitBut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            GoogleApiManager.reportSignOut()
            registerViewModel.initGoogleSign(requireContext())
            registerViewModel.signOut()
            User.clearAll()
            startActivity(Intent(context, SplashActivity::class.java))
            requireActivity().finish()
        }
    }
}