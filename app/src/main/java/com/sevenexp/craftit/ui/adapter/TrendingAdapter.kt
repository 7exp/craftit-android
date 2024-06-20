package com.sevenexp.craftit.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import androidx.core.content.ContextCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.facebook.shimmer.Shimmer
import com.facebook.shimmer.ShimmerDrawable
import com.sevenexp.craftit.R
import com.sevenexp.craftit.data.source.database.entity.TrendingEntity
import com.sevenexp.craftit.databinding.ItemTrendingBinding
import com.sevenexp.craftit.ui.detail.DetailActivity

class TrendingAdapter :
    PagingDataAdapter<TrendingEntity, TrendingAdapter.ViewHolder>(itemDiffCallback) {
    class ViewHolder(private val binding: ItemTrendingBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val shimmerDrawable = ShimmerDrawable()
        fun bind(post: TrendingEntity) {
            with(binding) {

                setImage(post.image, postImage)
                postTitle.text = post.name
                likeCount.text = post.likes.toString()
                stepCount.text = post.totalStep.toString()

                root.setOnClickListener {
                    val intent = Intent(root.context, DetailActivity::class.java)
                    intent.putExtra(DetailActivity.EXTRA_CRAFT_ID, post.id)
                    intent.putExtra(DetailActivity.EXTRA_TITLE, post.name)

                    it.context.startActivity(intent)
                }
            }
        }

        private fun setImage(url: String, target: ImageView) {
            val shimmer = Shimmer.AlphaHighlightBuilder().setBaseAlpha(0.9f).setHighlightAlpha(1f)
                .setAutoStart(true).setTilt(32f).build()
            shimmerDrawable.setShimmer(shimmer)

            with(binding) {
                Glide.with(binding.root).load(url).placeholder(shimmerDrawable)
                    .error(ContextCompat.getDrawable(root.context, R.drawable.noimage)).into(target)
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
        val binding =
            ItemTrendingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    companion object {
        val itemDiffCallback = object : DiffUtil.ItemCallback<TrendingEntity>() {
            override fun areItemsTheSame(
                oldItem: TrendingEntity, newItem: TrendingEntity
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: TrendingEntity, newItem: TrendingEntity
            ): Boolean {
                return oldItem == newItem
            }
        }
    }
}