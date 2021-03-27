package com.castprogramms.balamutbatut.ui.insertdatauser

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.Fragment
import com.castprogramms.balamutbatut.MainActivity
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.graph.Node
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.tools.TypesUser
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.users.Person
import com.castprogramms.balamutbatut.users.Student
import com.castprogramms.balamutbatut.users.Trainer
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.textfield.TextInputEditText
import java.lang.reflect.Type
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
    var img = ""
    var typeU = TypesUser.STUDENT.desc

    private val CODE_LENGTH = 6
    private val CODE = "569820"

    lateinit var code_for_trainer : TextInputEditText
    val dateAndTime = Calendar.getInstance()
    lateinit var parentConstraintLayout : ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.insertdatauser_fragment, container, false)
        editDate = view.findViewById(R.id.insert_date)
        val editFirstName: TextInputEditText = view.findViewById(R.id.name_user)
        val editLastName: TextInputEditText = view.findViewById(R.id.last_name_user)
        val finishRegistration : MaterialButton = view.findViewById(R.id.next)
        val maleRadioButton : MaterialRadioButton = view.findViewById(R.id.male)
        val femaleRadioButton : MaterialRadioButton = view.findViewById(R.id.female)
        val closeButton : MaterialButton = view.findViewById(R.id.close_message)
        val messageCard : MaterialCardView = view.findViewById(R.id.info)
        code_for_trainer = view.findViewById(R.id.code_for_trainer)
        parentConstraintLayout = view.findViewById(R.id.container_const)

        code_for_trainer

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

            /*if (editDate.text == requireContext().getString(R.string.date)){
                editDate.error = requireContext().getString(R.string.add_date)
                listEmptyEditText.add(false)
            }
            else
                listEmptyEditText.add(true)*/
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
                if (code_for_trainer.text.toString() == CODE) {
                    typeU = TypesUser.TRAINER.desc
                }
                Log.d("as", typeU)
                when (typeU) {
                    TypesUser.STUDENT.desc -> {
                        addDataStudent(Student(
                            editFirstName.text.toString(), editLastName.text.toString(),
                            date, sex, User.img, listOf(), listOf(Node(mutableListOf()))
                        ).apply {
                            groupID = Person.notGroup
                        })
                        loadDateStudnet()
                        (requireActivity() as MainActivity).toStudent()
                    }
                    TypesUser.TRAINER.desc -> {
                        addDataTrainer(
                            Trainer(
                                editFirstName.text.toString(), editLastName.text.toString(),
                                date, sex, User.img, ""
                            ).apply {
                                groupID = Person.notGroup
                            })
                        loadDateStudnet()
                        (requireActivity() as MainActivity).toTrainer()
                    }
                }
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
    private fun shouldShowError(): Boolean {
        val kodLength: Int = code_for_trainer.text?.length ?: 0
        return kodLength == CODE_LENGTH
    }
    private fun showError() {
        code_for_trainer.error = getString(R.string.error_kod_trainer)
    }

    private fun hideError() {
        code_for_trainer.error = ""
    }

    fun onDeleteView(view: View){
        parentConstraintLayout.removeView(view)
    }

    fun setDate() {
        DatePickerDialog(
            requireContext(), dateSetListener,
            dateAndTime.get(Calendar.YEAR),
            dateAndTime.get(Calendar.MONTH),
            dateAndTime.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    fun addDataStudent(student: Student){
        DataUserFirebase().addStudent(student, User.id)
    }

    fun addDataTrainer(trainer: Trainer){
        DataUserFirebase().addTrainer(trainer, User.id)
    }

    private fun loadDateStudnet(){
        DataUserFirebase().getUser(User.id)
            .get().addOnCompleteListener {
                if (it.isSuccessful) {
                    val data = it.result
                    if (data != null && data.data != null) {
                        User.mutableLiveDataSuccess.postValue(true)
                        User.setValueStudent(data.toObject(Student::class.java)!!)
                    } else {
                        User.mutableLiveDataSuccess.postValue(false)
                    }
                } else {
                    User.mutableLiveDataSuccess.postValue(false)
                }
            }.continueWith {
                if (User.student != null) {
                    DataUserFirebase().getStudentsGroup(User.id).addOnSuccessListener {
                        if (it.documents.isNotEmpty()) {
                            User.setValueStudent(
                                User.student?.apply {
                                    groupID = it.documents.first().getString("name").toString()
                                }!!
                            )
                    }
                }
            }
        }
    }
}
