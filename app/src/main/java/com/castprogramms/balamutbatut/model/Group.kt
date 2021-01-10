package com.castprogramms.balamutbatut.model

data class Group(
    val name : String?,
    val teacher : User?,
    val items : MutableList<User>?
)