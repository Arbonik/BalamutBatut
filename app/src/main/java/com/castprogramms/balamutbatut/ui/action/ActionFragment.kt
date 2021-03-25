package com.castprogramms.balamutbatut.ui.action

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.castprogramms.balamutbatut.R

class ActionFragment : Fragment() {

    val viewModel by  viewModels<ActionViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.action_fragment, container, false)
    }

}