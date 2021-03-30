package com.castprogramms.balamutbatut.ui.settings

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.NonNull
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.ViewModelProvider
import com.castprogramms.balamutbatut.MainActivity
import com.castprogramms.balamutbatut.MainActivityStudent
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.SplashActivity
import com.castprogramms.balamutbatut.tools.TypesUser
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.ui.registr.RegistrViewModel
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.*
import com.google.android.gms.common.api.internal.GoogleApiManager
import com.google.android.material.button.MaterialButton
import com.google.firebase.auth.FirebaseAuth


class SettingsFragment : Fragment() {

    val registerViewModel : RegistrViewModel by viewModels()

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