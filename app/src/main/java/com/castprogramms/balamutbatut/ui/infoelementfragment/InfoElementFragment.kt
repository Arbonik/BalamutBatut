package com.castprogramms.balamutbatut.ui.infoelementfragment

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.FragmentInfoElementBinding
import com.castprogramms.balamutbatut.network.Resource
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.SimpleExoPlayer
import com.google.android.material.snackbar.Snackbar
import org.koin.androidx.viewmodel.ext.android.viewModel

class InfoElementFragment: Fragment(R.layout.fragment_info_element) {
    val viewModel: InfoElementViewModel by viewModel()
    var title = ""
    var name = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        title = requireArguments().getString("title", "")
        name = requireArguments().getString("name", "")
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentInfoElementBinding.bind(view)
        setVideo(binding)
        setDescAndLevel(binding)
    }

    private fun setDescAndLevel(binding: FragmentInfoElementBinding) {
        viewModel.downloadDescAndLevel(title, name)
        viewModel.lifeDataDescAndLevel.observe(viewLifecycleOwner, {
            when(it){
                is Resource.Error ->
                    Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_SHORT).show()
                is Resource.Loading -> {}
                is Resource.Success -> {
                    if (it.data != null){
                        if (it.data.first != "null" && it.data.first != "")
                            binding.desc.text = it.data.first
                        else
                            binding.desc.text = "На данный момент тренеры не добавили описание этому элементу"

                        if (it.data.second != "null" && it.data.second != "")
                            binding.level.text = it.data.second
                        else
                            binding.level.text = "На данный момент тренеры не добавили сложность этому элементу"
                    }
                }
            }
        })
    }

    private fun setVideo(binding: FragmentInfoElementBinding) {
        viewModel.downloadVideo(title, name)
        viewModel.lifeDataVideo.observe(viewLifecycleOwner, {
            when(it){
                is Resource.Error ->{
                    if (it.message != " Object does not exist at location.")
                        Snackbar.make(requireView(), "Тренеры не загрузили видео для данного элемента", Snackbar.LENGTH_SHORT).show()
                    else
                        Snackbar.make(requireView(), it.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
                is Resource.Loading -> {}
                is Resource.Success -> {
                    if (it.data != null){
                        val player = SimpleExoPlayer.Builder(requireContext()).build()
                        binding.video.playerView.player = player
                        binding.video.releasePlayer()
                        binding.video.startPlayer()
                        val media = MediaItem.fromUri(it.data)
                        player.addMediaItem(media)
                        player.prepare()
                    }
                }
            }
        })
    }
}