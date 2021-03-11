package com.castprogramms.balamutbatut.ui.node

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.graph.Node
import com.castprogramms.balamutbatut.graph.dataNode
import com.castprogramms.balamutbatut.tools.NodeData
import org.w3c.dom.Text

class ChooseNodeViewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view =  inflater.inflate(R.layout.fragment_choose_node_view, container, false)

        val nameElement : TextView = view.findViewById(R.id.nameElement)
        val descriptionElement : TextView = view.findViewById(R.id.infoElement)

        nameElement.text = dataNode.nameElement
        descriptionElement.text = dataNode.description

        return view
    }


}