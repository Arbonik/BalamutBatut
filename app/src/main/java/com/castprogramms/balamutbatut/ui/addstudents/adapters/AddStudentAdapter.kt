package com.castprogramms.balamutbatut.ui.addstudents.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowId
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.tools.DataUserFirebase
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.users.Student
import com.google.firebase.firestore.EventListener
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.QuerySnapshot
import com.google.gson.Gson

class AddStudentAdapter(_query: Query)
    : RecyclerView.Adapter<AddStudentAdapter.AddStudentsViewHolder>() {
    var students = mutableListOf<Student>()
    var studentsID = mutableListOf<String>()

    var query = _query
        set(value) {
            notifyDataSetChanged()
            field = value
        }

    init {
        query.addSnapshotListener(object : EventListener<QuerySnapshot> {
            override fun onEvent(value: QuerySnapshot?, error: FirebaseFirestoreException?) {
                update()
                value?.documents?.forEach {
                    students.add(
                        Gson().fromJson(it.data.toString(), Student::class.java)
                    )
                    studentsID.add(it.id)
                    notifyDataSetChanged()
                }
            }
        })
    }
    fun update(){
        students.clear()
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AddStudentsViewHolder {
        return AddStudentsViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.student_adapter, parent, false)
        )
    }

    override fun onBindViewHolder(holder: AddStudentsViewHolder, position: Int) {
        holder.bind(students[position], studentsID[position])
    }

    override fun getItemCount(): Int = students.size

    inner class AddStudentsViewHolder(view: View): RecyclerView.ViewHolder(view){
        val cardViewStudent : CardView = view.findViewById(R.id.card_view_student)
        val studentNameTextView : TextView = view.findViewById(R.id.student_name)
        val studentDateTextView : TextView = view.findViewById(R.id.student_date)
        val studentSexTextView : TextView = view.findViewById(R.id.student_sex)
        fun bind(student: Student, id: String){
            studentNameTextView.text = student.first_name + " " + student.second_name
            studentDateTextView.text = student.date
            studentSexTextView.text = student.sex
            cardViewStudent.setOnClickListener {
                if (User.trainer != null)
                    student.nameGroup = User.trainer!!.groupID
                DataUserFirebase().updateStudent(student, id)
            }
        }
    }
}