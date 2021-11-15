package org.andrelsmoraes.bluetoothraffle.ui

import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.fragment.app.Fragment
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import org.andrelsmoraes.bluetoothraffle.R
import org.andrelsmoraes.bluetoothraffle.databinding.FragmentDevicesBinding
import org.andrelsmoraes.bluetoothraffle.domain.model.Device
import org.andrelsmoraes.bluetoothraffle.ui.misc.DeviceListAdapter
import org.andrelsmoraes.bluetoothraffle.utils.UiStateEvent
import org.andrelsmoraes.bluetoothraffle.utils.addDividerVertical
import org.andrelsmoraes.bluetoothraffle.utils.disable
import org.andrelsmoraes.bluetoothraffle.utils.enable
import org.andrelsmoraes.bluetoothraffle.utils.viewBinding
import org.koin.androidx.viewmodel.ext.android.viewModel

class DevicesFragment : Fragment(R.layout.fragment_devices) {

    private val binding by viewBinding(FragmentDevicesBinding::bind)
    val viewModel: DevicesViewModel by viewModel()

    private var menuItemSearchDevices: MenuItem? = null
    private val deviceListAdapter = DeviceListAdapter()

    private val activityResultLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult(),
    ) {
        binding.fabRaffle.extend()
        viewModel.refreshDevices()
    }

    companion object {
        const val TRANSITION_TO_RAFFLED_NAME = "transition_devices_to_new_raffled"

        fun newInstance(): DevicesFragment {
            return DevicesFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        binding.apply {
            lifecycleOwner = this@DevicesFragment.viewLifecycleOwner
            viewModel = this@DevicesFragment.viewModel

            recyclerDevices.adapter = deviceListAdapter
            recyclerDevices.addDividerVertical()
        }

        viewLifecycleOwner.apply {
            viewModel.devicesData.observe(this, { devices ->
                deviceListAdapter.setItems(devices)
            })

            viewModel.raffledData.observe(this, { devices ->
                openRaffledActivity(devices)
            })

            viewModel.uiState.observe(this, { state ->
                when (state) {
                    is UiStateEvent.Loading -> menuItemSearchDevices?.disable()
                    else -> menuItemSearchDevices?.enable()
                }
            })
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.devices_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
        menuItemSearchDevices = menu.findItem(R.id.menuSearchDevices)
    }

    @FlowPreview
    @ExperimentalCoroutinesApi
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menuSearchDevices -> {
                viewModel.startDevicesSearch()

                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun openRaffledActivity(devices: List<Device>) {
        val intent = NewRaffledActivity.createIntent(this.requireContext(), devices)
        val transition = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            binding.fabRaffle,
            TRANSITION_TO_RAFFLED_NAME
        )
        activityResultLauncher.launch(intent, transition)
    }

}
