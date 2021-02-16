package com.castprogramms.balamutbatut.graph

import android.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.widget.EditText
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.tools.NodeData
import com.example.graphguilibrary.NodeView
import kotlin.math.min

// класс для вызова AlertDialog
class CustomAlertDialog {
    fun createAlertDialog(nodeView: NodeView): AlertDialog.Builder? {
        val view = LayoutInflater.from(nodeView.context).inflate(R.layout.main_fragment, null)
        return AlertDialog.Builder(nodeView.context)
                .setTitle("Добавление узла")
                .setView(view)
                .setPositiveButton("Добавить") { dialog, which ->
                    Log.d("Test",view.findViewById<EditText>(R.id.email).text.toString())
                    Log.d("Test",view.findViewById<EditText>(R.id.desc).text.toString())
                    val model = nodeView.parent as TreeGraphView
                    model.addNodeView(nodeView, NodeView(
                        min(nodeView.context.resources.displayMetrics.widthPixels,
                                nodeView.context.resources.displayMetrics.heightPixels) / 20.toFloat(),
                                nodeView.context,
                                Node(mutableListOf(),
                                NodeData(mutableListOf(view.findViewById<EditText>(R.id.email).text.toString(),
                                    view.findViewById<EditText>(R.id.desc).text.toString())))
                    )
                    )
                }
    }
}