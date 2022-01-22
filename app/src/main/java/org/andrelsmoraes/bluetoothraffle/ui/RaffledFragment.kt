package org.andrelsmoraes.bluetoothraffle.ui

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.flowWithLifecycle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.launch
import org.andrelsmoraes.bluetoothraffle.R
import org.andrelsmoraes.bluetoothraffle.databinding.FragmentRaffledBinding
import org.andrelsmoraes.bluetoothraffle.ui.misc.DeviceListAdapter
import org.andrelsmoraes.bluetoothraffle.utils.addDividerVertical
import org.andrelsmoraes.bluetoothraffle.utils.viewBinding
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
        setHasOptionsMenu(false)

        binding.apply {
            lifecycleOwner = this@RaffledFragment.viewLifecycleOwner
            viewModel = this@RaffledFragment.viewModel

            recyclerRaffled.adapter = raffledListAdapter
            recyclerRaffled.addDividerVertical()
        }

        lifecycleScope.launch {
            viewModel.raffledData
                .flowWithLifecycle(lifecycle)
                .filter { it.isNotEmpty() }
                .distinctUntilChanged()
                .collect { devices ->
                    raffledListAdapter.setItems(devices)
                }
        }
    }

    fun onNavigationSelected() {
        viewModel.refreshDevices()
    }

}
