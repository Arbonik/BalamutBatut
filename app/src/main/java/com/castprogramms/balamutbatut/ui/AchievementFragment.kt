package com.castprogramms.balamutbatut.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.Repository
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.ui.changeprogram.adapters.ElementsAdapter
import kotlinx.coroutines.newSingleThreadContext
import org.koin.android.ext.android.inject

class AchievementFragment : Fragment() {
    private val repository : Repository by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_achievement, container, false)
        val recyclerView : RecyclerView = view.findViewById(R.id.recycler_achi_student)
        val adapter = ElementsAdapter(requireContext(), true)
        recyclerView.adapter = adapter
        val controller = AnimationUtils
            .loadLayoutAnimation(context, R.anim.recycler_anim_right_to_left)
        User.mutableLiveDataStudent.observe(viewLifecycleOwner, Observer {
            if (it != null){
                repository.getElement(it.elements).observe(viewLifecycleOwner, Observer {
                    recyclerView.layoutAnimation = controller
                    adapter.setElement(it)
                    recyclerView.scheduleLayoutAnimation()
                })
            }
        })

        return view
    }

}