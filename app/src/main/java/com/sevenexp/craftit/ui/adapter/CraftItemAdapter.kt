package com.sevenexp.craftit.ui.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.sevenexp.craftit.data.response.items.HandicraftItem
import com.sevenexp.craftit.databinding.ItemPostBinding

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