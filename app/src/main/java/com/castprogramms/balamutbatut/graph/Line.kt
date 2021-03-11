package com.castprogramms.balamutbatut.graph

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.view.View
import com.castprogramms.balamutbatut.graph.NodeView

// класс для отрисовки линии между узлами
class Line(var firstNode: NodeView, var secondNode: NodeView, context: Context):View(context) {
    public override fun onDraw(canvas: Canvas){
        canvas.drawLine(firstNode.center.x, firstNode.center.y,
            secondNode.center.x, secondNode.center.y, Paint(Color.CYAN))
    }
}