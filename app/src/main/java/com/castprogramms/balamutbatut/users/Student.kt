package com.castprogramms.balamutbatut.users

import com.castprogramms.balamutbatut.graph.Node

data class Student(
    var first_name: String,
    var second_name : String,
    var date : String,
    var nodes : List<Node>
){
    var nameGroup : String? = null
}