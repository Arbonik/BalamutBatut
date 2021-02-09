package com.castprogramms.balamutbatut.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ProgressBar
import android.widget.TextView
import com.castprogramms.balamutbatut.R

class SkillFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_skill, container, false)
        val progressBarProg : ProgressBar = view.findViewById(R.id.progress_prog)
        val textViewProg : TextView = view.findViewById(R.id.textProg)
        val progressBarModel : ProgressBar = view.findViewById(R.id.progress_model)
        val textViewModel : TextView = view.findViewById(R.id.textModel)
        val progressBarKonstr : ProgressBar = view.findViewById(R.id.progress_konstr)
        val textViewKonstr : TextView = view.findViewById(R.id.textKonstr)

//        progressBarProg.setProgress(User.prog, true)
  //      progressBarProg.progressDrawable.setColorFilter(Color.GREEN, android.graphics.PorterDuff.Mode.SRC_IN)
//        progressBarModel.setProgress(User.model, true)
//        progressBarModel.progressDrawable.setColorFilter(Color.RED, android.graphics.PorterDuff.Mode.SRC_IN)
  //      progressBarKonstr.setProgress(User.konstr, true)
    //    progressBarKonstr.progressDrawable.setColorFilter(Color.BLUE, android.graphics.PorterDuff.Mode.SRC_IN)

        textViewProg.text = textViewProg.text.toString() +  progressBarProg.progress.toString()
        textViewModel.text = textViewModel.text.toString() + progressBarModel.progress.toString()
        textViewKonstr.text = textViewKonstr.text.toString() + progressBarKonstr.progress.toString()
        return view
    }
}