package com.castprogramms.balamutbatut.ui.progress

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.graph.TreeGraphView
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.users.Student

class ProgressFragment : Fragment() {

    private lateinit var viewModel: ProgressViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.progress_fragment, container, false)
        val model = view.findViewById<TreeGraphView>(R.id.model)
        User.mutableLiveDataStudent.observe(viewLifecycleOwner, Observer{
            if (it != null) {
                model.setNodesWithInfo(it.nodes.toMutableList())
            }
        })
        return view
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(ProgressViewModel::class.java)
    }

}