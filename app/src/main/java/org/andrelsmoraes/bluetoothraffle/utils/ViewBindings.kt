package org.andrelsmoraes.bluetoothraffle.utils

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.view.View
import androidx.databinding.BindingAdapter

@BindingAdapter("visibleOrGone")
fun View.visibleOrGone(visible: Boolean) {
    val alphaStart = if (visible) Opacity.VIEW_OPACITY_0 else Opacity.VIEW_OPACITY_100
    val alphaEnd = if (visible) Opacity.VIEW_OPACITY_100 else Opacity.VIEW_OPACITY_0

    this.visibility = if (visible) View.VISIBLE else View.GONE

    alpha = alphaStart
    animate().alpha(alphaEnd).setDuration(500).setListener(object : AnimatorListenerAdapter() {
        override fun onAnimationEnd(animation: Animator?) {
            this@visibleOrGone.visibility = if (visible) View.VISIBLE else View.GONE
        }
    })
}
