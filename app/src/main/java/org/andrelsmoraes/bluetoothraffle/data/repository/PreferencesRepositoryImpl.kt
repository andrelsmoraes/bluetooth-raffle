package org.andrelsmoraes.bluetoothraffle.data.repository

import android.content.Context
import androidx.preference.PreferenceManager
import org.andrelsmoraes.bluetoothraffle.R
import org.andrelsmoraes.bluetoothraffle.domain.repository.PreferencesRepository
import org.andrelsmoraes.bluetoothraffle.utils.time.Seconds
import org.andrelsmoraes.bluetoothraffle.utils.toInt

class PreferencesRepositoryImpl(private val context: Context) : PreferencesRepository {

    companion object {
        private const val SECONDS_DEFAULT = 60
        private const val QUANTITY_PER_ROUND_DEFAULT = 1
        private const val SHAKE_TO_RAFFLE_DEFAULT = false
    }

    override fun getDevicesSearchPeriod() = Seconds(
        runCatching {
            PreferenceManager.getDefaultSharedPreferences(context).toInt(
                context.getString(R.string.key_devices_search_period),
                context.resources.getInteger(R.integer.default_devices_search_period)
            )
        }.getOrDefault(SECONDS_DEFAULT)
    )

    override fun getRaffleQuantityPerRound() =
        runCatching {
            PreferenceManager.getDefaultSharedPreferences(context).toInt(
                context.getString(R.string.key_raffle_quantity_per_round),
                context.resources.getInteger(R.integer.default_raffle_quantity_per_round)
            )
        }.getOrDefault(QUANTITY_PER_ROUND_DEFAULT)

    override fun isShakeToRaffleEnabled() =
        runCatching {
            PreferenceManager.getDefaultSharedPreferences(context).getBoolean(
                context.getString(R.string.key_shake_to_raffle_enabled),
                context.resources.getBoolean(R.bool.default_shake_to_raffle_enabled)
            )
        }.getOrDefault(SHAKE_TO_RAFFLE_DEFAULT)

}
