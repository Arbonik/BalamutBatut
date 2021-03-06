package com.castprogramms.balamutbatut.graph

import android.content.Context
import android.graphics.*
import android.util.Log
import android.view.GestureDetector
import android.view.GestureDetector.SimpleOnGestureListener
import android.view.MotionEvent
import androidx.navigation.findNavController
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.graph.CustomAlertDialog
import com.castprogramms.balamutbatut.graph.Node
import com.castprogramms.balamutbatut.graph.NodeInfoAlertDialog
import com.castprogramms.balamutbatut.graph.SetDataNodeAlertDialog
import com.castprogramms.balamutbatut.tools.TypesUser
import com.castprogramms.balamutbatut.tools.User

// класс для отображения узла
class NodeView(val radius: Float,context: Context, var node: Node)
    : androidx.appcompat.widget.AppCompatImageButton(context){
    var center = PointF()
    val gestureDetector = GestureDetector(context, object : SimpleOnGestureListener() {

        override fun onLongPress(e: MotionEvent) { // длинный ТЫК
            if (User.trainer != null) {
                Log.e("Test", "Longpress detected")
                CustomAlertDialog().createAlertDialog(this@NodeView)!!.create().show()
            }
        }

        override fun onSingleTapUp(e: MotionEvent): Boolean { // одиночный ТЫК
            Log.e("Test", this@NodeView.node.dataNode.toString())
            when (User.typeUser) {
                TypesUser.TRAINER -> {
                }
                TypesUser.NOTHING -> { }
            }
            return true
        }

        override fun onDoubleTap(e: MotionEvent?): Boolean { // двойной ТЫК
            SetDataNodeAlertDialog().createDialog(this@NodeView.context).create().show()
            return true
        }

    })

    override fun onTouchEvent(event: MotionEvent): Boolean {
        gestureDetector.onTouchEvent(event)
        return true
    }
}