package com.castprogramms.balamutbatut.ui.rating

import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.findViewTreeLifecycleOwner
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
import com.castprogramms.balamutbatut.R
import com.castprogramms.balamutbatut.databinding.RecyclerItemRatingBinding
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.users.Student
import com.google.firebase.firestore.Query
import com.squareup.picasso.Picasso

class RatingAdapter(val getRang: (it: String) -> MutableLiveData<Resource<String>>) :
    RecyclerView.Adapter<RatingAdapter.RatingViewHolder>() {

    var students = mutableListOf<Pair<String, Student>>()
        set(value) {
            field = value
            filterStudent()
            notifyDataSetChanged()
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatingViewHolder {
        return RatingViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.recycler_item_rating, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RatingViewHolder, position: Int) {
        holder.onBind(students[position], position)
    }

    override fun getItemCount() = students.size

    inner class RatingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val binding = RecyclerItemRatingBinding.bind(view)
        fun onBind(pair: Pair<String, Student>, position: Int) {
            setBackground(binding, position)
            binding.progressRatingPhotoItem.visibility = View.VISIBLE
            Glide.with(itemView)
                .load(pair.second.img)
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
                        val size = binding.root.height * 0.56
                        val bitmap = resource?.toBitmap(size.toInt(), size.toInt())
                        val resizeBitmap = bitmap
                        binding.iconStudent.setImageDrawable(resizeBitmap?.toDrawable(itemView.resources))
                        return true
                    }
                })
                .into(binding.iconStudent)
            binding.studentName.text = pair.second.getFullName()
            binding.score.text = itemView.context.resources.getString(R.string.quantityElements) + " " + getAllSize(pair.second).toString()
            binding.position.text = (position + 1).toString()
            getRang(pair.first).observeForever {
                when (it) {
                    is Resource.Error -> {
                    }
                    is Resource.Loading -> {
                    }
                    is Resource.Success -> {
//                        binding.rang.text = itemView.context.resources.getString(R.string.rang) + " " + it.data
                    }
                }
            }
        }
    }

    private fun filterStudent() {
        students.sortBy { getAllSize(it.second) }
        students.reverse()
    }

    private fun getAllSize(student: Student): Int {
        var size = 0
        student.element.forEach {
            size += it.value.size
        }
        return size
    }

    private fun setBackground(binding: RecyclerItemRatingBinding, position: Int) {
        when (position) {
            0 -> {
            }
            1 -> {
                binding.root.background =
                    binding.root.context.resources.getDrawable(R.drawable.rating_rectangle_silver)
                binding.cron.setImageDrawable(binding.root.context.resources.getDrawable(R.drawable.cron_silver))
            }
            2 -> {
                binding.root.background =
                    binding.root.context.resources.getDrawable(R.drawable.rating_rectangle_bronse)
                binding.cron.setImageDrawable(binding.root.context.resources.getDrawable(R.drawable.cron_bronse))
            }
            else -> {
                binding.root.background =
                    binding.root.context.resources.getDrawable(R.drawable.rating_rectangle)
                binding.cron.visibility = View.GONE
            }
        }
    }
}