package com.castprogramms.balamutbatut.tools

import android.widget.ExpandableListAdapter
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.observe
import com.castprogramms.balamutbatut.Repository
import org.koin.android.ext.android.inject

open class FragmentWithElement: Fragment() {
    val repository : Repository by inject()
    internal var adapter: ExpandableListAdapter? = null
    open fun generateAdapter(map: Map<String, List<Int>>): MutableLiveData<MutableMap<String, List<Element>>> {
        val maps = mutableMapOf<String, List<Element>>()
        val mutableMap = MutableLiveData(maps)
        map.forEach {
            repository.getElement(it.value.toList(), it.key).observe(viewLifecycleOwner) { it1 ->
                maps.putAll(mutableMapOf(it.key to it1))
                mutableMap.postValue(maps)
            }
        }
        return mutableMap
    }
}