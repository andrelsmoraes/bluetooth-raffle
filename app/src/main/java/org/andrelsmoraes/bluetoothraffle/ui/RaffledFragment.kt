package org.andrelsmoraes.bluetoothraffle.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.andrelsmoraes.bluetoothraffle.R
import org.andrelsmoraes.bluetoothraffle.databinding.FragmentRaffledBinding
import org.andrelsmoraes.bluetoothraffle.ui.misc.DeviceListAdapter
import org.andrelsmoraes.bluetoothraffle.utils.UiStateEvent
import org.andrelsmoraes.bluetoothraffle.utils.addDividerVertical
import org.andrelsmoraes.bluetoothraffle.utils.viewBinding
import org.andrelsmoraes.bluetoothraffle.utils.visibleOrGone
import org.koin.androidx.viewmodel.ext.android.viewModel

class RaffledFragment : Fragment(R.layout.fragment_raffled) {

    private val binding by viewBinding(FragmentRaffledBinding::bind)
    val viewModel: RaffledViewModel by viewModel()

    private val raffledListAdapter = DeviceListAdapter()

    companion object {
        fun newInstance(): RaffledFragment {
            return RaffledFragment()
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
    }

    private fun initViews() {
        binding.apply {
            recyclerRaffled.adapter = raffledListAdapter
            recyclerRaffled.addDividerVertical()
        }
    }

    private fun observeData() {
        lifecycleScope.launch {
            viewModel.raffledData
                .flowWithLifecycle(lifecycle)
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .collect { devices ->
                    raffledListAdapter.setItems(devices)
                }
        }
        lifecycleScope.launch {
            viewModel.uiState
                .flowWithLifecycle(lifecycle)
                .collect(::configureUiState)
        }
    }

    private fun configureUiState(state: UiStateEvent) {
        if (state == UiStateEvent.Error) {
            Toast.makeText(
                requireContext(),
                R.string.toast_error,
                Toast.LENGTH_SHORT
            ).show()
        }
        binding.apply {
            layoutEmptyStateRaffled.root.visibleOrGone(state == UiStateEvent.Empty)
            recyclerRaffled.visibleOrGone(state == UiStateEvent.Success)
        }
    }

    fun onNavigationSelected() {
        viewModel.refreshDevices()
    }

}
