package com.castprogramms.balamutbatut.ui.editprofile

import android.content.Intent
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.castprogramms.balamutbatut.*
import com.castprogramms.balamutbatut.databinding.FragmentEditProfileBinding
import com.castprogramms.balamutbatut.network.Repository
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.TypesUser
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.tools.User.student
import com.castprogramms.balamutbatut.tools.User.trainer
import com.google.android.material.button.MaterialButton
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditProfileFragment : Fragment(R.layout.fragment_edit_profile) {
    val viewModel: EditProfileViewModel by viewModel()
    lateinit var binding: FragmentEditProfileBinding

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditProfileBinding.bind(view)
        requireActivity().setTitle(R.string.all_person_data)
        if (User.typeUser == TypesUser.STUDENT) {
            User.mutableLiveDataStudent.observe(viewLifecycleOwner, {
                if (it != null) {
                    binding.lastNameUser.setText(it.second_name, TextView.BufferType.EDITABLE)
                    binding.studentName.setText(it.first_name, TextView.BufferType.EDITABLE)
                    Glide.with(requireView())
                        .load(it.img)
                        .addListener(object : RequestListener<Drawable> {
                            override fun onLoadFailed(
                                e: GlideException?,
                                model: Any?,
                                target: Target<Drawable>?,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding.userIcon.setImageResource(R.drawable.male_user)

                                return true
                            }

                            override fun onResourceReady(
                                resource: Drawable?,
                                model: Any?,
                                target: Target<Drawable>?,
                                dataSource: DataSource?,
                                isFirstResource: Boolean
                            ): Boolean {
                                binding.userIcon.setImageDrawable(resource)

                                return true
                            }
                        })
                        .into(binding.userIcon)
                }
            })
        }
        else{
            if (User.typeUser == TypesUser.TRAINER){
                User.mutableLiveDataTrainer.observe(viewLifecycleOwner, {
                    if (it != null) {
                        binding.lastNameUser.setText(it.second_name, TextView.BufferType.EDITABLE)
                        binding.studentName.setText(it.first_name, TextView.BufferType.EDITABLE)
                        Glide.with(requireView())
                            .load(it.img)
                            .addListener(object : RequestListener<Drawable> {
                                override fun onLoadFailed(
                                    e: GlideException?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    binding.userIcon.setImageResource(R.drawable.male_user)

                                    return true
                                }

                                override fun onResourceReady(
                                    resource: Drawable?,
                                    model: Any?,
                                    target: Target<Drawable>?,
                                    dataSource: DataSource?,
                                    isFirstResource: Boolean
                                ): Boolean {
                                    binding.userIcon.setImageDrawable(resource)

                                    return true
                                }
                            })
                            .into(binding.userIcon)
                    }
                })
            }
        }
        binding.userIcon.setOnClickListener {
            val intent = Intent(
                Intent.ACTION_PICK,
                MediaStore.Images.Media.INTERNAL_CONTENT_URI
            )
            intent.putExtra("return-data", true)
            startActivityForResult(intent, 1)
        }

        //TODO то же самое, пофикксить валидацию
        binding.acceptEdit.setOnClickListener {
            val listEmptyEditText = mutableListOf<Boolean>()

            if (binding.studentName.text.isNullOrBlank()) {
                binding.studentName.error = requireContext().getString(R.string.add_first_name)
                listEmptyEditText.add(false)
            } else
                listEmptyEditText.add(true)

            if (binding.lastNameUser.text.isNullOrBlank()) {
                binding.lastNameUser.error =
                    requireContext().getString(R.string.add_second_name)
                listEmptyEditText.add(false)
            } else
                listEmptyEditText.add(true)
            if (!listEmptyEditText.contains(false)) {
                viewModel.updateUserFirstName(binding.studentName.text.toString(), User.id)
                viewModel.updateUserSecondName(binding.lastNameUser.text.toString(), User.id)
                findNavController().popBackStack()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, dataImg: Intent?) {
        super.onActivityResult(requestCode, resultCode, dataImg)
        if (requestCode == 1 && dataImg != null) {
            val uri: Uri? = dataImg.data
            binding.userIcon.setImageURI(uri)
            if (uri != null)
                viewModel.loadPhotoUser(uri, User.id)
        }
    }
}
