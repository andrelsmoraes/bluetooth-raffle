package org.andrelsmoraes.bluetoothraffle.utils

import FragmentViewBindingDelegate
import android.Manifest
import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.Context
import android.content.SharedPreferences
import android.content.pm.PackageManager
import android.os.Build
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.ConfigurationCompat
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import androidx.viewbinding.ViewBinding
import org.andrelsmoraes.bluetoothraffle.R
import org.andrelsmoraes.bluetoothraffle.utils.time.Seconds
import org.andrelsmoraes.bluetoothraffle.utils.time.SecondsProgression
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

inline fun <T : ViewBinding> AppCompatActivity.viewBinding(
    crossinline bindingInflater: (LayoutInflater) -> T
) = lazy(LazyThreadSafetyMode.NONE) {
    bindingInflater.invoke(layoutInflater)
}

fun <T : ViewBinding> Fragment.viewBinding(viewBindingFactory: (View) -> T) =
    FragmentViewBindingDelegate(this, viewBindingFactory)

inline fun Context.safeRunWithBluetoothPermissions(onGranted: () -> Unit, onDenied: () -> Unit) {
    val permission = if (Build.VERSION.SDK_INT < Build.VERSION_CODES.S) {
        Manifest.permission.ACCESS_COARSE_LOCATION
    } else {
        Manifest.permission.BLUETOOTH_CONNECT
    }
    if (checkSelfPermission(permission) == PackageManager.PERMISSION_GRANTED) {
        onGranted()
    } else {
        onDenied()
    }
}

fun Context.getStringVersion(): String {
    val versionPrefix = getString(R.string.preferences_title_version)
    val versionName = getString(R.string.preferences_version_name)
    return "$versionPrefix $versionName"
}

fun View.visibleOrGone(visible: Boolean) {
    val visibility = if (visible) View.VISIBLE else View.GONE
    val animate = visibility != this.visibility
    if (animate) {
        val alphaStart = if (visible) Opacity.VIEW_OPACITY_0 else Opacity.VIEW_OPACITY_100
        val alphaEnd = if (visible) Opacity.VIEW_OPACITY_100 else Opacity.VIEW_OPACITY_0
        val duration: Long = if (visible) 600 else 300
        alpha = alphaStart
        this.visibility = View.VISIBLE
        animate().alpha(alphaEnd).setDuration(duration).setListener(object : AnimatorListenerAdapter() {
            override fun onAnimationEnd(animation: Animator) {
                this@visibleOrGone.visibility = visibility
            }
        })

    } else {
        this.visibility = visibility
    }
}

fun Seconds.formatToMinutesAndSeconds(): String {
    val value = if (this.toMillis() < 0) 0 else this.toMillis()
    return String.format("%02d:%02d",
        TimeUnit.MILLISECONDS.toMinutes(value) - TimeUnit.HOURS.toMinutes(TimeUnit.MILLISECONDS.toHours(value)),
        TimeUnit.MILLISECONDS.toSeconds(value) - TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(value))
    )
}

infix fun Seconds.downTo(to: Seconds) = SecondsProgression(this, to, -1)

fun MenuItem.enable(enabled: Boolean = true) {
    this.isEnabled = enabled
    this.icon?.alpha = if (enabled) Opacity.ICON_OPACITY_100 else Opacity.ICON_OPACITY_50
}

fun MenuItem.disable() {
    enable(false)
}

fun SharedPreferences.toInt(key: String, defValue: Int): Int {
    (10 downTo 0)

    return this.getString(key, defValue.toString())?.toInt() ?: defValue
}

fun Date.formatToTime(context: Context): String {
    val locale = try {
        ConfigurationCompat.getLocales(context.resources.configuration)[0]
    } catch (e: Exception) {
        Locale.getDefault()
    }

    return SimpleDateFormat("HH:mm", locale).format(this)
}

fun RecyclerView.addDividerVertical() =
    this.addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
