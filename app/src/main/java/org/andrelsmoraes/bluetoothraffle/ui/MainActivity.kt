package org.andrelsmoraes.bluetoothraffle.ui

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import kotlinx.android.synthetic.main.activity_main.*
import org.andrelsmoraes.bluetoothraffle.R

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentDevice: DevicesFragment
    private lateinit var fragmentRaffled: RaffledFragment
    private lateinit var fragmentSettings: SettingsFragment

    private lateinit var current: Fragment

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_devices -> {
                changeFragment(fragmentDevice)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_raffled -> {
                fragmentRaffled.onNavigationSelected()
                changeFragment(fragmentRaffled)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_settings -> {
                changeFragment(fragmentSettings)
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementsUseOverlay = false

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeComponents()
    }

    private fun initializeComponents() {
        bottomView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

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
            .setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
            .hide(current)
            .show(fragment)
            .commit()

        current = fragment
    }

}
