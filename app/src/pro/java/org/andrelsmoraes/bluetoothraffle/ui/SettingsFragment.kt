package org.andrelsmoraes.bluetoothraffle.ui

import android.os.Bundle
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import org.andrelsmoraes.bluetoothraffle.R
import org.andrelsmoraes.bluetoothraffle.utils.getStringVersion

class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen_settings, rootKey)

        val versionNamePreference: Preference? =
            findPreference(getString(R.string.key_version_name))
        versionNamePreference?.summary = requireContext().getStringVersion()
        versionNamePreference?.onPreferenceClickListener = null
        versionNamePreference?.isEnabled = false
        versionNamePreference?.shouldDisableView = false
    }

}
