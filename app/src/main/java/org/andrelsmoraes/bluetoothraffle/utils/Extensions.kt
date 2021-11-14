package org.andrelsmoraes.bluetoothraffle.utils

import android.content.Context
import android.content.SharedPreferences
import android.view.MenuItem
import androidx.core.content.ContextCompat
import androidx.core.os.ConfigurationCompat
import androidx.preference.EditTextPreference
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.RecyclerView
import org.andrelsmoraes.bluetoothraffle.R
import org.andrelsmoraes.bluetoothraffle.utils.time.Seconds
import org.andrelsmoraes.bluetoothraffle.utils.time.SecondsProgression
import java.lang.Exception
import java.lang.NumberFormatException
import java.text.SimpleDateFormat
import java.util.*
import java.util.concurrent.TimeUnit

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
    this.icon.alpha = if (enabled) Opacity.ICON_OPACITY_100 else Opacity.ICON_OPACITY_50
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
