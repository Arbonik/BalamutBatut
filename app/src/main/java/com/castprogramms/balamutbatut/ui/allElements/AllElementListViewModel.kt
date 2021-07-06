package com.castprogramms.balamutbatut.ui.allElements

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.network.Repository

class AllElementListViewModel(private val repository: Repository) : ViewModel() {
    var mutableLiveElements = MutableLiveData<MutableList<Int>>()
    fun getAllElement(id: MutableMap<String, List<Int>>) = repository.getAllElements(id)
}