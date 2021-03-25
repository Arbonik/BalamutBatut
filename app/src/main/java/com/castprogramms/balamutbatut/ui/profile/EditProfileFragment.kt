package com.castprogramms.balamutbatut.ui.profile

import android.R.attr.data
import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.castprogramms.balamutbatut.BalamutApplication.Companion.repository
import com.castprogramms.balamutbatut.MainActivity
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.graph.Node
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.tools.TypesUser
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.tools.User.id
import com.castprogramms.balamutbatut.tools.User.img
import com.castprogramms.balamutbatut.tools.User.student
import com.castprogramms.balamutbatut.tools.User.trainer
import com.castprogramms.balamutbatut.users.Person
import com.castprogramms.balamutbatut.users.Student
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView


class EditProfileFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        val acceptEditButton : MaterialButton = view.findViewById(R.id.accept_edit)
        val userName : TextInputEditText = view.findViewById(R.id.name_user)
        val userLastName : TextInputEditText = view.findViewById(R.id.last_name_user)
        val userIcon : CircleImageView = view.findViewById(R.id.user_icon)

        repository.user.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    when (it.data!!.type) {
                        TypesUser.TRAINER.desc -> {
                            userName.setText(User.trainer?.first_name, TextView.BufferType.EDITABLE)
                            userLastName.setText(
                                User.trainer?.second_name,
                                TextView.BufferType.EDITABLE
                            )
                            Glide.with(this)
                                .load(User.trainer?.img)
                                .into(userIcon)
                        }
                        TypesUser.STUDENT.desc -> {
                            userName.setText(User.student?.first_name, TextView.BufferType.EDITABLE)
                            userLastName.setText(
                                User.student?.second_name,
                                TextView.BufferType.EDITABLE
                            )
                            Glide.with(this)
                                .load(User.student?.img)
                                .into(userIcon)
                        }
                    }
                }
            }
            userIcon.setOnClickListener {
                val intent = Intent(
                    Intent.ACTION_PICK,
                    MediaStore.Images.Media.INTERNAL_CONTENT_URI
                )
                intent.putExtra("return-data", true)
                startActivityForResult(intent, 1)
            }

            acceptEditButton.setOnClickListener {
                val listEmptyEditText = mutableListOf<Boolean>()
                if (userName.text.isNullOrBlank()){
                    userName.error = requireContext().getString(R.string.add_first_name)
                    listEmptyEditText.add(false)
                }
                else
                    listEmptyEditText.add(true)

                if (userLastName.text.isNullOrBlank()){
                    userLastName.error = requireContext().getString(R.string.add_second_name)
                    listEmptyEditText.add(false)
                }
                else
                    listEmptyEditText.add(true)
                if (!listEmptyEditText.contains(false)) {
                    if (User.typeUser == TypesUser.STUDENT){
                        updateStudent(Student(
                            userName.text.toString(), userLastName.text.toString(),
                            student?.date.toString(), student?.sex.toString(), userIcon.toString(),
                            listOf(Node(mutableListOf())).apply {student?.groupID = Person.notGroup }), User.id)
                        loadDateStudnet()
                        findNavController().navigate(R.id.action_editProfileFragment_to_profileFragment)
                    }
                    if (User.typeUser == TypesUser.TRAINER){
                        updateStudent(Student(
                            userName.text.toString(), userLastName.text.toString(),
                            trainer?.date.toString(), trainer?.sex.toString(), userIcon.toString(),
                            listOf(Node(mutableListOf())).apply {trainer?.groupID = Person.notGroup }), User.id)
                        loadDateStudnet()
                        findNavController().navigate(R.id.action_editProfileFragment2_to_profile_Fragment)
                    }


                    //(requireActivity() as MainActivity).toStudent()
                }
            }
        }
        return view
    }

    fun updateStudent(student: Student, studentID: String) {
        DataUserFirebase().updateStudent(student, studentID)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, dataImg: Intent?) {
        super.onActivityResult(requestCode, resultCode, dataImg)
        if (requestCode == 1 && dataImg != null) {
            val uri: Uri? = dataImg.getData();
            val imageView = view?.findViewById<ImageView>(R.id.user_icon);
            imageView?.setImageURI(uri)}
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
                            User.setValueStudent(User.student?.apply {
                                groupID = it.documents.first().getString("name").toString()
                            }!!
                            )
                        }
                    }
                }
            }
    }
}
