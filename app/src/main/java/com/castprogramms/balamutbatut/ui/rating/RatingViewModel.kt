package com.castprogramms.balamutbatut.ui.rating

import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.Repository

class RatingViewModel(private val repository: Repository): ViewModel() {
    fun getAllStudent() = repository.getAllStudents()
}