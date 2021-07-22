package com.castprogramms.balamutbatut.tools

import android.content.Context
import android.widget.ArrayAdapter
import com.castprogramms.balamutbatut.R

class CustomAdapterSpinner(context: Context, layout: Int): ArrayAdapter<String>(context, layout){
    override fun getCount(): Int {
    val count = super.getCount()
    return if (count > 0) count - 1 else count
    }
}