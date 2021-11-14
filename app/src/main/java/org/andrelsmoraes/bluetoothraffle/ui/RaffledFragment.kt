package org.andrelsmoraes.bluetoothraffle.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import kotlinx.android.synthetic.main.fragment_raffled.*
import org.andrelsmoraes.bluetoothraffle.databinding.FragmentRaffledBinding
import org.andrelsmoraes.bluetoothraffle.ui.misc.DeviceListAdapter
import org.andrelsmoraes.bluetoothraffle.utils.addDividerVertical
import org.koin.android.viewmodel.ext.android.viewModel

class RaffledFragment : Fragment() {

    val viewModel: RaffledViewModel by viewModel()

    private val raffledListAdapter = DeviceListAdapter()

    companion object {
        fun newInstance(): RaffledFragment {
            return RaffledFragment()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewModel.raffledData.observe(this, { devices ->
            raffledListAdapter.setItems(devices)
        })
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        setHasOptionsMenu(false)
        val binding = FragmentRaffledBinding.inflate(
            inflater, container, false).also {
            it.lifecycleOwner = viewLifecycleOwner
            it.viewModel = viewModel
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        recyclerRaffled.adapter = raffledListAdapter
        recyclerRaffled.addDividerVertical()
    }

    fun onNavigationSelected() {
        viewModel.refreshDevices()
    }

}
