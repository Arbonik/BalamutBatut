package com.castprogramms.balamutbatut.ui.insertdatauser

import android.app.DatePickerDialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Vibrator
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.DatePicker
import androidx.appcompat.app.AlertDialog
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.net.toUri
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.castprogramms.balamutbatut.MainActivity
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.Repository
import com.castprogramms.balamutbatut.databinding.InsertdatauserFragmentBinding
import com.castprogramms.balamutbatut.graph.Node
import com.castprogramms.balamutbatut.tools.ActionsWithCoins
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.tools.TypesUser
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.ui.qrcode.QrCodeViewModel
import com.castprogramms.balamutbatut.users.Person
import com.castprogramms.balamutbatut.users.Student
import com.castprogramms.balamutbatut.users.Trainer
import com.google.android.gms.vision.Frame
import com.google.android.gms.vision.barcode.Barcode
import com.google.android.gms.vision.barcode.BarcodeDetector
import com.google.android.material.button.MaterialButton
import com.google.android.material.card.MaterialCardView
import com.google.android.material.radiobutton.MaterialRadioButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.switchmaterial.SwitchMaterial
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import com.theartofdev.edmodo.cropper.CropImage
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*

class InsertDataUserFragment: Fragment() {
    private val repository : Repository by inject()
    val viewModel: QrCodeViewModel by viewModel()
    lateinit var editDate: MaterialButton
    var date = ""
    val dateSetListener = object : DatePickerDialog.OnDateSetListener{
        override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
            editDate.text = requireContext().getString(R.string.your_date) + " $dayOfMonth-${month+1}-$year"
            viewModel.date = "$dayOfMonth-${month+1}-$year"
        }
    }
    var sex = ""
    var img = ""
    var typeU = TypesUser.STUDENT.desc

    private val CODE = "569820"

    lateinit var code_for_trainer : TextInputEditText
    lateinit var code_for_trainer_container : TextInputLayout
    val dateAndTime = Calendar.getInstance()
    lateinit var parentConstraintLayout : ConstraintLayout

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.insertdatauser_fragment, container, false)
        val binding = InsertdatauserFragmentBinding.bind(view)
        binding.scan.setOnClickListener {
//            CropImage.startPickImageActivity(requireContext(), this)
//                .setAutoZoomEnabled(true)
//                .start(requireContext(), this)
//            startActivityForResult(Intent(MediaStore.ACTION_IMAGE_CAPTURE), 2)
            findNavController()
                .navigate(R.id.action_insertDataUserFragment2_to_qrCodeScannerFragment,
                    Bundle().apply { putString("action", ActionsWithCoins.GET.desc) })
        }
        editDate = view.findViewById(R.id.insert_date)
        if (viewModel.date.isNotEmpty())
            editDate.text = requireContext().getString(R.string.your_date) + viewModel.date
        val editFirstName: TextInputEditText = view.findViewById(R.id.name_user)
        val editLastName: TextInputEditText = view.findViewById(R.id.last_name_user)
        val finishRegistration : MaterialButton = view.findViewById(R.id.next)
        val maleRadioButton : MaterialRadioButton = view.findViewById(R.id.male)
        val femaleRadioButton : MaterialRadioButton = view.findViewById(R.id.female)
        val closeButton : MaterialButton = view.findViewById(R.id.close_message)
        val messageCard : MaterialCardView = view.findViewById(R.id.info)
        val switchMaterial : SwitchMaterial = view.findViewById(R.id.switch_trainer)

        code_for_trainer = view.findViewById(R.id.code_for_trainer)
        code_for_trainer_container = view.findViewById(R.id.code_for_trainer_container)
        parentConstraintLayout = view.findViewById(R.id.container_const)

        switchMaterial.setOnCheckedChangeListener { _b, isChecked ->
            when(switchMaterial.isChecked){
                true -> {
                    code_for_trainer_container.visibility = View.VISIBLE

                }
                false -> {
                    code_for_trainer_container.visibility = View.GONE
                }
            }
        }

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
            if (switchMaterial.isChecked){
                if (code_for_trainer.text.isNullOrBlank()){
                    code_for_trainer.error = requireContext().getString(R.string.message_trainer)
                    listEmptyEditText.add(false)
                }
                else{
                    listEmptyEditText.add(true)
                }
            }

            if (code_for_trainer.text.toString() == CODE) {
                typeU = TypesUser.TRAINER.desc
            } else {
                code_for_trainer.error = requireContext().getString(R.string.invalid_code)
            }
            if (!listEmptyEditText.contains(false)) {

                if (switchMaterial.isChecked && code_for_trainer.text.toString()  != CODE) {

                    val alert = AlertDialog.Builder(requireContext())
                    alert.setTitle(requireContext().getString(R.string.invalid_code))
                    alert.setMessage(requireContext().getString(R.string.invalid_code_message))

                    alert.setPositiveButton(android.R.string.ok) { dialog, which ->
                        code_for_trainer.setText("")
                    }

                    alert.setNegativeButton(R.string.no_trainer) { dialog, which ->
                        typeU = TypesUser.STUDENT.desc
                        switchMaterial.isChecked = false
                        code_for_trainer.setText("")
                        code_for_trainer_container.visibility = View.GONE
                    }.create()
                    alert.show()

                } else{
                    Log.d("as", typeU)
                    when (typeU) {
                        TypesUser.STUDENT.desc -> {
                            addDataStudent(Student(
                                editFirstName.text.toString(), editLastName.text.toString(),
                                viewModel.date, sex, User.img, listOf(), listOf(Node(mutableListOf())),
                                mapOf(), if (User.isScan) 50 else 0
                            ).apply {
                                groupID = Person.notGroup
                            })
                            repository.loadUserData(User.id)
                            (requireActivity() as MainActivity).toStudent()
                        }
                        TypesUser.TRAINER.desc -> {
                            addDataTrainer(
                                Trainer(
                                    editFirstName.text.toString(), editLastName.text.toString(),
                                    viewModel.date, sex, User.img, Person.notGroup)
                            )
                            repository.loadUserData(User.id)
                            (requireActivity() as MainActivity).toTrainer()
                        }
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        val result = CropImage.getActivityResult(data)
        val barcodeDetector = BarcodeDetector.Builder(requireContext())
            .setBarcodeFormats(Barcode.QR_CODE)
            .build()
        val frame = Frame.Builder().setBitmap(result.bitmap).build()
        val qrCodes = barcodeDetector.detect(frame)
        Log.e("Data", qrCodes.toString())
        if(qrCodes.size() != 0){
//            binding.textView.post {
                val vibrator: Vibrator =
                    requireActivity().applicationContext.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
                vibrator.vibrate(100)
                val uri = qrCodes.valueAt(0).displayValue.toString()
                Snackbar.make(requireView(), uri, Snackbar.LENGTH_LONG).show()
//                binding.camerapreview.visibility = View.GONE
//                binding.textView.visibility = View.VISIBLE
//                binding.textView.text = uri
//                Thread.sleep(1000)
//            }
        }
    }

    fun onDeleteView(view: View){
        parentConstraintLayout.removeView(view)
    }

    private fun setDate() {
        DatePickerDialog(
            requireContext(), dateSetListener,
            dateAndTime.get(Calendar.YEAR),
            dateAndTime.get(Calendar.MONTH),
            dateAndTime.get(Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun addDataStudent(student: Student){
        repository.addStudent(student, User.id)
    }

    private fun addDataTrainer(trainer: Trainer){
        repository.addTrainer(trainer, User.id)
    }
}
