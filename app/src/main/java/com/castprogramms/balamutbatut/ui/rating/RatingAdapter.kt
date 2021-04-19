package com.castprogramms.balamutbatut.ui.rating

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.RecyclerItemRatingBinding
import com.castprogramms.balamutbatut.users.Student
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso

class RatingAdapter(_query: Query): RecyclerView.Adapter<RatingAdapter.RatingViewHolder>() {

    val students = mutableListOf<Student>()

    var query = _query
    set(value) {
        field = value
        notifyDataSetChanged()
    }
    init {
        query.addSnapshotListener { value, error ->
            update()
            if (value != null){
                students.addAll(value.toObjects(Student::class.java))
                filterStudent()
                notifyDataSetChanged()
            }
            else
                Log.e("fire", error?.message.toString())
        }
    }

    private fun update() {
        students.clear()
        notifyDataSetChanged()
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        return RatingViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.recycler_item_rating, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        holder.onBind(students[position])
    }

    override fun getItemCount() = students.size

    inner class RatingViewHolder(view: View): RecyclerView.ViewHolder(view){
        val binding = RecyclerItemRatingBinding.bind(view)
        fun onBind(student: Student) {
            if (student.img != "" && student.img != "null")
                Picasso.get()
                    .load(student.img)
                    .into(binding.imageRating)
            binding.textRating.text = student.getFullName()
            binding.score.text = itemView.context.resources.getString(R.string.quantityElements)+getAllSize(student).toString()
        }
    }
    private fun filterStudent(){
        students.sortBy {getAllSize(it)}
        students.reverse()
    }

    private fun getAllSize(student: Student): Int {
        var size = 0
        student.element.forEach {
            size += it.value.size
        }
        return size
    }
}