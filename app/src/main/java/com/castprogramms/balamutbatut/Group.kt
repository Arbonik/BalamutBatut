package com.castprogramms.balamutbatut

data class Group(
    var name: String = "",
    var description: String = "",
    var numberTrainer: String = "",
    var students : List<String> = listOf()
)