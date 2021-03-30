package com.castprogramms.balamutbatut.users

import com.castprogramms.balamutbatut.graph.Node
import com.castprogramms.balamutbatut.tools.Element

class Student(
    first_name: String,
    second_name: String,
    date: String,
    sex: String,
    img: String,
    var elements: List<Element>,
    var nodes: List<Node>
):Person(first_name, second_name, date, sex, groupID = notGroup, type = "student", img){
    fun setNodesList(nodes: List<Node>){
        this.nodes = nodes
    }
    constructor() :this("","","","","", listOf(), listOf())

    fun getFullName() = "$first_name $second_name"
}