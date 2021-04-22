package com.castprogramms.balamutbatut.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.Repository
import com.castprogramms.balamutbatut.databinding.FragmentAchievementBinding
import com.castprogramms.balamutbatut.databinding.FragmentInfoFragmentBinding
import com.castprogramms.balamutbatut.tools.FragmentWithElement
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.ui.changeprogram.adapters.ElementsAdapter
import com.castprogramms.balamutbatut.ui.changeprogram.adapters.IHopeThisAdapterCanWork
import com.castprogramms.balamutbatut.ui.rating.ExpandableList
import kotlinx.coroutines.newSingleThreadContext
import org.koin.android.ext.android.inject

class AchievementFragment : FragmentWithElement() {
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_achievement, container, false)
        val binding = FragmentAchievementBinding.bind(view)
        val adapterList = IHopeThisAdapterCanWork(viewLifecycleOwner, true)
        binding.recyclerAchiStudent.adapter = adapterList
        binding.recyclerAchiStudent.layoutManager = LinearLayoutManager(requireContext())
        User.mutableLiveDataStudent.observe(viewLifecycleOwner, Observer {
            if (it != null){
                generateAdapter(it.element).observe(viewLifecycleOwner){
                    adapterList.setElement(it)
                }
            }
        })

        return view
    }

}