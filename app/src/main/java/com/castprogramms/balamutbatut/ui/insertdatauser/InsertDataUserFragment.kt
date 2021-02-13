package com.castprogramms.balamutbatut.ui.insertdatauser

import android.app.DatePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import com.castprogramms.balamutbatut.MainActivityStudent
import com.castprogramms.balamutbatut.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.castprogramms.balamutbatut.users.Student
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.castprogramms.balamutbatut.tools.Registration
import com.castprogramms.balamutbatut.graph.Node
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import java.util.*

class InsertDataUserFragment: Fragment() {

    lateinit var editDate: MaterialButton
    var date = ""
    val dateSetListener = object : DatePickerDialog.OnDateSetListener{
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            editDate.text = requireContext().getString(R.string.your_date) +" $dayOfMonth-${month+1}-$year"
            date = "$dayOfMonth-${month+1}-$year"
        }
    }

    val dateAndTime = Calendar.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.insertdatauser_fragment, container, false)
        editDate = view.findViewById(R.id.date)
        val editFirstName: TextInputEditText = view.findViewById(R.id.name)
        val editLastName: TextInputEditText = view.findViewById(R.id.last_name)
        val finishRegistration : MaterialButton = view.findViewById(R.id.next)
        finishRegistration.setOnClickListener {
            val editTexts = mutableListOf(editFirstName, editLastName)
            val validationEditText = validationDate(editTexts)
            for(i in validationEditText.indices){
                if (!validationEditText[i]){
                    setWarningColorEditText(editTexts[i])
                }
            }
            if (!validationEditText.contains(false)) {
                addDataStudent(Student(editFirstName.text.toString(), editLastName.text.toString(),
                    date, listOf(Node(mutableListOf()))))
//                val googleAuth = GoogleSignIn.getLastSignedInAccount(requireContext())
//                if (googleAuth != null){
//                    val authentication = Registration().auth(googleAuth)
//                    if (authentication)
                Registration().loadDate()
                        (requireActivity() as MainActivityStudent).toMainGraph(true)
//                }
            }
        }
        editDate.setOnClickListener {
            setDate()
        }

        return view
    }

    fun setDate() {
        DatePickerDialog(
            requireContext(),dateSetListener,
            dateAndTime.get(Calendar.YEAR),
            dateAndTime.get(Calendar.MONTH),
            dateAndTime.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun validationDate(mutableList: MutableList<TextInputEditText>): MutableList<Boolean>{
        val listEmptyEditText = mutableListOf<Boolean>()
        mutableList.forEach {
            if (it.text.isNullOrBlank())
                listEmptyEditText.add(false)
            else
                listEmptyEditText.add(true)
        }
        return listEmptyEditText
    }

    fun setWarningColorEditText(editText: TextInputEditText){
        editText.setBackgroundColor(requireContext().resources.getColor(R.color.red))
    }

    fun addDataStudent(student: Student){
        DataUserFirebase().addStudent(student, User.id)
    }
}