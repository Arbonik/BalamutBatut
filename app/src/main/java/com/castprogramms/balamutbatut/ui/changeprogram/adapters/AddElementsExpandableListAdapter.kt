package com.castprogramms.balamutbatut.ui.changeprogram.adapters

import android.content.Context
import android.graphics.Typeface
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseExpandableListAdapter
import android.widget.TextView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ListItemAddElementBinding
import com.castprogramms.balamutbatut.tools.Element
import kotlin.math.floor
import kotlin.math.pow

class AddElementsExpandableListAdapter(
    private val context: Context,
    private var titleList: List<String>,
    private var dataList: Map<String, List<Element>>
) : BaseExpandableListAdapter() {
    fun setData(titleList: List<String>, dataList: Map<String, List<Element>>){
        if (this.titleList != titleList || this.dataList != dataList) {
            this.titleList = titleList
            this.dataList = dataList
            notifyDataSetChanged()
        }
    }
    var checkedElements = mutableMapOf<String, List<Element>>()
    override fun getCombinedChildId(groupId: Long, childId: Long): Long {
        val or = 0x7000000000000000L
        val group = groupId and 0x7FFFFFFF shl 32
        val child = childId and -0x1
        return or or group or child
//        return 0
    }

    override fun getGroupCount(): Int {
        return this.titleList.size
    }

    override fun getChildrenCount(listPosition: Int): Int {
        return (this.dataList[this.titleList[listPosition]] ?: error("")).size
    }

    override fun getGroup(listPosition: Int): Any {
        return this.titleList[listPosition]
    }

    override fun getChild(listPosition: Int, expandedListPosition: Int): Any {
        val positionGroup = if (listPosition != 0)floor(getChildId(listPosition, expandedListPosition) / (listPosition  * 10.0.pow(getChildrenCount(listPosition).toString().length))).toInt() else 0
        val positionChild = if ((getChildId(listPosition, expandedListPosition) % (listPosition  * 10.0.pow(getChildrenCount(listPosition).toString().length))).isNaN()) 0 else getChildId(listPosition, expandedListPosition) % (listPosition  * 10.0.pow(getChildrenCount(listPosition).toString().length))
        return this.dataList[this.titleList[listPosition]]!![getCombinedChildId(listPosition.toLong(), expandedListPosition.toLong()).toInt()]
    }

    override fun getGroupId(listPosition: Int): Long {
        return listPosition.toLong()
    }

    override fun getChildId(listPosition: Int, expandedListPosition: Int): Long {
        Log.e("data", 10.0.pow(getChildrenCount(listPosition).toString().length.toDouble()).toString())
        Log.e("data", (listPosition * (10.0.pow(getChildrenCount(listPosition).toString().length.toDouble())) + expandedListPosition).toString())
        return (listPosition * (10.0.pow(getChildrenCount(listPosition).toString().length.toDouble())) + expandedListPosition).toLong()
    }

    override fun hasStableIds(): Boolean {
        return true
    }

    override fun getGroupView(
        listPosition: Int,
        isExpanded: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val listTitle = getGroup(listPosition) as String
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            convertView = layoutInflater.inflate(R.layout.list_group, parent, false)
        }
        val listTitleTextView = convertView!!.findViewById<TextView>(R.id.listTitle)
        listTitleTextView.setTypeface(null, Typeface.BOLD)
        listTitleTextView.text = listTitle
        return convertView
    }

    override fun getChildView(
        listPosition: Int,
        expandedListPosition: Int,
        isLastChild: Boolean,
        convertView: View?,
        parent: ViewGroup
    ): View {
        var convertView = convertView
        val element = getChild(listPosition, expandedListPosition) as Element
        val group = getGroup(listPosition) as String
        if (convertView == null) {
            val layoutInflater = LayoutInflater.from(context)
            convertView = layoutInflater.inflate(R.layout.list_item_add_element, parent, false)
            val binding = ListItemAddElementBinding.bind(convertView)
            convertView!!.setOnClickListener{
                Log.e("data", element.name)
                binding.checkbox.isChecked = !binding.checkbox.isChecked
                if (binding.checkbox.isChecked)
                    addElement(element, group)
                else
                    removeElement(element, group)
            }
            binding.checkbox.setOnClickListener {
                if (binding.checkbox.isChecked)
                    addElement(element, group)
                else
                    removeElement(element, group)
            }
            binding.expandedListItem.text = element.name
        }
        return convertView
    }

    override fun isChildSelectable(listPosition: Int, expandedListPosition: Int): Boolean {
        return true
    }
    fun addElement(element: Element, group: String){
        if (checkedElements[group] == null)
            checkedElements.put(group, listOf(element))
        else
            checkedElements[group] = checkedElements[group]?.toMutableList().apply {
                this?.add(element)
            }!!
    }

    fun removeElement(element: Element, group: String){
        if (checkedElements[group] != null){
            checkedElements[group] = checkedElements[group]?.toMutableList().apply {
                this?.remove(element)
            }!!
        }
    }
}