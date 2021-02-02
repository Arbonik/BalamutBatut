package com.example.graphguilibrary

open class Node(var childNodeID: MutableList<Int>){
    var data :Any? = null
    constructor(
            childNodeID: MutableList<Int>,
            data : Any
    ): this(childNodeID){
        this.data = data
    }

    override fun toString(): String {
        if (data == null)
            return "Пустой узел"
        else
            return data.toString()
    }
}