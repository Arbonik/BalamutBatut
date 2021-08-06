package com.castprogramms.balamutbatut.ui.addstudents.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ItemAddStudentBinding
import com.castprogramms.balamutbatut.databinding.ItemStudentBinding
import com.castprogramms.balamutbatut.network.Repository
import com.castprogramms.balamutbatut.tools.Element
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.tools.Utils.isDarkThemeOn
import com.castprogramms.balamutbatut.users.Student
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class AddStudentAdapter(var idGroup: String)
    : RecyclerView.Adapter<AddStudentAdapter.AddStudentsViewHolder>(), Filterable {
    private var students = mutableListOf<Student>()
    private var studentsID = mutableListOf<String>()
    private var sortedStudent = mutableListOf<Student>()
    private var sortedStudentsID = mutableListOf<String>()
    fun setData(mutableList: MutableList<Pair<String, Student>>){
        students = mutableListOf()
        studentsID = mutableListOf()
        mutableList.forEach {
            studentsID.add(it.first)
            students.add(it.second)
        }
        sortedStudent = students
        sortedStudentsID = studentsID
        notifyDataSetChanged()
    }

    val liveDataSelectedStudent = MutableLiveData<MutableList<String>>(mutableListOf())
    fun addNeedStudent(id: String){
        if (liveDataSelectedStudent.value.isNullOrEmpty())
            liveDataSelectedStudent.postValue(mutableListOf(id))
        else
            liveDataSelectedStudent.postValue(liveDataSelectedStudent.value.apply {
                this?.add(id)
            })
    }
    fun deleteNeedStudent(id: String){
        if (liveDataSelectedStudent.value?.contains(id) == true)
            liveDataSelectedStudent.postValue(liveDataSelectedStudent.value.apply {
                this?.remove(id)
            })
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddStudentsViewHolder {
        return AddStudentsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_add_student, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AddStudentsViewHolder, position: Int) {
        holder.bind(sortedStudent[position], sortedStudentsID[position])
    }

    override fun getItemCount(): Int = sortedStudent.size

    inner class AddStudentsViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemAddStudentBinding.bind(view)
        fun bind(student: Student, id: String){
            setBackground(binding)
            binding.score.text = countElements(student)
            binding.studentName.text = student.getFullName()
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
                        binding.iconStudent.setBackgroundResource(R.drawable.male_user)
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
                        binding.iconStudent.setImageDrawable(resource)
                        return true
                    }
                })
                .into(binding.iconStudent)
            binding.root.setOnClickListener {
                binding.isNeed.setChecked(!binding.isNeed.isChecked, true)
                checkCheckBox(id, binding.isNeed.isChecked)
            }
            binding.isNeed.setOnClickListener {
                binding.isNeed.setChecked(!binding.isNeed.isChecked, true)
                checkCheckBox(id, binding.isNeed.isChecked)
            }
        }

        private fun checkCheckBox(id: String, checked: Boolean) {
            if (checked)
                addNeedStudent(id)
            else
                deleteNeedStudent(id)
        }

        private fun countElements(student: Student): String {
            var score = 0
            student.element.forEach {
                score += it.value.size
            }
            return "Элементы: $score"
        }

        private fun setBackground(binding: ItemAddStudentBinding) {
            if (binding.root.context.isDarkThemeOn())
                binding.root.setBackgroundResource(R.drawable.rating_rectangle_black)
            else
                binding.root.setBackgroundResource(R.drawable.rating_rectangle)
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                sortedStudent = mutableListOf()
                sortedStudentsID = mutableListOf()
                val names = mutableListOf<String>()
                val isContains = mutableListOf<Boolean>()
                students.forEach {
                    names.add(it.getFullName())
                }
                names.forEach {
                    if (it.contains(constraint.toString(), true))
                        isContains.add(true)
                    else
                        isContains.add(false)
                }
                for (i in students.indices)
                    if (isContains[i]) {
                        sortedStudent.add(students[i])
                        sortedStudentsID.add(studentsID[i])
                    }
                return FilterResults().apply { values = sortedStudent }
            }

            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                notifyDataSetChanged()
            }
        }
    }
}