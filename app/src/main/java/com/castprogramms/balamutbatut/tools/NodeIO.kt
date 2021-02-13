package com.castprogramms.balamutbatut.tools

import com.castprogramms.balamutbatut.graph.Node

interface NodeIO {
    fun getNode(position: Int): Node

    fun getNodes(): List<Node>

    fun writeNode(node: Node)

    fun writeNodes(nodes: List<Node>)
}