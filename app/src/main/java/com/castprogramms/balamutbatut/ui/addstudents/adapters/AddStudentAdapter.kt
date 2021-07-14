package com.castprogramms.balamutbatut.ui.addstudents.adapters

import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.ItemStudentBinding
import com.castprogramms.balamutbatut.network.Repository
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.users.Student
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot

class AddStudentAdapter(_query: Query, var idGroup: String, private val repository: Repository)
    : RecyclerView.Adapter<AddStudentAdapter.AddStudentsViewHolder>(), Filterable {
    var students = mutableListOf<Student>()
    var studentsID = mutableListOf<String>()
    var sortedStudent = mutableListOf<Student>()
    var sortedStudentsID = mutableListOf<String>()
    var filterWord = ""
    var query = _query
        set(value) {
            notifyDataSetChanged()
            field = value
        }

    init {
        query.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                update()
                if (value != null) {
                    students = value.toObjects(Student::class.java)
                    value.documents.forEach {
                        studentsID.add(it.id)
                    }
                    sortedStudent = students
                    sortedStudentsID = studentsID
                    filter.filter(filterWord)
                    notifyDataSetChanged()
                }
            }
        })
    }
    fun update(){
        students.clear()
        studentsID.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddStudentsViewHolder {
        return AddStudentsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.item_student, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AddStudentsViewHolder, position: Int) {
        holder.bind(sortedStudent[position], sortedStudentsID[position])
    }

    override fun getItemCount(): Int = sortedStudent.size

    inner class AddStudentsViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = ItemStudentBinding.bind(view)
        fun bind(student: Student, id: String){
//            binding.studentName.text = student.first_name + " " + student.second_name
//            Glide.with(itemView)
//                .load(student.img)
//                .addListener(object : RequestListener<Drawable> {
//                    override fun onLoadFailed(
//                        e: GlideException?,
//                        model: Any?,
//                        target: Target<Drawable>?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        binding.progressRatingPhotoItem.visibility = View.GONE
//                        binding.iconStudent.setImageDrawable(itemView.context.getDrawable(R.drawable.male_user))
//                        return true
//                    }
//
//                    override fun onResourceReady(
//                        resource: Drawable?,
//                        model: Any?,
//                        target: Target<Drawable>?,
//                        dataSource: DataSource?,
//                        isFirstResource: Boolean
//                    ): Boolean {
//                        binding.progressRatingPhotoItem.visibility = View.GONE
//                        val size = binding.root.height * 0.56
//                        val bitmap = resource?.toBitmap(size.toInt(), size.toInt())
//                        binding.iconStudent.setImageDrawable(bitmap?.toDrawable(itemView.resources))
//                        return true
//                    }
//                })
//                .into(binding.iconStudent)
//            binding.root.setOnClickListener {
//                if (User.trainer != null)
//                    student.groupID = idGroup
//                repository.updateStudentGroup(id, student.groupID)
//            }
        }
    }

    override fun getFilter(): Filter {
        return object : Filter(){
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                filterWord = constraint.toString()
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