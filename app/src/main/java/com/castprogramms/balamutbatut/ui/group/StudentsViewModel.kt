package com.castprogramms.balamutbatut.ui.group

import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.network.Repository

class StudentsViewModel(private val repository: Repository):ViewModel() {
    fun getQuery(idGroup : String) = repository.getCollectionAllStudent(idGroup)
}