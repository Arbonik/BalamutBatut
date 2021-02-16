package com.example.graphguilibrary

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import com.castprogramms.balamutbatut.graph.CustomAlertDialog
import com.castprogramms.balamutbatut.graph.Node
import com.castprogramms.balamutbatut.graph.NodeInfoAlertDialog

// класс для отображения узла
class NodeView (val radius: Float,context: Context, var node: Node)
    : androidx.appcompat.widget.AppCompatImageButton(context){
    var center = PointF()
    val gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {

        override fun onLongPress(e: MotionEvent) { // длинный ТЫК
            Log.e("Test", "Longpress detected")
            CustomAlertDialog().createAlertDialog(this@NodeView)!!.create().show()
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean { // одиночный ТЫК
            Log.d("Test", this@NodeView.node.toString())
            NodeInfoAlertDialog().createDialog(this@NodeView)?.create()?.show()
            return true
        }
    })

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return true
    }
}