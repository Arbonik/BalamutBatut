package com.castprogramms.balamutbatut.ui.changeprogram

import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.Repository
import com.castprogramms.balamutbatut.tools.Element

class ChangeElementsViewModel: ViewModel() {
    fun getNamesElements(repository: Repository, idElements: Map<String, List<Int>>) =
        repository.getAllElements(idElements)
}