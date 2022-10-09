package org.andrelsmoraes.bluetoothraffle.ui

import android.app.Activity.RESULT_OK
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.Settings
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.app.ActivityOptionsCompat
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.andrelsmoraes.bluetoothraffle.R
import org.andrelsmoraes.bluetoothraffle.databinding.FragmentDevicesBinding
import org.andrelsmoraes.bluetoothraffle.domain.model.Device
import org.andrelsmoraes.bluetoothraffle.ui.misc.BluetoothPermission
import org.andrelsmoraes.bluetoothraffle.ui.misc.DeviceListAdapter
import org.andrelsmoraes.bluetoothraffle.utils.ShakeDetector
import org.andrelsmoraes.bluetoothraffle.utils.UiStateEvent
import org.andrelsmoraes.bluetoothraffle.utils.addDividerVertical
import org.andrelsmoraes.bluetoothraffle.utils.disable
import org.andrelsmoraes.bluetoothraffle.utils.enable
import org.andrelsmoraes.bluetoothraffle.utils.viewBinding
import org.andrelsmoraes.bluetoothraffle.utils.visibleOrGone
import org.koin.androidx.viewmodel.ext.android.viewModel

class DevicesFragment : Fragment(R.layout.fragment_devices) {

    private val binding by viewBinding(FragmentDevicesBinding::bind)
    private val viewModel: DevicesViewModel by viewModel()

    private var menuItemSearchDevices: MenuItem? = null
    private val deviceListAdapter = DeviceListAdapter()
    private var bluetoothAdapter: BluetoothAdapter? = null
    private var shakeDetector: ShakeDetector? = null
    private var appSettingsResultLauncher: ActivityResultLauncher<Intent>? = null
    private var raffledResultLauncher: ActivityResultLauncher<Intent>? = null
    private var bluetoothEnableResultLauncher: ActivityResultLauncher<Intent>? = null
    private var bluetoothPermission: BluetoothPermission? = null

    companion object {
        const val TRANSITION_TO_RAFFLED_NAME = "transition_devices_to_new_raffled"

        fun newInstance(): DevicesFragment {
            return DevicesFragment()
        }
    }

