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
import com.sevenexp.craftit.data.response.items.FypItems
import com.sevenexp.craftit.databinding.ItemPostBinding

class SearchAdapter : RecyclerView.Adapter<SearchAdapter.CraftItemViewHolder>() {
    private val listPost = ArrayList<FypItems>()

    override fun getItemCount(): Int = listPost.size


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CraftItemViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CraftItemViewHolder(binding.root)
    }

    override fun onBindViewHolder(holder: CraftItemViewHolder, position: Int) {
        holder.bind(listPost[position])
    }


    fun setData(newItems: List<FypItems>) {
        val oldList = ArrayList(listPost)
        val diffResult = DiffUtil.calculateDiff(DiffUtilCallback(oldList, newItems))
        listPost.clear()
        listPost.addAll(newItems)
        diffResult.dispatchUpdatesTo(this)
    }

    inner class CraftItemViewHolder(itemView: View) : ViewHolder(itemView) {
        private val binding = ItemPostBinding.bind(itemView)

        fun bind(post: FypItems) {
            binding.apply {
                likeCount.text = post.likes.toString()
                stepCount.text = post.totalStep.toString()
                postTitle.text = post.name

                userLabel.text = post.tagsItems.joinToString(separator = ", ")
                userName.text = post.createdBy
                setImage(post.image, postImage)
                setImage(post.imageUser, profileImage)
            }
        }

        private fun setImage(imageUrl: String, imageView: ImageView) {
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
        private val oldList: List<FypItems>,
        private val newList: List<FypItems>
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