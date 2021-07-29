package com.castprogramms.balamutbatut.ui.rating

import android.graphics.drawable.Drawable
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.Transformation
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
import com.castprogramms.balamutbatut.databinding.RecyclerItemRatingBinding
import com.castprogramms.balamutbatut.network.Resource
import com.castprogramms.balamutbatut.tools.Element
import com.castprogramms.balamutbatut.tools.User
import com.castprogramms.balamutbatut.tools.User.id
import com.castprogramms.balamutbatut.tools.Utils.isDarkThemeOn
import com.castprogramms.balamutbatut.ui.group.adapters.ElementsStudentAdapter
import com.castprogramms.balamutbatut.users.Student

class RatingAdapter(
    val getRang: (it: String) -> MutableLiveData<Resource<String>>,
    val getSortedElements: () -> MutableLiveData<MutableList<Pair<String, List<Element>>>>,
    val getStudentElements: (String) -> MutableLiveData<Resource<MutableList<Pair<String, List<Int>>>>>
) : RecyclerView.Adapter<RatingAdapter.RatingViewHolder>() {

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
                        binding.iconStudent.setImageDrawable(resource)
                        return true
                    }
                })
                .into(binding.iconStudent)
            binding.studentName.text =
                if (pair.first != User.id) pair.second.getFullName()
                else pair.second.getFullName() + " (Ты)"
            binding.score.text = itemView.context.resources.getString(R.string.quantityElements) + " " + getAllSize(pair.second).toString()
            binding.position.text = (position + 1).toString()

            val adapter = ElementsStudentAdapter()
            binding.groupElements.shimmerLayoutManager = LinearLayoutManager(itemView.context)
            binding.groupElements.adapter = adapter
            setDataAdapter(adapter, pair.first)

            binding.root.setOnClickListener {
                if (binding.expandableView.visibility == View.GONE) {
                    expand(binding.expandableView)
                    setExpandBackground(binding, position)
                }
                else{
                    collapse(binding.expandableView, position)
                    setCollapseBackground(binding, position)
                }
            }
            binding.seeElement.setOnClickListener {
                val bundle = Bundle()
                bundle.putString("id", id)
                itemView.findNavController().navigate(R.id.action_rating_Fragment_to_showElementsFragment, bundle)
            }
        }

        private fun setDataAdapter(adapter: ElementsStudentAdapter, studentId: String){
            binding.groupElements.showShimmer()

            getSortedElements().observeForever {
                if (it != null){
                    adapter.allElements = it
                    if (adapter.userElements.isNotEmpty())
                        binding.groupElements.hideShimmer()
                }
            }
            getStudentElements(studentId).observeForever{
                when(it){
                    is Resource.Error ->{}
                    is Resource.Loading -> {}
                    is Resource.Success -> {
                        if (it.data != null)
                            adapter.userElements = it.data
                        if (adapter.allElements.isNotEmpty())
                            binding.groupElements.hideShimmer()
                    }
                }
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
            a.setAnimationListener(object : Animation.AnimationListener{
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

        private fun collapse(v: View, position: Int) {
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
            a.setAnimationListener(object : Animation.AnimationListener{
                override fun onAnimationStart(animation: Animation?) {

                }

                override fun onAnimationEnd(animation: Animation?) {
                    v.visibility = View.GONE
                }

                override fun onAnimationRepeat(animation: Animation?) {
                }
            })

            // Collapse speed of 1dp/ms
            a.duration = (initialHeight / v.context.resources.displayMetrics.density).toLong()
            v.startAnimation(a)
        }
    }

    private fun filterStudent() {
        students.sortByDescending { getAllSize(it.second) }
    }

    private fun getAllSize(student: Student): Int {
        var size = 0
        student.element.forEach {
            size += it.value.size
        }
        return size
    }


    private fun setBackground(binding: RecyclerItemRatingBinding, position: Int) {
        if (binding.root.context.isDarkThemeOn()) {
            when (position) {
                0 -> binding.cron.setImageResource(R.drawable.cron_gold)
                1 -> binding.cron.setImageResource(R.drawable.cron_silver)
                2 -> binding.cron.setImageResource(R.drawable.cron_bronse)
                else -> binding.cron.visibility = View.GONE
            }
            binding.root.setBackgroundResource(R.drawable.rating_rectangle_black)
        }
        else {
            when (position) {
                0 -> {
                    binding.root.setBackgroundResource(R.drawable.rating_rectangle_gold)
                    binding.cron.setImageResource(R.drawable.cron_gold)
                }
                1 -> {
                    binding.root.setBackgroundResource(R.drawable.rating_rectangle_silver)
                    binding.cron.setImageResource(R.drawable.cron_silver)
                }
                2 -> {
                    binding.root.setBackgroundResource(R.drawable.rating_rectangle_bronse)
                    binding.cron.setImageResource(R.drawable.cron_bronse)
                }
                else -> {
                    binding.root.setBackgroundResource(R.drawable.rating_rectangle)
                    binding.cron.visibility = View.GONE
                }
            }
        }
    }

    private fun setExpandBackground(binding: RecyclerItemRatingBinding, position: Int) {
        if (binding.root.context.isDarkThemeOn()) {
            binding.root.setBackgroundResource(R.drawable.rating_rectangle_black_expand)
        }
        else {
            when (position) {
                0 -> {
                    binding.root.setBackgroundResource(R.drawable.rating_rectangle_gold_expand)
                }
                1 -> {
                    binding.root.setBackgroundResource(R.drawable.rating_rectangle_silver_expand)
                }
                2 -> {
                    binding.root.setBackgroundResource(R.drawable.rating_rectangle_bronse_expand)
                }
                else -> {
                    binding.root.setBackgroundResource(R.drawable.rating_rectangle_expand)
                }
            }
        }
    }
    private fun setCollapseBackground(binding: RecyclerItemRatingBinding, position: Int) {
        if (binding.root.context.isDarkThemeOn()) {
            binding.root.setBackgroundResource(R.drawable.rating_rectangle_black)
        }
        else {
            when (position) {
                0 -> {
                    binding.root.setBackgroundResource(R.drawable.rating_rectangle_gold)
                }
                1 -> {
                    binding.root.setBackgroundResource(R.drawable.rating_rectangle_silver)
                }
                2 -> {
                    binding.root.setBackgroundResource(R.drawable.rating_rectangle_bronse)
                }
                else -> {
                    binding.root.setBackgroundResource(R.drawable.rating_rectangle)
                }
            }
        }
    }
}