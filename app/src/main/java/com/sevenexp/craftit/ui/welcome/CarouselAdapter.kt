package com.sevenexp.craftit.ui.welcome

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.sevenexp.craftit.R
import com.smarteist.autoimageslider.SliderViewAdapter


class CarouselAdapter(private val context: Context, private val items: ArrayList<Drawable>) :
    SliderViewAdapter<CarouselAdapter.SliderAdapterViewHolder>() {
    private val mSliderItems: MutableList<Drawable> = items

    class SliderAdapterViewHolder(itemView: View) : ViewHolder(itemView) {
        var imageView: ImageView = itemView.findViewById(R.id.iv_image)
    }

    override fun onBindViewHolder(viewHolder: SliderAdapterViewHolder?, position: Int) {
        viewHolder?.imageView?.setImageDrawable(mSliderItems[position])
    }

    override fun getCount(): Int = mSliderItems.size
    override fun onCreateViewHolder(parent: ViewGroup?): SliderAdapterViewHolder {
        val inflate: View =
            LayoutInflater.from(parent?.context).inflate(R.layout.carousel_layout, null)
        return SliderAdapterViewHolder(inflate)
    }

}