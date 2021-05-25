package com.castprogramms.balamutbatut.ui.rating

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ExpandableListAdapter
import android.widget.ExpandableListView
import androidx.fragment.app.Fragment
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.FragmentRatingBinding
import com.castprogramms.balamutbatut.tools.Element
import org.koin.androidx.viewmodel.ext.android.viewModel

class RatingFragment: Fragment() {
    val viewModel : RatingViewModel by viewModel()

    internal var adapter: ExpandableListAdapter?= null
    internal var titleList: List<String> ?= null

    val easy = "EasyElement"
    val midlle = "MiddleElement"
    val hard = "HardElement"
    val dataMap: HashMap<String, List<Element>>
    get() {
        val listData = HashMap<String, List<String>>()

        val item = ArrayList<String>()
        item.add("Легкие: $easy")
        item.add("Средние: $midlle")
        item.add("Сложные: $hard")

        listData["Выполненные элементы"] = item

        return dataMap
    }
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_rating, container, false)
        val binding = FragmentRatingBinding.bind(view)
        val ratingAdapter = RatingAdapter(viewModel.getAllStudent())
        binding.recyclerRating.adapter = ratingAdapter

        val expandableListView = view.findViewById<ExpandableListView>(R.id.expandableListView)
        if (expandableListView != null) {
            val listData = dataMap
            titleList = ArrayList(listData.keys)
            adapter = ExpandableList(requireContext(), titleList as ArrayList<String>, listData)
            expandableListView.setAdapter(adapter)
            expandableListView.setOnGroupExpandListener { groupPosition ->  }

            expandableListView.setOnGroupCollapseListener { groupPosition ->  }

            expandableListView.setOnChildClickListener { parent, v, groupPosition, childPosition, id ->
                false
            }
        }
        return view
    }
}