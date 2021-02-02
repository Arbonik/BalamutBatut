package com.example.graphguilibrary

interface NodeIO {
    fun getNode(position: Int): Node

    fun getNodes(): List<Node>

    fun writeNode(node: Node)

    fun writeNodes(nodes: List<Node>)
}