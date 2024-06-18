package com.sevenexp.craftit.utils

import android.content.Context
import android.view.Gravity
import android.view.View
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import com.sevenexp.craftit.R

class TopSnackBar(private val view: View) {
    private var snackbar: Snackbar? = null

    private fun show(message: String, bgColor: Int) {
        snackbar?.dismiss()
        snackbar = Snackbar.make(view, message, Snackbar.LENGTH_LONG).apply {
            setBackgroundTint(ContextCompat.getColor(context, bgColor))
            setTextColor(ContextCompat.getColor(context, R.color.white))
            animationMode = Snackbar.ANIMATION_MODE_FADE

            view.apply {
                translationY = 64f
                background = ContextCompat.getDrawable(context, R.drawable.snackbar_rounded)
                (layoutParams as FrameLayout.LayoutParams).apply {
                    gravity = Gravity.TOP or Gravity.CENTER_HORIZONTAL
                    setMargins(16, 32, 16, 32)
                }
            }
            show()
        }
    }

    fun error(message: String) = show(message, R.color.error)
    fun success(message: String) = show(message, R.color.primary)
    fun warning(message: String) = show(message, R.color.warning)
    fun info(message: String) = show(message, R.color.info)
}