    @FlowPreview
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initBluetooth()
        initShakeSensor()
        initViews()
        initMenu()
        initLaunchers()
        observeData()
        configurePermissionWarning()
    }

    override fun onResume() {
        super.onResume()
        shakeDetector?.registerSensor()
    }

    override fun onPause() {
        super.onPause()
        shakeDetector?.unregisterSensor()
    }

    @FlowPreview
    override fun onDestroyView() {
        super.onDestroyView()
        viewModel.stopDevicesSearch()
        raffledResultLauncher = null
        bluetoothEnableResultLauncher = null
        bluetoothPermission?.clear()
        bluetoothPermission = null
    }

    @FlowPreview
    private fun initViews() {
        binding.apply {
            recyclerDevices.adapter = deviceListAdapter
            recyclerDevices.addDividerVertical()

            layoutEmptyStateDevices.buttonSearchDevices.setOnClickListener {
                onSearchDeviceClicked()
            }
            layoutPermissionMissingState.buttonOpenSettings.setOnClickListener {
                onOpenAppSettings()
            }
            layoutSearchingDevices.buttonStopSearch.setOnClickListener {
                viewModel.stopDevicesSearch()
            }
            fabRaffle.setOnClickListener {
                if (isVisible && binding.fabRaffle.isVisible) {
                    viewModel.raffle()
                }
            }
        }
    }

    private fun initBluetooth() {
        bluetoothAdapter = requireContext().getSystemService(BluetoothManager::class.java)?.adapter
        bluetoothPermission = BluetoothPermission.Builder.create(
            fragment = this,
            onGranted = { enableBluetooth() },
            onDenied = { viewModel.onPermissionError() }
        )
    }

    @FlowPreview
    private fun initShakeSensor() {
        shakeDetector = ShakeDetector(requireContext())
        shakeDetector?.shakeListener = object : ShakeDetector.ShakeListener {
            override fun onShakeDetected() {
                if (isVisible && binding.fabRaffle.isVisible && viewModel.isShakeEnabled()) {
                    viewModel.raffle()
                }
            }
        }
    }

    @FlowPreview
    private fun initMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.devices_menu, menu)
                menuItemSearchDevices = menu.findItem(R.id.menuSearchDevices)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.menuSearchDevices -> {
                        if (bluetoothAdapter?.isEnabled == true) {
                            Toast.makeText(
                                requireContext(),
                                R.string.toast_searching_devices_start,
                                Toast.LENGTH_SHORT
                            ).show()
                        }
                        onSearchDeviceClicked()
                        return true
                    }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    @FlowPreview
    private fun initLaunchers() {
        raffledResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult(),
        ) {
            binding.fabRaffle.extend()
            viewModel.refreshDevices()
        }
        appSettingsResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
                if (bluetoothPermission?.isGranted() == true) {
                    viewModel.refreshDevices()
                } else {
                    viewModel.onPermissionError()
                }
            }
        bluetoothEnableResultLauncher =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == RESULT_OK && bluetoothAdapter?.isEnabled == true
                        && bluetoothPermission?.isGranted() == true) {
                    viewModel.startDevicesSearch()
                } else {
                    Toast.makeText(
                        requireContext(),
                        R.string.text_bluetooth_off,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.devicesData
                .flowWithLifecycle(lifecycle)
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .collect { devices ->
                    deviceListAdapter.setItems(devices)
                }
        }
        lifecycleScope.launch {
            viewModel.raffledData
                .flowWithLifecycle(lifecycle)
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .collect { devices ->
                    openRaffledActivity(devices)
                }
        }
        lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(lifecycle)
                .collect(::configureUiState)
        }
        lifecycleScope.launch {
            viewModel.timerData
                .flowWithLifecycle(lifecycle)
                .collect(::updateTimer)
        }
    }

    @FlowPreview
    private fun onSearchDeviceClicked() {
        if (bluetoothAdapter?.isEnabled == true && bluetoothPermission?.isGranted() == true) {
            viewModel.startDevicesSearch()
        } else {
            enableBluetooth()
        }
    }

    private fun onOpenAppSettings() {
        val settingsIntent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
            data = Uri.fromParts("package", requireContext().packageName, null)
        }
        appSettingsResultLauncher?.launch(settingsIntent)
    }

    private fun enableBluetooth() {
        if (bluetoothPermission?.isGranted() == true) {
            val enableBluetoothIntent = Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE)
            bluetoothEnableResultLauncher?.launch(enableBluetoothIntent)
        } else {
            bluetoothPermission?.requestPermission()
        }
    }

    private fun configurePermissionWarning() {
        val text = bluetoothPermission?.getPermissionMessage()
        if (bluetoothPermission?.isGranted() == true || text == null) {
            binding.layoutEmptyStateDevices.textPermissionWarning.visibleOrGone(false)
            binding.layoutPermissionMissingState.textPermissionWarning.visibleOrGone(false)
            return
        }
        binding.layoutEmptyStateDevices.textPermissionWarning.text = text
        binding.layoutEmptyStateDevices.textPermissionWarning.visibleOrGone(true)
        binding.layoutPermissionMissingState.textPermissionWarning.text = text
        binding.layoutPermissionMissingState.textPermissionWarning.visibleOrGone(true)
    }

    private fun configureUiState(state: UiStateEvent) {
        when (state) {
            is UiStateEvent.Loading -> menuItemSearchDevices?.disable()
            is UiStateEvent.Error -> {
                Toast.makeText(
                    requireContext(),
                    R.string.toast_error,
                    Toast.LENGTH_SHORT
                ).show()
                return
            }
            else -> menuItemSearchDevices?.enable()
        }
        configurePermissionWarning()
        binding.apply {
            layoutEmptyStateDevices.root.visibleOrGone(state == UiStateEvent.Empty)
            layoutSearchingDevices.root.visibleOrGone(state == UiStateEvent.Loading)
            layoutPermissionMissingState.root
                .visibleOrGone(state == UiStateEvent.PermissionMissing)
            recyclerDevices.visibleOrGone(state == UiStateEvent.Success)
            fabRaffle.visibleOrGone(state == UiStateEvent.Success)
        }
    }

    private fun updateTimer(remainingTime: String) {
        binding.layoutSearchingDevices.textTimer.text =
            getString(R.string.text_searching_devices, remainingTime)
    }

    private fun openRaffledActivity(devices: List<Device>) {
        val intent = NewRaffledActivity.createIntent(this.requireContext(), devices)
        val transition = ActivityOptionsCompat.makeSceneTransitionAnimation(
            requireActivity(),
            binding.fabRaffle,
            TRANSITION_TO_RAFFLED_NAME
        )
        raffledResultLauncher?.launch(intent, transition)
    }

}
