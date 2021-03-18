package com.castprogramms.balamutbatut.graph

import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.tools.NodeData

var dataNode = NodeData(mutableListOf(), "Сед - Живот - Сед", "Пргаешь, падаешь, " +
        "отскакиваешь, снова падаешь, но уже на животик, снова отскакиваешь, падаешь, отскакиваешь, встаешь на ноги, готово.", R.drawable.lock_24)

open class Node(var childNodeID: MutableList<Int>)
{
    constructor():this(mutableListOf())
    var dataNode = NodeData(mutableListOf(), "Сед - Живот - Сед", "Пргаешь, падаешь, " +
            "отскакиваешь, снова падаешь, но уже на животик, снова отскакиваешь, падаешь, отскакиваешь, встаешь на ноги, готово.", R.drawable.lock_24)
    constructor(
            childNodeID: MutableList<Int>,
            data : NodeData
    ): this(childNodeID){
        this.dataNode = data
    }

    override fun toString(): String {
        return dataNode.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (other is Node){

            return this.dataNode == other.dataNode
        }
        else
            return false
    }

}