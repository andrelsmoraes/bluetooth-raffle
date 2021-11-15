package org.andrelsmoraes.bluetoothraffle.ui

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import org.andrelsmoraes.bluetoothraffle.R
import org.andrelsmoraes.bluetoothraffle.databinding.ActivityMainBinding
import org.andrelsmoraes.bluetoothraffle.utils.viewBinding

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)

    private lateinit var fragmentDevice: DevicesFragment
    private lateinit var fragmentRaffled: RaffledFragment
    private lateinit var fragmentSettings: SettingsFragment

    private lateinit var current: Fragment

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementsUseOverlay = false

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initializeComponents()
    }

    private fun initializeComponents() {
        binding.bottomView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_devices -> {
                    changeFragment(fragmentDevice)
                    true
                }
                R.id.navigation_raffled -> {
                    fragmentRaffled.onNavigationSelected()
                    changeFragment(fragmentRaffled)
                    true
                }
                R.id.navigation_settings -> {
                    changeFragment(fragmentSettings)
                    true
                }
                else -> false
            }
        }

        fragmentDevice = DevicesFragment.newInstance()
        fragmentRaffled = RaffledFragment.newInstance()
        fragmentSettings = SettingsFragment.newInstance()
        current = fragmentDevice

        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, fragmentSettings)
            .hide(fragmentSettings)
            .commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, fragmentRaffled)
            .hide(fragmentRaffled)
            .commit()
        supportFragmentManager.beginTransaction()
            .add(R.id.fragmentContainer, fragmentDevice)
            .commit()
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit)
            .hide(current)
            .show(fragment)
            .commit()

        current = fragment
    }

}
