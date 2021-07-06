package com.castprogramms.balamutbatut.ui.changeprogram

import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.network.Repository

class ChangeElementsViewModel(private val repository: Repository): ViewModel() {
    fun getTrueOrder() = repository.getTrueOrder()
    fun getStudent(id: String) = repository.getStudent(id)
}