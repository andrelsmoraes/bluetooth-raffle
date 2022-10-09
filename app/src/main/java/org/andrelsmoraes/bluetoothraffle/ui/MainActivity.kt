package org.andrelsmoraes.bluetoothraffle.ui

import android.os.Bundle
import android.view.Window
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import com.google.android.material.transition.platform.MaterialContainerTransformSharedElementCallback
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import org.andrelsmoraes.bluetoothraffle.R
import org.andrelsmoraes.bluetoothraffle.databinding.ActivityMainBinding
import org.andrelsmoraes.bluetoothraffle.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class MainActivity : AppCompatActivity() {

    private val binding by viewBinding(ActivityMainBinding::inflate)
    private val viewModel: MainViewModel by viewModel()

    private lateinit var fragmentDevice: DevicesFragment
    private lateinit var fragmentRaffled: RaffledFragment
    private lateinit var fragmentSettings: SettingsFragment

    private lateinit var current: Fragment

    @FlowPreview
    override fun onCreate(savedInstanceState: Bundle?) {
        window.requestFeature(Window.FEATURE_ACTIVITY_TRANSITIONS)
        setExitSharedElementCallback(MaterialContainerTransformSharedElementCallback())
        window.sharedElementsUseOverlay = false

        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initViews()
        initFragments()
        observeData()
    }

    private fun initViews() {
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
    }

    private fun initFragments() {
        fragmentDevice = DevicesFragment.newInstance()
        fragmentRaffled = RaffledFragment.newInstance()
        fragmentSettings = SettingsFragment.newInstance()
        current = fragmentDevice

        supportFragmentManager.commit {
            add(R.id.fragmentContainer, fragmentSettings)
            hide(fragmentSettings)
            add(R.id.fragmentContainer, fragmentRaffled)
            hide(fragmentRaffled)
            add(R.id.fragmentContainer, fragmentDevice)
        }
    }

    private fun changeFragment(fragment: Fragment) {
        supportFragmentManager.commit {
            setCustomAnimations(R.anim.fragment_enter, R.anim.fragment_exit)
            hide(current)
            show(fragment)
        }
        current = fragment
    }

    private fun updateSizeBadge(menuItemId: Int, size: Int) {
        binding.bottomView.getOrCreateBadge(menuItemId).apply {
            isVisible = size > 0
            number = size
        }
    }

    @FlowPreview
    private fun observeData() {
        lifecycleScope.launch {
            viewModel.sizeData
                .flowWithLifecycle(lifecycle)
                .distinctUntilChanged()
                .collect { devicesSize ->
                    updateSizeBadge(R.id.navigation_devices, devicesSize.remainingSize)
                    updateSizeBadge(R.id.navigation_raffled, devicesSize.raffledSize)
                }
        }
        lifecycleScope.launch {
            viewModel.foundData
                .flowWithLifecycle(lifecycle)
                .distinctUntilChanged()
                .collect {
                    binding.bottomView.getOrCreateBadge(R.id.navigation_devices).apply {
                        isVisible = false
                        clearNumber()
                    }
                }
        }
        viewModel.observeSizes()
    }

}
