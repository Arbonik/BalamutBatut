package com.castprogramms.balamutbatut.tools

import android.util.Log
import android.widget.ExpandableListAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.observe
import com.castprogramms.balamutbatut.Repository
import org.koin.android.ext.android.inject

open class FragmentWithElement(layoutID: Int): Fragment(layoutID) {
    val repository : Repository by inject()
    var mutableLiveData = MutableLiveData<MutableMap<String, List<Element>>>()
    open fun generateAdapter(map: Map<String, List<Int>>){
        val maps = mutableMapOf<String, List<Element>>()
        map.forEach {
            repository.getElement(it.value.toList(), it.key).observe(viewLifecycleOwner, Observer{ it1 ->
                maps.put(it.key, it1)
                Log.e("data", maps.toString())
                mutableLiveData.postValue(maps)
            })
        }
    }
}