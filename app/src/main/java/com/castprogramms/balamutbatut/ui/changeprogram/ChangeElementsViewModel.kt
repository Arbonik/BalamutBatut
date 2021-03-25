package com.castprogramms.balamutbatut.ui.changeprogram

import androidx.lifecycle.ViewModel
import com.castprogramms.balamutbatut.Repository
import com.castprogramms.balamutbatut.tools.Element

class ChangeElementsViewModel: ViewModel() {
    fun getNamesElements(elements: List<Element>, repository: Repository) =
        repository.getElement(elements)
}