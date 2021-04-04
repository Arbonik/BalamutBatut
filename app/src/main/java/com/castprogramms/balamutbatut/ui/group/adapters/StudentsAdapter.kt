package com.castprogramms.balamutbatut.ui.group.adapters

import android.app.ActivityOptions
import android.os.Bundle
import android.transition.TransitionInflater
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.Navigator
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.Repository
import com.castprogramms.balamutbatut.tools.ItemTouchHelperAdapter
import com.castprogramms.balamutbatut.users.Student
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

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
        holder.bind(students[position], studentsID[position])
    }

    override fun getItemCount(): Int = students.size

    inner class StudentsViewHolder(view: View): RecyclerView.ViewHolder(view){
        val cardViewStudent : CardView = view.findViewById(R.id.card_view_student)
        val studentNameTextView : TextView = view.findViewById(R.id.student_name)
        val studentDateTextView : TextView = view.findViewById(R.id.student_date)
        val studentSexTextView : TextView = view.findViewById(R.id.student_sex)
        val studentImage : CircleImageView = view.findViewById(R.id.icon_student)
        fun bind(student: Student, id: String){
            studentNameTextView.text = student.first_name + " " + student.second_name
            studentDateTextView.text = student.date
            studentSexTextView.text = student.sex
            if (student.img != "" && student.img != "null") {
                Picasso.get()
                    .load(student.img)
                    .into(studentImage)
            }
            cardViewStudent.setOnClickListener {
                val sharedView = studentImage
                val transName = itemView.context.resources.getString(R.string.icon)
                val options = ActivityOptions.makeSceneTransitionAnimation(fragment.requireActivity(), sharedView, transName)
                val changeBounds = TransitionInflater.from(fragment.requireContext()).inflateTransition(R.transition.changebounds_with_arcmotion)
                val bundle = Bundle()
                bundle.putBundle("anim", options.toBundle())
                bundle.putString("id", id)
                it.findNavController()
                    .navigate(R.id.action_studentsFragment_to_infoStudentFragment, bundle)
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