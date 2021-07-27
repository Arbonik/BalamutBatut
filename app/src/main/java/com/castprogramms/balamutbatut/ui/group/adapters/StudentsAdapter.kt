package com.castprogramms.balamutbatut.ui.group.adapters

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.MutableLiveData
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
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.Element
import com.castprogramms.balamutbatut.tools.ItemTouchHelperAdapter
import com.castprogramms.balamutbatut.users.Student

class StudentsAdapter(
    var idGroup: String,
    val deleteStudent: (String, String) -> Unit,
    val getSortedElements: () -> MutableLiveData<MutableList<Pair<String, List<Element>>>>,
    val getStudentElements: (String) -> MutableLiveData<Resource<MutableList<Pair<String, List<Int>>>>>
) :
    RecyclerView.Adapter<StudentsAdapter.StudentsViewHolder>(), ItemTouchHelperAdapter {
    var students = mutableListOf<Student>()
    var studentsID = mutableListOf<String>()

    fun setData(data: MutableList<Pair<String, Student>>){
        val curStudents = mutableListOf<Student>()
        val curStudentsID = mutableListOf<String>()
        data.sortByDescending {
            it.second.getQuantityElements().split(" ")[1].toInt()
        }

        data.forEach {
            curStudents.add(it.second)
            curStudentsID.add(it.first)
        }
        students = curStudents
        studentsID = curStudentsID
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
            binding.score.text = countElements(student)
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
                        binding.iconStudent.setImageResource(R.drawable.male_user)
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
                        binding.iconStudent.setImageDrawable(bitmap?.toDrawable(itemView.resources))
                        return true
                    }
                })
                .into(binding.iconStudent)
            val adapter = ElementsStudentAdapter()
            setDataAdapter(adapter, id)
            binding.groupElements.adapter = adapter
            binding.groupElements.layoutManager = LinearLayoutManager(itemView.context)
            binding.addElement.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("id", id)
                itemView.findNavController()
                    .navigate(R.id.action_studentsFragment_to_groupElementsFragment, bundle)
            }

            binding.root.setOnClickListener {
                if (binding.expandableView.visibility == View.GONE) {
                    expand(binding.expandableView)
                    binding.dataUser.setBackgroundResource(R.drawable.background_group_student)
                }
                else
                    collapse(binding.expandableView)
            }
        }

        private fun expand(v: View) {
            val matchParentMeasureSpec: Int = View.MeasureSpec.makeMeasureSpec(
                (v.parent as View).width,
                View.MeasureSpec.EXACTLY
            )
            val wrapContentMeasureSpec: Int =
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            v.measure(matchParentMeasureSpec, wrapContentMeasureSpec)
            val targetHeight: Int = v.measuredHeight

            val a: Animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    v.layoutParams.height =
                        if (interpolatedTime == 1f) ViewGroup.LayoutParams.WRAP_CONTENT
                        else (targetHeight * interpolatedTime).toInt()
                    v.requestLayout()
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }
            a.setAnimationListener(object :Animation.AnimationListener{
                override fun onAnimationStart(animation: Animation?) {
                    v.layoutParams.height = 1
                    v.visibility = View.VISIBLE
                }

                override fun onAnimationEnd(animation: Animation?) {
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })

            // Expansion speed of 1dp/ms
            a.duration = (targetHeight / v.context.resources.displayMetrics.density).toLong()
            v.startAnimation(a)
        }

        private fun collapse(v: View) {
            val initialHeight: Int = v.measuredHeight
            val a: Animation = object : Animation() {
                override fun applyTransformation(interpolatedTime: Float, t: Transformation) {
                    if (interpolatedTime == 1f) {
                        v.visibility = View.GONE
                    } else {
                        v.layoutParams.height =
                            initialHeight - (initialHeight * interpolatedTime).toInt()
                        v.requestLayout()
                    }
                }

                override fun willChangeBounds(): Boolean {
                    return true
                }
            }
            a.setAnimationListener(object :Animation.AnimationListener{
                override fun onAnimationStart(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    v.visibility = View.GONE
                    binding.dataUser.setBackgroundResource(R.drawable.rating_rectangle)
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })

            // Collapse speed of 1dp/ms
            a.duration = (initialHeight / v.context.resources.displayMetrics.density).toLong()
            v.startAnimation(a)
        }

        private fun setDataAdapter(adapter: ElementsStudentAdapter, studentId: String){
            getSortedElements().observeForever {
                if (it != null){
                    adapter.allElements = it
                    if (adapter.userElements.isNotEmpty())
                        binding.progressBarElements.progressBar.visibility = View.GONE
                }
            }
            getStudentElements(studentId).observeForever{
                when(it){
                    is Resource.Error ->
                        binding.progressBarElements.progressBar.visibility = View.GONE
                    is Resource.Loading -> {

                    }
                    is Resource.Success -> {
                        if (it.data != null)
                            adapter.userElements = it.data
                        if (adapter.allElements.isNotEmpty())
                            binding.progressBarElements.progressBar.visibility = View.GONE
                    }
                }
            }
        }
    }

    private fun countElements(student: Student): String {
        var score = 0
        student.element.forEach {
            score += it.value.size
        }
        return "Элементы: $score"
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
        deleteStudent(studentsID[position], idGroup)
        students.removeAt(position)
        studentsID.removeAt(position)
        notifyItemRemoved(position)
    }

    private fun replace(list: MutableList<Student>, fromPosition: Int, toPosition: Int):MutableList<Student>{
        val firstPair = list[fromPosition]
        val secondPair = list[toPosition]
        list[fromPosition] = secondPair
        list[toPosition] = firstPair
        return list
    }

    private fun replaceID(list: MutableList<String>, fromPosition: Int, toPosition: Int):MutableList<String>{
        val firstPair = list[fromPosition]
        val secondPair = list[toPosition]
        list[fromPosition] = secondPair
        list[toPosition] = firstPair
        return list
    }
}