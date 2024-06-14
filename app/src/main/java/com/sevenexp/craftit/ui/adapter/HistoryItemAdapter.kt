package com.sevenexp.craftit.ui.adapter

import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.sevenexp.craftit.R
import com.sevenexp.craftit.data.source.database.entity.HistoryEntity
import com.sevenexp.craftit.databinding.ItemContinueBinding

class HistoryItemAdapter : RecyclerView.Adapter<HistoryItemAdapter.HistoryViewHolder>() {
    private val listPost = ArrayList<HistoryEntity>()

    override fun getItemCount(): Int = listPost.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding =
            ItemContinueBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return HistoryViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        holder.bind(listPost[position])
        holder.itemView.setOnClickListener {
//            TODO: Implement intent to detail activity
        }
    }


    fun setData(newItems: List<HistoryEntity>) {
        val oldList = ArrayList(listPost)
        val diffResult = DiffUtil.calculateDiff(DiffUtilCallback(oldList, newItems))
        listPost.clear()
        listPost.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class HistoryViewHolder(itemView: View) : ViewHolder(itemView) {
        private val binding = ItemContinueBinding.bind(itemView)

        fun bind(post: HistoryEntity) {
            binding.apply {
                val spannable = SpannableString("${post.currentStep}/${post.totalStep}")
                spannable.setSpan(
                    ForegroundColorSpan(binding.root.context.getColor(R.color.primary)),
                    0,
                    spannable.indexOf("/"),
                    SpannableString.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                setImage(post.image ?: "", ivThumbnail)
                postTitle.text = post.name
                stepCount.text = spannable
            }
        }

        private fun setImage(imageUrl: String, imageView: ImageView, roundness: Int = 16) {
            val shimmerDrawable = ShimmerDrawable()
            val shimmer = Shimmer.AlphaHighlightBuilder()
                .setBaseAlpha(0.8f)
                .setHighlightAlpha(0.9f)
                .setAutoStart(true)
                .setTilt(32f)
                .build()

            shimmerDrawable.setShimmer(shimmer)
            if (imageUrl.isNotEmpty()) {
                try {
                    Glide.with(binding.root)
                        .load(imageUrl)
                        .placeholder(shimmerDrawable)
                        .error(shimmerDrawable)
                        .into(imageView)
                } catch (e: Exception) {
                    Log.d("Craftit item adapter", "Failed set image for $imageUrl")
                }
            } else {
                imageView.setImageDrawable(shimmerDrawable)
            }
        }
    }

    class DiffUtilCallback(
        private val oldList: List<HistoryEntity>,
        private val newList: List<HistoryEntity>
    ) :
        DiffUtil.Callback() {

        override fun getOldListSize(): Int = oldList.size
        override fun getNewListSize(): Int = newList.size

        override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem.javaClass == newItem.javaClass
        }

        override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
            val oldItem = oldList[oldItemPosition]
            val newItem = newList[newItemPosition]

            return oldItem == newItem
        }
    }
}