package com.castprogramms.balamutbatut.users

import com.castprogramms.balamutbatut.graph.Node

class Student(
    first_name: String,
    second_name: String,
    date: String,
    sex: String,
    img: String,
    var nodes: List<Node>
):Person(first_name, second_name, date, sex, img){
    var nameGroup : String? = null
    val type = "student"
    fun setNodesList(nodes: List<Node>){
        this.nodes = nodes
    }
    constructor() :this("","","","","", listOf())
}