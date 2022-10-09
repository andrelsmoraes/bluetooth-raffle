package org.andrelsmoraes.bluetoothraffle.ui

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.preference.Preference
import androidx.preference.PreferenceFragmentCompat
import org.andrelsmoraes.bluetoothraffle.BuildConfig
import org.andrelsmoraes.bluetoothraffle.R
import org.andrelsmoraes.bluetoothraffle.utils.getStringVersion

class SettingsFragment : PreferenceFragmentCompat() {

    companion object {
        private val TAG = SettingsFragment::class.java.simpleName
        private const val APP_PRO_ID = "org.andrelsmoraes.bluetoothraffle"

        fun newInstance(): SettingsFragment {
            return SettingsFragment()
        }
    }

    override fun onCreatePreferences(savedInstanceState: Bundle?, rootKey: String?) {
        setPreferencesFromResource(R.xml.preference_screen_settings, rootKey)

        val versionNamePreference: Preference? =
            findPreference(getString(R.string.key_version_name))
        versionNamePreference?.summary = requireContext().getStringVersion()

        val versionMessagePreference: Preference? =
            findPreference(getString(R.string.key_version_message))
        versionMessagePreference?.setOnPreferenceClickListener {
            openGooglePlay()
            true
        }
    }

    private fun openGooglePlay() {
        runCatching {
            val googlePlayIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse("market://details?id=$APP_PRO_ID")
                setPackage("com.android.vending")
            }
            requireContext().startActivity(googlePlayIntent)
        }.onFailure { error ->
            Log.e(TAG, error.stackTraceToString())
            openBrowser()
        }
    }

    private fun openBrowser() {
        runCatching {
            val browserIntent = Intent(Intent.ACTION_VIEW).apply {
                data = Uri.parse(
                    "https://play.google.com/store/app/details?id=$APP_PRO_ID"
                )
            }
            requireContext().startActivity(browserIntent)
        }.onFailure { error ->
            Log.e(TAG, error.stackTraceToString())
            Toast.makeText(requireContext(), R.string.toast_error, Toast.LENGTH_SHORT).show()
        }

    }

}
