package org.andrelsmoraes.bluetoothraffle.ui

import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_devices.*
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
import org.koin.android.viewmodel.ext.android.viewModel

class DevicesFragment : Fragment() {

    val viewModel: DevicesViewModel by viewModel()

    private var menuItemSearchDevices: MenuItem? = null
    private val deviceListAdapter = DeviceListAdapter()

    companion object {
        private const val REQUEST_OPEN_NEW_RAFFLED = 2003
        const val TRANSITION_TO_RAFFLED_NAME = "transition_devices_to_new_raffled"

        fun newInstance(): DevicesFragment {
            return DevicesFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(true)
        val binding = FragmentDevicesBinding.inflate(
                inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerDevices.adapter = deviceListAdapter
        recyclerDevices.addDividerVertical()
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (REQUEST_OPEN_NEW_RAFFLED == requestCode) {
            fabRaffle.extend()
            viewModel.refreshDevices()
        } else {
            super.onActivityResult(requestCode, resultCode, data)
        }
    }

    private fun openRaffledActivity(devices: List<Device>) {
        val options = ActivityOptions.makeSceneTransitionAnimation(
            requireActivity(),
            fabRaffle,
            TRANSITION_TO_RAFFLED_NAME
        )

        startActivityForResult(
            NewRaffledActivity.createIntent(this.requireContext(), devices),
            REQUEST_OPEN_NEW_RAFFLED,
            options.toBundle()
        )
    }

}
