package com.castprogramms.balamutbatut.ui.editelement

import android.app.AlertDialog
import android.content.Intent
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Bundle
import android.os.Message
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.Fragment
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ErrorDialogBinding
import com.castprogramms.balamutbatut.databinding.FragmentEditElementBinding
import com.castprogramms.balamutbatut.databinding.SuccessDialogBinding
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.Element
import com.castprogramms.balamutbatut.tools.User
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import org.koin.androidx.viewmodel.ext.android.viewModel

class EditElementFragment : Fragment(R.layout.fragment_edit_element) {
    val viewModel: EditElementViewModel by viewModel()
    var element = Element()
    var uri = Uri.parse("")
    var isError = false
    var isSuccess = false
    lateinit var binding: FragmentEditElementBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        element = Element(requireArguments().getString("element", "Пусто"))
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditElementBinding.bind(view)
        binding.elementTitle.text = element.name
        addTextWatcher(binding)
        binding.load.setOnClickListener {
            binding.load.error = null
            val intent = Intent(
                Intent.ACTION_PICK
            )
            intent.type = "video/"
            intent.putExtra("return-data", true)
            startActivityForResult(intent, 1)
        }
        binding.loadVideoInCloud.setOnClickListener {
            if (validate(binding)) {
                observeResults(binding)
            }
        }
    }

    private fun observeResults(binding: FragmentEditElementBinding) {
        val desc = binding.desc.text.toString()
        val lifeData = viewModel.loadVideoAndDesc(desc, uri, element.name)
        Log.e("data", User.id)

        lifeData.observe(viewLifecycleOwner, {
            when(it){
                is Resource.Error -> {
                    createErrorAlertDialog(it.message.toString())
                }
                is Resource.Loading -> {

                }
                is Resource.Success -> {
                    createSuccessAlertDialog(it.data.toString())
                }
            }
        })
//        val lifeDataDesc = viewModel.loadDesc(desc, element.name)

//        lifeDataVideo.observe(viewLifecycleOwner, {
//            when (it) {
//                is Resource.Error -> {
//                    if (!isError)
//                        createErrorAlertDialog(it.message.toString())
//                }
//                is Resource.Loading -> {}
//                is Resource.Success -> {}
//            }
//        })
//
//        lifeDataDesc.observe(viewLifecycleOwner, {
//            when (it) {
//                is Resource.Error -> {
//                    if (!isError)
//                        createErrorAlertDialog(it.message.toString())
//                }
//                is Resource.Loading -> {
//                    createLoadingAlertDialog()
//                }
//                is Resource.Success -> {
//                    if (lifeDataVideo.value is Resource.Success && !isSuccess)
//                        createSuccessAlertDialog("Данные успешно отправлены")
//                }
//            }
//        })
    }

    private fun createSuccessAlertDialog(message: String) {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.success_dialog, null)
        val binding = SuccessDialogBinding.bind(view)
        binding.textBody.text = message
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(0))
        alertDialog.show()
    }

    private fun createLoadingAlertDialog() {

    }

    private fun createErrorAlertDialog(message: String) {
        val view = LayoutInflater.from(requireContext()).inflate(R.layout.error_dialog, null)
        val binding = ErrorDialogBinding.bind(view)
        binding.textBody.text = message
        val alertDialog = AlertDialog.Builder(requireContext())
            .setView(view)
            .create()

        alertDialog.window?.setBackgroundDrawable(ColorDrawable(0))
        alertDialog.show()
    }

    private fun addTextWatcher(binding: FragmentEditElementBinding) {
        binding.desc.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.descContainer.helperText = null
            }
        })
    }

    private fun validate(binding: FragmentEditElementBinding): Boolean {
        return if (uri == Uri.parse("") && binding.desc.text.isNullOrBlank()) {
            if (uri == Uri.parse(""))
                binding.load.error = resources.getString(R.string.error_load)
            if (binding.desc.text.isNullOrBlank())
                binding.descContainer.helperText = resources.getString(R.string.error_desc)
            false
        } else {
            true
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, dataImg: Intent?) {
        super.onActivityResult(requestCode, resultCode, dataImg)
        if (requestCode == 1 && dataImg != null) {
            val uri: Uri? = dataImg.data
            if (uri != null) {
                this.uri = uri
                Log.e("test", uri.toString())
                binding.expandableView.visibility = View.VISIBLE
                val player = SimpleExoPlayer.Builder(requireContext()).build()
                binding.video.playerView.player = player
                binding.video.releasePlayer()
                binding.video.startPlayer()
                val media = MediaItem.fromUri(uri)
                player.addMediaItem(media)
                player.prepare()
                player.play()
            }
        }
    }
}