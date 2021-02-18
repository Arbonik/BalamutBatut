package com.castprogramms.balamutbatut.graph

import android.app.AlertDialog
import android.content.Context
import com.castprogramms.balamutbatut.R
import com.example.graphguilibrary.NodeView

class NodeInfoAlertDialog {
    fun createDialog(nodeView: NodeView) : AlertDialog.Builder {
        return AlertDialog.Builder(nodeView.context)
            .setTitle(nodeView.context.resources.getString(R.string.title_dialog_info))
            .setMessage(nodeView.node.data.toString())
    }
}   