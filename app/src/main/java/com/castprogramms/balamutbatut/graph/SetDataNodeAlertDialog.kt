package com.castprogramms.balamutbatut.graph

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import com.castprogramms.balamutbatut.R

class SetDataNodeAlertDialog {
    fun createDialog(context: Context): AlertDialog.Builder {
        val view = LayoutInflater.from(context).inflate(R.layout.set_data_node_fragment, null)
        return AlertDialog.Builder(context)
            .setTitle(context.resources.getString(R.string.title_set_data))
            .setView(view)
    }
}