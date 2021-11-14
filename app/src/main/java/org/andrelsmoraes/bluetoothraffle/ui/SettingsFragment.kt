package org.andrelsmoraes.bluetoothraffle.ui

import android.os.Bundle
import androidx.preference.PreferenceFragmentCompat
import org.andrelsmoraes.bluetoothraffle.R

class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen_settings, rootKey)
    }

}
