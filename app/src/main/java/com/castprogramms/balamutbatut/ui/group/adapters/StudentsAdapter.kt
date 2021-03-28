package com.castprogramms.balamutbatut.ui.group.adapters

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.navigation.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.Group
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.users.Student
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.Gson
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class StudentsAdapter(_query: Query) :
    RecyclerView.Adapter<StudentsAdapter.StudentsViewHolder>() {
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
                .inflate(R.layout.student_adapter, parent, false)
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
                val bundle = Bundle()
                bundle.putString("id", id)
                it.findNavController()
                    .navigate(R.id.action_studentsFragment_to_infoStudentFragment, bundle)
            }
        }
    }
}