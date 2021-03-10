package com.castprogramms.balamutbatut.ui.insertdatauser

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.annotation.RequiresApi
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.Constraints
import androidx.core.view.marginTop
import androidx.fragment.app.Fragment
import com.castprogramms.balamutbatut.MainActivity
import com.castprogramms.balamutbatut.MainActivityStudent
import com.castprogramms.balamutbatut.R
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.castprogramms.balamutbatut.users.Student
import com.castprogramms.balamutbatut.tools.Registration
import com.castprogramms.balamutbatut.graph.Node
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.google.android.material.card.MaterialCardView
import com.google.android.material.radiobutton.MaterialRadioButton
import java.util.*

class InsertDataUserFragment: Fragment() {

    lateinit var editDate: MaterialButton
    var date = ""
    val dateSetListener = object : DatePickerDialog.OnDateSetListener{
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            editDate.text = requireContext().getString(R.string.your_date) + " $dayOfMonth-${month+1}-$year"
            date = "$dayOfMonth-${month+1}-$year"
        }
    }
    var sex = ""

    val dateAndTime = Calendar.getInstance()
    lateinit var parentConstraintLayout : ConstraintLayout

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
        val maleRadioButton : MaterialRadioButton = view.findViewById(R.id.male)
        val femaleRadioButton : MaterialRadioButton = view.findViewById(R.id.female)
        val closeButton : MaterialButton = view.findViewById(R.id.close_message)
        val messageCard : MaterialCardView = view.findViewById(R.id.info)
        parentConstraintLayout = view.findViewById(R.id.container_const)

        finishRegistration.setOnClickListener {
            val listEmptyEditText = mutableListOf<Boolean>()
            if (editFirstName.text.isNullOrBlank()){
                editFirstName.error = requireContext().getString(R.string.add_first_name)
                listEmptyEditText.add(false)
            }
            else
                listEmptyEditText.add(true)

            if (editLastName.text.isNullOrBlank()){
                editLastName.error = requireContext().getString(R.string.add_second_name)
                listEmptyEditText.add(false)
            }
            else
                listEmptyEditText.add(true)

            if (editDate.text == requireContext().getString(R.string.date)){
                editDate.error = requireContext().getString(R.string.add_date)
                listEmptyEditText.add(false)
            }
            else
                listEmptyEditText.add(true)
            if (!maleRadioButton.isChecked && !femaleRadioButton.isChecked){
                maleRadioButton.error = requireContext().resources.getString(R.string.add_sex)
                listEmptyEditText.add(false)
            }
            else{
                if (maleRadioButton.isChecked)
                    sex = requireContext().resources.getString(R.string.male)

                if (femaleRadioButton.isChecked)
                    sex = requireContext().resources.getString(R.string.female)
            }
            if (!listEmptyEditText.contains(false)) {
                addDataStudent(Student(editFirstName.text.toString(), editLastName.text.toString(),
                    date, sex, listOf(Node(mutableListOf()))))
                Registration().loadDate()
                (requireActivity() as MainActivity).toStudent()
            }
        }
        editDate.setOnClickListener {
            setDate()
        }
        closeButton.setOnClickListener {
            onDeleteView(messageCard)
        }
        return view
    }

    fun onDeleteView(view: View){
        parentConstraintLayout.removeView(view)
    }

    fun setDate() {
        DatePickerDialog(
            requireContext(),dateSetListener,
            dateAndTime.get(Calendar.YEAR),
            dateAndTime.get(Calendar.MONTH),
            dateAndTime.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun addDataStudent(student: Student){
        DataUserFirebase().addStudent(student, User.id)
    }

}
