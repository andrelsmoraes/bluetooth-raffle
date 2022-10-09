package org.andrelsmoraes.bluetoothraffle.data.repository

import android.content.Context
import androidx.preference.PreferenceManager
import org.andrelsmoraes.bluetoothraffle.R
import org.andrelsmoraes.bluetoothraffle.domain.repository.PreferencesRepository
import org.andrelsmoraes.bluetoothraffle.utils.time.Seconds
import org.andrelsmoraes.bluetoothraffle.utils.toInt

class PreferencesRepositoryImpl(private val context: Context) : PreferencesRepository {

    override fun getDevicesSearchPeriod() = Seconds(
        PreferenceManager.getDefaultSharedPreferences(context).toInt(
            context.getString(R.string.key_devices_search_period),
            context.resources.getInteger(R.integer.default_devices_search_period)
        )
    )

    override fun getRaffleQuantityPerRound() =
        PreferenceManager.getDefaultSharedPreferences(context).toInt(
            context.getString(R.string.key_raffle_quantity_per_round),
            context.resources.getInteger(R.integer.default_raffle_quantity_per_round)
        )

    override fun isShakeToRaffleEnabled() =
        PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
            context.getString(R.string.key_shake_to_raffle_enabled),
            context.resources.getBoolean(R.bool.default_shake_to_raffle_enabled)
        )

}
