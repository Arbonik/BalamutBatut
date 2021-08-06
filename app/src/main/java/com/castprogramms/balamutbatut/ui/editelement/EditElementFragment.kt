package com.castprogramms.balamutbatut.ui.editelement

import android.app.AlertDialog
import android.content.Intent
import android.content.res.ColorStateList
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ErrorDialogBinding
import com.castprogramms.balamutbatut.databinding.FragmentEditElementBinding
import com.castprogramms.balamutbatut.databinding.SuccessDialogBinding
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.CustomAdapterSpinner
import com.castprogramms.balamutbatut.tools.Element
import com.castprogramms.balamutbatut.tools.Utils.isDarkThemeOn
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel
import java.util.*
import kotlin.concurrent.timerTask

class EditElementFragment : Fragment(R.layout.fragment_edit_element) {

    val viewModel: EditElementViewModel by viewModel()
    var element = Element()
    var titleElement = ""
    private var uri = Uri.parse("")
    var isError = false
    var level = ""
    var position = 0
    lateinit var binding: FragmentEditElementBinding
    private var isDescAndLevelChange = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        element = Element(requireArguments().getString("element", "Пусто"))
        titleElement = requireArguments().getString("title", "")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentEditElementBinding.bind(view)
        requireActivity().title = element.name
        binding.nameElement.isEnabled = false
        if (binding.root.context.isDarkThemeOn())
            binding.addData.setBackgroundResource(R.drawable.background_edit_element_button_dark)
        else
            binding.addData.setBackgroundResource(R.drawable.background_edit_element_button)

        viewModel.downloadDescAndLevel(titleElement, element.name)
        viewModel.checkHaveVideo(titleElement, element.name)
        addSpinnerAdapter(binding)
        binding.nameElement.text = Editable.Factory().newEditable(element.name)
        addTextWatcher(binding)
        binding.loadVideo.setOnClickListener {
            binding.loadVideo.error = null
            val intent = Intent(
                Intent.ACTION_PICK
            )
            intent.type = "video/"
            intent.putExtra("return-data", true)
            startActivityForResult(intent, 1)
        }
        binding.addData.setOnClickListener {
            if (validate(binding)) {
                observeResults(binding)
            }
        }

        viewModel.liveDataHaveVideo.observe(viewLifecycleOwner, {
            when(it){
                is Resource.Error ->{
                    setNoVideoInfo()
                    binding.info.visibility = View.VISIBLE
                }
                is Resource.Loading -> {}
                is Resource.Success -> {
                    if (it.data != null){
                        if (it.data) {
                            setYesVideoInfo()
                            binding.info.visibility = View.VISIBLE
                        }
                        else{
                            setNoVideoInfo()
                            binding.info.visibility = View.VISIBLE
                        }
                    }
                }
            }
        })
    }

    private fun addSpinnerAdapter(binding: FragmentEditElementBinding) {
        val adapter = CustomAdapterSpinner(requireContext(), R.layout.spinner_textview)
        val levels = resources.getStringArray(R.array.levels).toList()
        val hintLevel = "Сложность"

        adapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item)
        adapter.addAll(levels)
        adapter.add(hintLevel)
        binding.spinnerLevel.adapter = adapter
        binding.spinnerLevel.setSelection(adapter.count, true)
        binding.spinnerLevel.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                if (binding.spinnerLevel.selectedItem != hintLevel)
                    level = levels[position]
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }

        viewModel.lifeDataDescAndLevel.observe(viewLifecycleOwner, {
            when(it){
                is Resource.Error ->
                    Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_SHORT).show()
                is Resource.Loading -> {}
                is Resource.Success -> {
                    if (it.data != null){
                        if (it.data.first != "null")
                            binding.descElement.text = Editable.Factory().newEditable(it.data.first)
                        if (levels.contains(it.data.second))
                            binding.spinnerLevel.setSelection(levels.indexOf(it.data.second), true)
                    }
                }
            }
        })
    }

    private fun observeResults(binding: FragmentEditElementBinding) {
        val name = binding.nameElement.text.toString()
        val desc = binding.descElement.text.toString()
        val lifeData = viewModel.loadVideoNameDecs(titleElement, name, uri, desc, level)
        binding.addData.doneLoadingAnimation(
            resources.getColor(R.color.white),
            resources.getDrawable(R.drawable.done).toBitmap()
        )
        lifeData.observe(viewLifecycleOwner, {
            when (it) {
                is Resource.Error -> {
                    createErrorAlertDialog(it.message.toString())
                }
                is Resource.Loading -> {
                    binding.addData.startMorphAnimation()
                }
                is Resource.Success -> {
                    binding.addData.revertAnimation {
                        if (binding.root.context.isDarkThemeOn())
                            binding.addData.setBackgroundResource(R.drawable.background_edit_element_button_dark)
                        else
                            binding.addData.setBackgroundResource(R.drawable.background_edit_element_button)

                        binding.addData.text = "Успех"
                        binding.addData.isPressed = true
                        binding.addData.isClickable = false
                        setTimerToExit()
                    }
                }
            }
        })
    }

    private fun setTimerToExit() {
        Timer().schedule(timerTask { binding.descElement.post { findNavController().popBackStack()} }, 1000)
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
        binding.nameElement.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                binding.nameElementContainer.helperText = null
            }
        })
    }

    private fun validate(binding: FragmentEditElementBinding): Boolean {
        return if (binding.nameElement.text.isNullOrBlank()) {
            binding.nameElementContainer.helperText = resources.getString(R.string.error_name)
            false
        } else {
            true
        }
    }

    private fun setNoVideoInfo(){
        binding.done.setImageResource(R.drawable.no_video)
        binding.done.imageTintList = ColorStateList.valueOf(Color.RED)
        binding.textInfo.text = "Нет видео"
    }

    private fun setYesVideoInfo(){
        binding.done.setImageResource(R.drawable.done)
        binding.done.imageTintList = ColorStateList.valueOf(Color.GREEN)
        binding.textInfo.text = "Видео уже загружено"
    }

    private fun setLoadVideoInfo(){
        binding.done.setImageResource(R.drawable.done)
        binding.done.imageTintList = ColorStateList.valueOf(Color.GREEN)
        binding.textInfo.text = "Видео загружено"
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, dataImg: Intent?) {
        super.onActivityResult(requestCode, resultCode, dataImg)
        if (requestCode == 1 && dataImg != null) {
            val uri: Uri? = dataImg.data
            if (uri != null) {
                this.uri = uri
                setLoadVideoInfo()
                binding.info.visibility = View.VISIBLE
            }
        }
    }
}