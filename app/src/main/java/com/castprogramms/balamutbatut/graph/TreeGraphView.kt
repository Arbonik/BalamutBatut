package com.castprogramms.balamutbatut.graph

import android.content.Context
import android.graphics.PointF
import android.util.AttributeSet
import android.view.ViewGroup
import com.castprogramms.balamutbatut.tools.NodeData
import com.castprogramms.balamutbatut.tools.User
import com.example.graphguilibrary.Line
import com.example.graphguilibrary.NodeView

// класс для отрисовки графа
class TreeGraphView(context: Context, attributeSet: AttributeSet):ViewGroup(context,attributeSet){

    var quantity = 1
    var nodes1 = MutableList(quantity) { Node(if (it != quantity - 1) mutableListOf(it + 1) else mutableListOf(), NodeData(mutableListOf("qwerty")))}

    var nodes = mutableListOf<NodeView>()
    val lines = mutableListOf<Line>()
    val radius = Math.min(context.resources.displayMetrics.widthPixels, context.resources.displayMetrics.heightPixels) / 20.toFloat()

    init {
        update()
    }
    fun setNodesWithInfo(nodes:MutableList<Node>){
        nodes1 = nodes
        update()
    }
    fun update(){
        this.removeAllViews()
        nodes.clear()
        lines.clear()
        for (i in 0 until nodes1.size){
            nodes.add(
                    NodeView(
                            radius,
                            this.context,
                            nodes1[i]
                    )
            )
        }
        setNodesAndLines(nodes)
    }
    fun addNodeView(parentNodeView: NodeView, nodeView: NodeView){ // функция для добавления узла
        nodes.add(nodeView)
        nodes1.add(nodeView.node)
        User.student?.setNodesList(nodes1)
        parentNodeView.node.childNodeID.add(nodes.size-1)
        setNodesAndLines(nodes)
    }

    fun setNodesAndLines(mutableList: MutableList<NodeView>){ // функция для переотрисовки
        nodes = mutableList
        lines.clear()
        this.removeAllViews()
        for (i in 0 until nodes.size-1){
            nodes[i].node.childNodeID.forEach {
                lines.add(
                        Line(
                                nodes[i],
                                nodes[it],
                                this.context
                        )
                )
            }
        }
        nodes.forEachIndexed { index, node ->
            if (index == 0)
                node.center = PointF((context.resources.displayMetrics.widthPixels / (2 * (index + 1))).toFloat(), node.radius + (height / nodes.size) * index)
            node.node.childNodeID.forEachIndexed { i, it ->
                nodes[it].center = PointF(((node.center.x*2 / (node.node.childNodeID.size+1)) * (i+1)), ((height / nodes.size) * (index + 1)).toFloat())
            }
        }
        lines.forEach {
            addView(it)
        }


        nodes.forEach {
            addView(it,LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT))
        }
    //        Log.e("Test",DataUserFirebase().getStudent("UwsuyZ4DB4J8b1AbRbE9").toString())

    }

    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) { // размещение на фрагменте детей

        val count = childCount

        for (index in 0 until count) {
            val view = getChildAt(index)
            var indexNode: Int
            if (view is NodeView) {
                indexNode = index - lines.size
                if (indexNode == 0) {
                    view.center = PointF(
                        (width / (2 * (indexNode + 1))).toFloat(),
                        view.radius + (height / calculateLevels()) * indexNode
                    )
                }
                view.node.childNodeID.forEachIndexed { i, it ->
                    nodes[it].center.set(PointF(
                        ((view.center.x * 2 / (view.node.childNodeID.size + 1)) * (i + 1)),
                        ((height / calculateLevels()) * (
                                if (indexNode == 0) 1 else
                                    getLevelNodeView(view))).toFloat()
                    ))
                }
                view.layout(
                    (view.center.x - view.radius).toInt(),
                    (view.center.y - view.radius).toInt(),
                    (view.center.x + view.radius).toInt(),
                    (view.center.y + view.radius).toInt()
                )
                view.invalidate()

            } else {
                view.layout(l, t, r, b)
                view.invalidateOutline()
            }
        }
    }

    private fun calculateLevels(count: Int = 2):Int{
        var counter = count
        nodes.forEachIndexed { index, nodeView ->
            if (getNodesWithChild(nodeView.node.childNodeID).isNotEmpty())
                counter++
        }
        return counter
    }

    private fun getNodesWithChild(mutableList: MutableList<Int>):MutableList<NodeView>{
        val mutableListNodeViews = mutableListOf<NodeView>()
        mutableList.forEach {
            val nodeView = nodes[it]
            if (nodeView.node.childNodeID.isNotEmpty())
                mutableListNodeViews.add(nodeView)
        }
        return mutableListNodeViews
    }

    private fun getLevelNodeView(nodeView: NodeView): Int {
        var level = 1
        var view = searchParent(nodeView)
        while (view != nodes.first()){
            view = searchParent(view)
            level++
        }
        level++
        return level
    }

    private fun getPosition(nodeView: NodeView): Int {
        var position = 0
        for (i in nodes.indices){
            if (nodeView == nodes[i]){
                position = i
                break
            }
        }
        return position
    }

    private fun searchParent(nodeView: NodeView): NodeView {
        return nodes.first { it.node.childNodeID.contains(getPosition(nodeView))}
    }
}