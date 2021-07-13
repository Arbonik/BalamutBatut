package com.castprogramms.balamutbatut.ui.group.adapters

import android.app.ActivityOptions
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.fragment.app.Fragment
import androidx.interpolator.view.animation.FastOutSlowInInterpolator
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ItemStudentBinding
import com.castprogramms.balamutbatut.network.Repository
import com.castprogramms.balamutbatut.tools.ItemTouchHelperAdapter
import com.castprogramms.balamutbatut.users.Student
import com.google.firebase.firestore.Query

class StudentsAdapter(_query: Query, private val repository: Repository, var idGroup: String, val fragment: Fragment) :
    RecyclerView.Adapter<StudentsAdapter.StudentsViewHolder>(), ItemTouchHelperAdapter {
    var students = mutableListOf<Student>()
    var studentsID = mutableListOf<String>()

    var query = _query
        set(value) {
            notifyDataSetChanged()
            field = value
        }

    init {
        query.addSnapshotListener { value, error ->
            update()
            if (value != null) {
                students = value.toObjects(Student::class.java)
                value.documents.forEach {
                    studentsID.add(it.id)
                }
                notifyDataSetChanged()
            }
        }
    }

    fun update() {
        students.clear()
        studentsID.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StudentsViewHolder {
        return StudentsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_student, parent, false)
        )
    }

    override fun onBindViewHolder(holder: StudentsViewHolder, position: Int) {
        holder.bind(students[position], studentsID[position], position)
    }

    override fun getItemCount(): Int = students.size

    inner class StudentsViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemStudentBinding.bind(view)
        fun bind(student: Student, id: String, position: Int){
            binding.studentName.text = student.first_name + " " + student.second_name
            binding.position.text = (position + 1).toString()
            Glide.with(itemView)
                .load(student.img)
                .addListener(object : RequestListener<Drawable> {
                    override fun onLoadFailed(
                        e: GlideException?,
                        model: Any?,
                        target: Target<Drawable>?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressRatingPhotoItem.visibility = View.GONE
                        binding.iconStudent.setImageDrawable(itemView.context.getDrawable(R.drawable.male_user))
                        return true
                    }

                    override fun onResourceReady(
                        resource: Drawable?,
                        model: Any?,
                        target: Target<Drawable>?,
                        dataSource: DataSource?,
                        isFirstResource: Boolean
                    ): Boolean {
                        binding.progressRatingPhotoItem.visibility = View.GONE
                        val size = binding.dataUser.height * 0.56
                        val bitmap = resource?.toBitmap(size.toInt(), size.toInt())
                        val resizeBitmap = bitmap
                        binding.iconStudent.setImageDrawable(resizeBitmap?.toDrawable(itemView.resources))
                        return true
                    }
                })
                .into(binding.iconStudent)
            binding.groupElements.adapter = ElementsStudentAdapter()
            binding.groupElements.layoutManager = LinearLayoutManager(itemView.context)
            binding.root.setOnClickListener {
                if (binding.expandableView.visibility == View.GONE) {
                    binding.expandableView.visibility = View.VISIBLE
                    binding.dataUser.background = itemView.context.getDrawable(R.drawable.background_group_student)
                    val anim = AnimationUtils.loadAnimation(itemView.context, R.anim.show)
                    anim.interpolator = FastOutSlowInInterpolator()
                    binding.expandableView.startAnimation(anim)
                    anim.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation?) {
                            binding.expandableView.visibility = View.VISIBLE
                        }

                        override fun onAnimationEnd(animation: Animation?) {}

                        override fun onAnimationRepeat(animation: Animation?) {}
                    })
                }
                else {
                    val anim =
                        AnimationUtils.loadAnimation(itemView.context, R.anim.check_item_anim)
                    anim.interpolator = FastOutSlowInInterpolator()
                    binding.expandableView.startAnimation(anim)
                    anim.setAnimationListener(object : Animation.AnimationListener {
                        override fun onAnimationStart(animation: Animation?) {
                            binding.expandableView.visibility = View.VISIBLE
                        }

                        override fun onAnimationEnd(animation: Animation?) {
                            binding.expandableView.visibility = View.GONE
                            binding.dataUser.background = itemView.context.getDrawable(R.drawable.rating_rectangle)
                        }

                        override fun onAnimationRepeat(animation: Animation?) {
                        }
                    })
                }
            }
        }
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        if (fromPosition < toPosition) {
            for (i in fromPosition until toPosition) {
                students = replace(students, i, i+1)
                studentsID = replaceID(studentsID, i, i +1)
            }
        }
        else{
            for (i in toPosition downTo fromPosition step 1){
                students = replace(students, i, i+1)
                studentsID = replaceID(studentsID, i, i +1)
            }
        }
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int) {
        repository.deleteStudentFromGroup(studentsID[position], idGroup)
        students.removeAt(position)
        studentsID.removeAt(position)
        notifyItemRemoved(position)
    }
    fun replace(list: MutableList<Student>, fromPosition: Int, toPosition: Int):MutableList<Student>{
        val firstPair = list[fromPosition]
        val secondPair = list[toPosition]
        list[fromPosition] = secondPair
        list[toPosition] = firstPair
        return list
    }

    fun replaceID(list: MutableList<String>, fromPosition: Int, toPosition: Int):MutableList<String>{
        val firstPair = list[fromPosition]
        val secondPair = list[toPosition]
        list[fromPosition] = secondPair
        list[toPosition] = firstPair
        return list
    }
}