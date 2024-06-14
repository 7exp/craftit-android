package com.sevenexp.craftit.widget

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.sevenexp.craftit.R
import com.sevenexp.craftit.databinding.CustomRecyclerviewBinding
import com.sevenexp.craftit.databinding.RecyclerviewEmptyBinding
import com.sevenexp.craftit.databinding.RecyclerviewErrorBinding
import com.sevenexp.craftit.databinding.RecyclerviewLoadingBinding

class CustomRecyclerView(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : ConstraintLayout(context, attrs, defStyleAttr) {
    constructor(context: Context) : this(context, null, 0)
    constructor(context: Context, attrs: AttributeSet) : this(context, attrs, 0)

    private val binding = CustomRecyclerviewBinding.inflate(LayoutInflater.from(context), this)

    private val errorBinding: RecyclerviewErrorBinding = binding.customError
    private val emptyBinding: RecyclerviewEmptyBinding = binding.customEmptyView
    private val loadingBinding: RecyclerviewLoadingBinding = binding.customOverlayView

    val recyclerView: RecyclerView get() = binding.customRecycler

    init {
        recyclerView.clipToPadding = false

        context.theme.obtainStyledAttributes(
            attrs,
            R.styleable.MyRecyclerView,
            0,
            0
        ).apply {
            try {
                val pH = getInt(R.styleable.MyRecyclerView_paddingHorizontal, 0)
                val pV = getInt(R.styleable.MyRecyclerView_paddingVertical, 0)
                recyclerView.setPadding(pH, pV, pH, pV)
            } finally {
                recycle()
            }
        }
    }

    private fun showEmptyView() {
        loadingBinding.root.visibility = View.GONE
        errorBinding.root.visibility = View.GONE

        emptyBinding.root.visibility = View.VISIBLE
    }

    private fun showErrorView() {
        loadingBinding.root.visibility = View.GONE
        emptyBinding.root.visibility = View.GONE

        errorBinding.root.visibility = View.VISIBLE
    }

    private fun showLoadingView() {
        emptyBinding.root.visibility = View.GONE
        errorBinding.root.visibility = View.GONE

        loadingBinding.root.visibility = View.VISIBLE
    }

    private fun hideAllViews() {
        loadingBinding.root.visibility = View.GONE
        errorBinding.root.visibility = View.GONE
        emptyBinding.root.visibility = View.GONE
    }

    fun showView(state: ViewStatus) {
        hideAllViews()
        when (state) {
            ViewStatus.LOADING -> showLoadingView()
            ViewStatus.ERROR -> showErrorView()
            ViewStatus.EMPTY -> showEmptyView()
            ViewStatus.ON_DATA -> {}

        }
    }

    fun setOnRetryClickListener(callback: () -> Unit) {
        errorBinding.rvBtnReload.setOnClickListener {
            callback()
        }
        emptyBinding.rvBtnReload.setOnClickListener {
            callback()
        }
    }


    enum class ViewStatus {
        ON_DATA,
        LOADING,
        ERROR,
        EMPTY,
    }
}