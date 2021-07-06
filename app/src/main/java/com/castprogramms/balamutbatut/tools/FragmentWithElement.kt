package com.castprogramms.balamutbatut.tools

import android.util.Log
import androidx.fragment.app.Fragment
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import com.castprogramms.balamutbatut.network.Repository
import com.castprogramms.balamutbatut.network.Resource
import org.koin.android.ext.android.inject

open class FragmentWithElement(layoutID: Int): Fragment(layoutID) {
    val repository : Repository by inject()
    var mutableLiveData = MutableLiveData<MutableMap<String, List<Element>>>()
    val mutableLiveDataOrder = MutableLiveData<Resource<List<String>>>()
    open fun generateAdapter(map: Map<String, List<Int>>){
        val maps = mutableMapOf<String, List<Element>>()
        if (map.isNotEmpty()) {
            map.forEach {
                repository.getElement(it.value.toList(), it.key)
                    .observe(viewLifecycleOwner, { it1 ->
                        maps.put(it.key, it1)
                        Log.e("data", maps.toString())
                        mutableLiveData.postValue(maps)
                    })
            }
        }else{
            repository.getAllElements(mapOf()).observe(viewLifecycleOwner, Observer{
                mutableLiveData.postValue(it.toMutableMap())
            })
        }
    }

    open fun getTrueOrder(){
        repository.getTrueOrder().observe(viewLifecycleOwner, Observer{
            when(it){
                is Resource.Error -> {}
                is Resource.Loading -> {}
                is Resource.Success -> {
                    mutableLiveDataOrder.postValue(it)
                }
            }
        })
    }
}