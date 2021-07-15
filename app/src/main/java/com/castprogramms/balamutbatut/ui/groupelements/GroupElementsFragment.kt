package com.castprogramms.balamutbatut.ui.groupelements

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.FragmentGroupElementsBinding

class GroupElementsFragment: Fragment(R.layout.fragment_group_elements) {
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val binding = FragmentGroupElementsBinding.bind(view)
        val adapter = GroupElementsAdapter()
        adapter.elementsTitle = mutableListOf("GIRLS" to 0)
        binding.recyclerElements.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerElements.adapter = adapter
    }
}