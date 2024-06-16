package com.sevenexp.craftit.ui.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.sevenexp.craftit.R
import com.sevenexp.craftit.data.response.items.FypItems
import com.sevenexp.craftit.databinding.ItemPostBinding

class CraftItemAdapter :
    PagingDataAdapter<FypItems, CraftItemAdapter.ViewHolder>(itemDiffCallback) {
    class ViewHolder(private val binding: ItemPostBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val shimmerDrawable = ShimmerDrawable()
        fun bind(post: FypItems) {
            with(binding) {

                setImage(post.image, postImage)
                setImage(post.imageUser, profileImage)
                postTitle.text = post.name
                userLabel.text = post.tagsItems.joinToString(", ")
                userName.text = post.createdBy
                likeCount.text = post.likes.toString()
                stepCount.text = post.totalStep.toString()
//                userTime.text = Helper.getTimelineUpload()

                root.setOnClickListener {
//                    val intent = Intent(root.context, DetailActivity::class.java)
//                    intent.putExtra(DetailActivity.EXTRA_STORY_ID, story.id)
//                    intent.putExtra(DetailActivity.EXTRA_TITLE, story.name)
//                    intent.putExtra(DetailActivity.EXTRA_IMAGE, story.photoUrl)
//
//                    it.context.startActivity(intent)
                }
            }
        }

        private fun setImage(url: String, target: ImageView) {
            val shimmer = Shimmer.AlphaHighlightBuilder()
                .setBaseAlpha(0.9f)
                .setHighlightAlpha(1f)
                .setAutoStart(true)
                .setTilt(32f)
                .build()
            shimmerDrawable.setShimmer(shimmer)

            with(binding) {
                Glide.with(binding.root)
                    .load(url)
                    .placeholder(shimmerDrawable)
                    .error(ContextCompat.getDrawable(root.context, R.drawable.noimage))
                    .into(target)
            }
        }
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        if (data != null) {
            holder.bind(data)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPostBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    companion object {
        val itemDiffCallback = object : DiffUtil.ItemCallback<FypItems>() {
            override fun areItemsTheSame(oldItem: FypItems, newItem: FypItems): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: FypItems, newItem: FypItems): Boolean {
                return oldItem == newItem
            }
        }
    }
}