package com.castprogramms.balamutbatut.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.SplashActivity
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.ui.registr.RegistrViewModel
import com.google.android.gms.common.api.internal.GoogleApiManager
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth
import org.koin.androidx.viewmodel.ext.android.viewModel


class SettingsFragment : Fragment() {

    val registerViewModel : RegistrViewModel by viewModel()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.settings_fragment, container, false)
        val signOut : MaterialButton = view.findViewById(R.id.exit_but)

        signOut.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            GoogleApiManager.reportSignOut()
            registerViewModel.initGoogleSign(requireContext())
            registerViewModel.signOut()
            User.clearAll()
            startActivity(Intent(context, SplashActivity::class.java))
        }

        return view
    }
}