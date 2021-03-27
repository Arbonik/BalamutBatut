package com.castprogramms.balamutbatut.ui.profile

import android.content.Intent
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
import com.bumptech.glide.Glide
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.Repository
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.tools.TypesUser
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.tools.User.student
import com.castprogramms.balamutbatut.tools.User.trainer
import com.castprogramms.balamutbatut.users.Student
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import de.hdodenhof.circleimageview.CircleImageView
import org.koin.android.ext.android.inject

class EditProfileFragment : Fragment() {
    private val repository: Repository by inject()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_edit_profile, container, false)
        val acceptEditButton: MaterialButton = view.findViewById(R.id.accept_edit)
        val userName: TextInputEditText = view.findViewById(R.id.name_user)
        val userLastName: TextInputEditText = view.findViewById(R.id.last_name_user)
        val userIcon: CircleImageView = view.findViewById(R.id.user_icon)

        repository.user.observe(viewLifecycleOwner) {
            when (it) {
                is Resource.Success -> {
                    when (it.data!!.type) {
                        TypesUser.TRAINER.desc -> {
                            userName.setText(trainer?.first_name, TextView.BufferType.EDITABLE)
                            userLastName.setText(
                                trainer?.second_name,
                                TextView.BufferType.EDITABLE
                            )
                            Glide.with(this)
                                .load(trainer?.img)
                                .error(R.drawable.male_user)
                                .into(userIcon)
                        }
                        TypesUser.STUDENT.desc -> {
                            userName.setText(student?.first_name, TextView.BufferType.EDITABLE)
                            userLastName.setText(
                                student?.second_name,
                                TextView.BufferType.EDITABLE
                            )
                            if (student?.img != null){
                                Glide.with(this)
                                    .load(student?.img)
                                    .error(R.drawable.male_user)
                                    .into(userIcon)
                            }
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

                if (userName.text.isNullOrBlank()) {
                    userName.error = requireContext().getString(R.string.add_first_name)
                    listEmptyEditText.add(false)
                } else
                    listEmptyEditText.add(true)

                if (userLastName.text.isNullOrBlank()) {
                    userLastName.error = requireContext().getString(R.string.add_second_name)
                    listEmptyEditText.add(false)
                } else
                    listEmptyEditText.add(true)
                if (!listEmptyEditText.contains(false)) {
                    repository.updateUserFirstName(userName.text.toString(), User.id)
                    repository.updateUserSecondName(userLastName.text.toString(), User.id)
                    //repository.updateUserIcon(userIcon.toString(), User.id)
                }
            }
        }
        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, dataImg: Intent?) {
        super.onActivityResult(requestCode, resultCode, dataImg)
        if (requestCode == 1 && dataImg != null) {
            val uri: Uri? = dataImg.data
            val imageView = view?.findViewById<ImageView>(R.id.user_icon)
            imageView?.setImageURI(uri)
        }
    }
}
