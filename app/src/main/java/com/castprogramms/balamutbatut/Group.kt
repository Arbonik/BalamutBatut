package com.castprogramms.balamutbatut

import com.castprogramms.balamutbatut.users.Student

data class Group(
    var name: String,
    var description: String,
    var numberTrainer: String,
    var students : MutableList<Student>
)