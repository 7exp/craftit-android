package com.sevenexp.craftit.ui.adapter

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
import com.sevenexp.craftit.data.response.items.HandicraftItem
import com.sevenexp.craftit.databinding.ItemPostBinding
import com.sevenexp.craftit.widget.Helper

class CraftItemAdapter : RecyclerView.Adapter<CraftItemAdapter.CraftItemViewHolder>() {
    private val listPost = ArrayList<HandicraftItem>()

    override fun getItemCount(): Int = listPost.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CraftItemViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CraftItemViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CraftItemViewHolder, position: Int) {
        holder.bind(listPost[position])
        holder.itemView.setOnClickListener {
//            TODO: Implement intent to detail activity
        }
    }


    fun setData(newItems: List<HandicraftItem>) {
        val oldList = ArrayList(listPost)
        val diffResult = DiffUtil.calculateDiff(DiffUtilCallback(oldList, newItems))
        listPost.clear()
        listPost.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class CraftItemViewHolder(itemView: View) : ViewHolder(itemView) {
        private val binding = ItemPostBinding.bind(itemView)

        fun bind(post: HandicraftItem) {
            binding.apply {
                likeCount.text = post.likes.toString()
                imageCount.text = post.totalImages.toString()
                stepCount.text = post.totalStep.toString()
                postTitle.text = post.name
                userLabel.text = post.tags?.take(2)?.joinToString(separator = ", ")
                userTime.text = Helper.getTimelineUpload(binding.root.context, post.updatedAt ?: post.createdAt ?: "1970-01-01T00:00:00.000Z")
                userName.text = post.createdBy
                setImage(post.image ?: "", postImage)
                setImage(post.userPhoto ?: "", profileImage, 50)
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
        private val oldList: List<HandicraftItem>,
        private val newList: List<HandicraftItem>
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