package org.andrelsmoraes.bluetoothraffle.ui

import android.os.Bundle
import androidx.preference.EditTextPreference
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import org.andrelsmoraes.bluetoothraffle.R
import org.andrelsmoraes.bluetoothraffle.utils.forceNumericInputType
import org.andrelsmoraes.bluetoothraffle.utils.getStringVersion

class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen_settings, rootKey)

        findPreference<EditTextPreference>(getString(R.string.key_devices_search_period))
            ?.forceNumericInputType()
        findPreference<EditTextPreference>(getString(R.string.key_raffle_quantity_per_round))
            ?.forceNumericInputType()

        val versionNamePreference: Preference? =
            findPreference(getString(R.string.key_version_name))
        versionNamePreference?.summary = requireContext().getStringVersion()
        versionNamePreference?.onPreferenceClickListener = null
        versionNamePreference?.isEnabled = false
        versionNamePreference?.shouldDisableView = false
    }

}
