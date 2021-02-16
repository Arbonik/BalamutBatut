package com.castprogramms.balamutbatut.graph

import com.castprogramms.balamutbatut.tools.NodeData

open class Node(var childNodeID: MutableList<Int>){
    var data = NodeData(mutableListOf())
    constructor(
            childNodeID: MutableList<Int>,
            data : NodeData
    ): this(childNodeID){
        this.data = data
    }

    override fun toString(): String {
        return data.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (other is Node){
            return this.data == other.data
        }
        else
            return false
    }
}