package org.andrelsmoraes.bluetoothraffle

import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var fragmentDevice: Fragment
    private lateinit var fragmentRaffled: Fragment
    private lateinit var fragmentSettings: Fragment

    private val onNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { item ->
        when (item.itemId) {
            R.id.navigation_devices -> {
                changeFragment(fragmentDevice)
                return@OnNavigationItemSelectedListener true
            }
            R.id.navigation_raffled -> {
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
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initializeComponents()
    }

    private fun initializeComponents() {
        bottomView.setOnNavigationItemSelectedListener(onNavigationItemSelectedListener)

        fragmentDevice = DevicesFragment.newInstance()
        fragmentRaffled = RaffledFragment.newInstance()
        fragmentSettings = SettingsFragment.newInstance()
        changeFragment(fragmentDevice)
    }

    private fun changeFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(R.anim.fade_in, R.anim.fade_out)
        transaction.replace(R.id.fragmentContainer, fragment)
        transaction.commit()
    }

}
