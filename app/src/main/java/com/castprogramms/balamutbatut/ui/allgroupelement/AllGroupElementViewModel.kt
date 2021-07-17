package com.castprogramms.balamutbatut.ui.allgroupelement

import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.network.Repository

class AllGroupElementViewModel(private val repository: Repository): ViewModel() {
    fun getAllElementsOnThisTitle(title: String) = repository.getAllElementsOnThisTitle(title)
}