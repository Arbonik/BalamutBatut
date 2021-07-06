package com.castprogramms.balamutbatut.ui.addstudents.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
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
                    student.groupID = idGroup
                repository.updateStudentGroup(id, student.groupID)

            }
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